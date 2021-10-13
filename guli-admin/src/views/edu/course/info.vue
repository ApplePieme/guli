<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布课程"/>
    </el-steps>
    
    <el-form label-width="120px">
      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
      </el-form-item>

      <!-- 所属分类 -->
      <!-- 一级分类 -->
      <el-form-item label="课程类别">
        <el-select
          v-model="courseInfo.subjectParentId"
          placeholder="一级分类"
          @change="subjectLevelOneChanged">
          <el-option
            v-for="subject in subjectNestedList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>
        <!-- 二级分类 -->
        <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
          <el-option
            v-for="subject in subSubjectList"
            :key="subject.value"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>
      </el-form-item>

      <!-- 课程讲师 -->
      <el-form-item label="课程讲师">
        <el-select
          v-model="courseInfo.teacherId"
          placeholder="请选择">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
      </el-form-item>

      <!-- 课程简介-->
      <el-form-item label="课程简介">
          <tinymce :height="300" v-model="courseInfo.description"/>
      </el-form-item>

      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload
          name="multipartFile"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API+'/oss/file/upload/cover'"
          class="avatar-uploader">
          <img :src="courseInfo.cover">
        </el-upload>
      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="next">保存并下一步</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import course from '@/api/edu/course'
import subject from '@/api/edu/subject'
import Tinymce from '@/components/Tinymce' // 引入富文本编辑器

const defaultForm = {
  title: '',
  subjectId: '',
  subjectParentId: '',
  teacherId: '',
  lessonNum: 0,
  description: '',
  cover: process.env.OSS_PATH + '/cover/default.jpg',
  price: 0
}

export default {
  components: { Tinymce },
  data() {
    return {
      courseId: '',
      courseInfo: defaultForm,
      teacherList: [],
      saveBtnDisabled: false, // 保存按钮是否禁用
      subjectNestedList: [],//一级分类列表
      subSubjectList: [],//二级分类列表
      BASE_API: process.env.BASE_API // 接口API地址
    }
  },
  created() {
    this.init()
  },
  watch: {
    $route(to, from) {
      this.init()
    }
  },
  methods: {
    // 初始化方法
    init() {
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id
        this.getCourseInfoById()
      } else {
        this.courseInfo = {...defaultForm}
        this.getSubjectList()
      }
      this.findAllTeachers()
    },
    // 根据 id 获取课程信息
    getCourseInfoById() {
      course.getCourseInfoById(this.courseId).then(res => {
        this.courseInfo = res.data.item
        subject.getNestedTreeList().then(res => {
          this.subjectNestedList = res.data.items
          for(let i = 0; i < this.subjectNestedList.length; i++) {
            if(this.courseInfo.subjectParentId === this.subjectNestedList[i].id) {
              this.subSubjectList = this.subjectNestedList[i].children
            }
          }
        })
      })
    },
    // 封面上传之前
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    // 封面上传成功之后
    handleAvatarSuccess(res) {
      this.courseInfo.cover = res.data.uploadUrl
    },
    // 根据一级分类获取对应的二级分类
    subjectLevelOneChanged(value) {
      for(let i = 0; i < this.subjectNestedList.length; i++) {
        if(value === this.subjectNestedList[i].id) {
          this.subSubjectList = this.subjectNestedList[i].children
          this.courseInfo.subjectId = ''
        }
      }
    },
    // 获取课程一级分类
    getSubjectList() {
      subject.getNestedTreeList().then(response => {
        this.subjectNestedList = response.data.items
      })
    },
    // 查询所有讲师
    findAllTeachers() {
      course.findAllTeachers().then(response => {
        this.teacherList = response.data.teachers
      })
    },
    // 保存并下一步
    next() {
      this.saveBtnDisabled = true
      if(this.courseInfo.id) {
        this.updateCourseInfo()
      } else {
        this.saveCourseInfo()
      }
    },
    // 新增课程信息
    saveCourseInfo() {
      course.saveCourseInfo(this.courseInfo).then(response => {
        this.$message({
          type: 'success',
          message: '保存成功!'
        })
        this.$router.push({ path: '/edu/course/chapter/' + response.data.courseId })
      })
    },
    // 修改课程信息
    updateCourseInfo() {
      course.updateCourseInfoById(this.courseInfo).then(res => {
        this.$message({
          type: 'success',
          message: '修改成功!'
        })
        this.$router.push({ path: '/edu/course/chapter/' + this.courseId})
      })
    }
  }
}
</script>

<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>