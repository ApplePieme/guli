<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布课程"/>
    </el-steps>

    <el-button type="text" @click="dialogChapterFormVisible = true">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
        <li
            v-for="chapter in chapterNestedList"
            :key="chapter.id">
            <p>
                {{ chapter.title }}
                <span class="acts">
                  <el-button type="text" @click="showVideoDialog(chapter.id)">添加课时</el-button>
                  <el-button type="text" @click="editChapter(chapter.id)">编辑</el-button>
                  <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
                </span>
            </p>
            <!-- 视频 -->
            <ul class="chanpterList videoList">
                <li
                    v-for="video in chapter.children"
                    :key="video.id">
                    <p>
                      {{ video.title }}
                      <span class="acts">
                        <el-button type="text" @click="editVideo(video.id)">编辑</el-button>
                        <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
                      </span>
                    </p>
                </li>
            </ul>
        </li>
    </ul>

    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="编辑章节">
        <el-form :model="chapter" label-width="120px">
            <el-form-item label="章节标题">
                <el-input v-model="chapter.title"/>
            </el-form-item>
            <el-form-item label="章节排序">
                <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="chapterFormClose">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdateChapter">确 定</el-button>
        </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="编辑课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title"/>
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.isFree">
            <el-radio :label="1">免费</el-radio>
            <el-radio :label="0">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频">
          <el-upload
                :on-success="handleVodUploadSuccess"
                :on-remove="handleVodRemove"
                :before-remove="beforeVodRemove"
                :on-exceed="handleUploadExceed"
                :file-list="fileList"
                :action="BASE_API + '/vod/video/upload'"
                :limit="1"
                class="upload-demo">
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
                <div slot="content">最大支持1G，<br>
                    支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                    GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                    MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                    SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
                <i class="el-icon-question"/>
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="videoFormClose">取 消</el-button>
        <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chapter from '@/api/edu/chapter'
import video from '@/api/edu/video'
import vod from '@/api/edu/vod'

const defaultVideo = {
  title: '',
  sort: 0,
  isFree: 0,
  videoSourceId: '',
  videoOriginalName: ''
}

export default {
  data() {
    return {
      courseId: '', // 所属课程
      chapterNestedList: [], // 章节嵌套课时列表
      saveBtnDisabled: false, // 保存按钮是否禁用
      dialogChapterFormVisible: false,
      chapter: {
        title: '',
        sort: 0
      },
      saveVideoBtnDisabled: false, // 课时按钮是否禁用
      dialogVideoFormVisible: false, // 是否显示课时表单
      chapterId: '', // 课时所在的章节id
      video: defaultVideo,// 课时对象
      fileList: [],//上传文件列表
      BASE_API: process.env.BASE_API // 接口API地址
    }
  },
  created() {
    this.init()
  },
  methods: {
    // 点击添加课时按钮显示面板
    showVideoDialog(id) {
      this.video = { ...defaultVideo }
      this.chapterId = id
      this.fileList = []
      this.dialogVideoFormVisible = true
    },
    // 点击编辑课时，回显课时信息
    editVideo(id) {
      this.dialogVideoFormVisible = true
      video.getVideoById(id).then(res => {
        this.video = res.data.item
        this.fileList = this.video.videoOriginalName===null||this.video.videoOriginalName==='' ? [] : [{'name': this.video.videoOriginalName}]
      })
    },
    // 删除课时
    removeVideo(id) {
      this.$confirm('此操作将永久删除该课时和视频, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return video.deleteVideo(id)
      }).then(res => {
        this.fetchChapterNestedListByCourseId()// 刷新列表
        this.$message({
          type: 'success',
          message: res.message
        })
      }).catch((response) => { // 失败
        if (response === 'cancel') {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    },
    // 修改课时
    updateVideo() {
      video.updateVideoById(this.video).then(res => {
        this.$message({
          type: 'success',
          message: res.message
        })
        this.afterSaveOrUpdateVideo()
      })
    },
    // 添加课时
    saveVideo() {
      this.video.courseId = this.courseId
      this.video.chapterId = this.chapterId
      video.saveVideo(this.video).then(res => {
         this.$message({
            type: 'success',
            message: res.message
          })
          this.afterSaveOrUpdateVideo()
      })
    },
    // 添加或修改课时
    saveOrUpdateVideo() {
      if(this.video.id) {
        this.updateVideo()
      } else {
        this.saveVideo()
      }
    },
    // 添加或修改课时成功后的方法
    afterSaveOrUpdateVideo() {
      this.dialogVideoFormVisible = false// 如果保存成功则关闭对话框
      this.fetchChapterNestedListByCourseId()// 刷新列表
      this.video = { ...defaultVideo }
      this.saveVideoBtnDisabled = false
    },
    //上传视频成功回调
    handleVodUploadSuccess(response, file, fileList) {
      this.video.videoSourceId = response.data.videoId
      this.video.videoOriginalName = file.name
    },
    //视图上传多于一个视频
    handleUploadExceed(files, fileList) {
      this.$message.warning('想要重新上传视频，请先删除已上传的视频')
    },
    // 删除视频之前
    beforeVodRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    // 删除视频
    handleVodRemove() {
      vod.removeById(this.video.videoSourceId).then(response=>{
        this.fileList = []
        this.video.videoSourceId = ''
        this.video.videoOriginalName = ''
        this.$message({
          type: 'success',
          message: response.message
        })
      })
    },
    // 编辑课时对话框关闭的方法
    videoFormClose() {
      this.dialogVideoFormVisible = false
      this.video = { ...defaultVideo }
    },
    /* ********************************以下内容为章节************************************* */
    // 编辑章节对话框关闭的方法
    chapterFormClose() {
      this.dialogChapterFormVisible = false
      this.chapter = {}
    },
    // 删除章节
    removeChapter(id) {
      this.$confirm('此操作将永久删除该章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return chapter.deleteChapterById(id)
      }).then(res => {
        this.fetchChapterNestedListByCourseId()// 刷新列表
        this.$message({
          type: 'success',
          message: res.message
        })
      }).catch((response) => { // 失败
        if (response === 'cancel') {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    },
    // 添加或修改章节成功后的方法
    afterSaveOrUpdateChapter() {
      this.dialogChapterFormVisible = false// 如果保存成功则关闭对话框
      this.fetchChapterNestedListByCourseId()// 刷新列表
      this.chapter.title = ''// 重置章节标题
      this.chapter.sort = 0// 重置章节排序
      this.saveBtnDisabled = false
    },
    // 点击编辑按钮，回显章节信息
    editChapter(id) {
      this.dialogChapterFormVisible = true
      chapter.getChapterById(id).then(res => {
        this.chapter = res.data.item
      })
    },
    // 修改章节
    updateChapter() {
      chapter.updateChapterById(this.chapter).then(res => {
        this.$message({
          type: 'success',
          message: res.message
        })
        this.afterSaveOrUpdateChapter()
      })
    },
    // 添加章节
    saveChapter() {
      this.chapter.courseId = this.courseId
      chapter.saveChapter(this.chapter).then(res => {
        this.$message({
          type: 'success',
          message: res.message
        })
        this.afterSaveOrUpdateChapter()
      })
    },
    // 添加或修改章节
    saveOrUpdateChapter() {
      this.saveBtnDisabled = true
      if (!this.chapter.id) {
        this.saveChapter()
      } else {
        this.updateChapter()
      }
    },
    // 初始化方法
    init() {
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id
        this.fetchChapterNestedListByCourseId()
      }
    },
    // 根据id获取课程基本信息
    fetchChapterNestedListByCourseId() {
      chapter.getNestedTreeList(this.courseId).then(res => {
        this.chapterNestedList = res.data.items
      })
    },
    previous() {
      this.$router.push({ path: '/edu/course/info/' + this.courseId })
    },
    next() {
      this.$router.push({ path: '/edu/course/publish/' + this.courseId })
    }
  }
}
</script>

<style scoped>
.chanpterList{
    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;
}
.chanpterList li{
  position: relative;
}
.chanpterList p{
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #DDD;
}
.chanpterList .acts {
    float: right;
    font-size: 14px;
}
.videoList{
  padding-left: 50px;
}
.videoList p{
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #DDD;
}
</style>