<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name"/>
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>

      <!-- 讲师头像 -->
      <el-form-item label="讲师头像">
      <!-- 头衔缩略图 -->
      <pan-thumb :image="teacher.avatar"/>
      <!-- 文件上传按钮 -->
      <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
      </el-button>
      <!--
          v-show：是否显示上传组件
          :key：类似于id，如果一个页面多个图片上传控件，可以做区分
          :url：后台上传的url地址
          @close：关闭上传组件
          @crop-upload-success：上传成功后的回调 
      -->
      <image-cropper
                    v-show="imagecropperShow"
                    :width="300"
                    :height="300"
                    :key="imagecropperKey"
                    :url="BASE_API+'/oss/file/upload/avatar'"
                    field="multipartFile"
                    @close="close"
                    @crop-upload-success="cropSuccess"/>
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import teacher from '@/api/edu/teacher'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

const defaultForm = {
  name: '',
  sort: 0,
  level: '',
  career: '',
  intro: '',
  avatar: process.env.OSS_PATH + '/avatar/default.jpg'
}

export default {
  components: { ImageCropper, PanThumb },
  data() {
    return {
      teacher: defaultForm,
      saveBtnDisabled: false, // 保存按钮是否禁用
      imagecropperShow: false, // 是否显示上传组件
      imagecropperKey: 0, // 上传组件id
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
    // 上传成功后执行
    cropSuccess(data) {
      this.imagecropperShow = false
      this.teacher.avatar = data.uploadUrl
      // 上传成功后，重新打开上传组件时初始化组件，否则显示上一次的上传结果
      this.imagecropperKey = this.imagecropperKey + 1
    },
    // 上传头像框关闭
    close() {
      this.imagecropperShow = false
      // 上传失败后，重新打开上传组件时初始化组件，否则显示上一次的上传结果
      this.imagecropperKey = this.imagecropperKey + 1
    },
    init() {
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id
        this.fetchDataById(id)
      } else {
        // 使用对象拓展运算符，拷贝对象，而不是引用
        // 否则新增一条记录后，defaultForm就变成了之前新增的teacher的值
        this.teacher = {...defaultForm}
      }
    },
    // 保存或更新记录
    saveOrUpdate() {
      this.saveBtnDisabled = true
      if (this.teacher.id) {
        // 如果 teacher 中有id，就是修改记录
        this.updateData()
      } else {
        // 否则就是新增记录
        this.saveData()
      }
    },
    // 保存
    saveData() {
      teacher.save(this.teacher).then(response => {
        return this.$message({
          type: 'success',
          message: '保存成功!'
        })
      }).then(resposne => {
        this.$router.push({ path: '/edu/teacher/list' })
      }).catch((response) => {
        // console.log(response)
        this.$message({
          type: 'error',
          message: '保存失败'
        })
      })
    },
    // 根据id获取记录
    fetchDataById(id) {
      teacher.getById(id)
        .then(response => {
          this.teacher = response.data.teacher
        })
        .catch((response) => {
          this.$message({
            type: 'error',
            message: '获取数据失败'
        })
      })
    },
    // 根据 id 修改记录
    updateData() {
      this.saveBtnDisabled = true
      teacher.updateById(this.teacher).then(response => {
        return this.$message({
            type: 'success',
            message: '修改成功!'
        })
      }).then(resposne => {
        this.$router.push({ path: '/edu/teacher/list' })
      }).catch((response) => {
        // console.log(response)
        this.$message({
            type: 'error',
            message: '保存失败'
        })
      })
    }
  }
}
</script>