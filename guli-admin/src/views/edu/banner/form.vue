<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="幻灯片标题">
        <el-input v-model="bannerInfo.title" placeholder="示例：互联网大厂高频重点面试题。"/>
      </el-form-item>

      <el-form-item label="链接地址">
        <el-select v-model="bannerInfo.linkUrl" placeholder="链接地址">
          <el-option value="/course" label="课程"/>
          <el-option value="/teacher" label="名师"/>
        </el-select>
      </el-form-item>

      <el-form-item label="幻灯片排序">
        <el-input-number v-model="bannerInfo.sort" controls-position="right" :min="0"/>
      </el-form-item>

      <el-form-item label="幻灯片封面">
        <el-upload
          name="multipartFile"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API+'/oss/file/upload/banner'"
          class="avatar-uploader">
          <img :src="bannerInfo.imageUrl">
        </el-upload>
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import banner from '@/api/edu/banner'

const defaultForm = {
  title: '',
  linkUrl: '',
  sort: 0,
  imageUrl: process.env.OSS_PATH + '/banner/default.jpg'
}

export default {
  data() {
    return {
      bannerInfo: defaultForm,
      saveBtnDisabled: false, // 添加按钮是否禁用
      BASE_API: process.env.BASE_API // 接口API地址
    }
  },
  created() {
    this.init()
  },
  watch: {
    $route() {
      this.init()
    }
  },
  methods: {
    // 更新 banner
    updateBanner() {
      banner.updateBannerById(this.bannerInfo).then(res => {
        this.$message({
          type: 'success',
          message: res.message
        })
        this.$router.push({ path: '/edu/banner/list' })
      })
    },
    // 添加或者更新 banner
    saveOrUpdate() {
      this.saveBtnDisabled = true
      if(this.bannerInfo.id) {
        this.updateBanner()
      } else {
        this.createBanner()
      }
    },
    // 根据 ID 查询用作回显
    fetchDataById(id) {
      banner.getBannerById(id).then(res => {
        this.bannerInfo = res.data.item
      })
    },
    // 初始化方法
    init() {
      if(this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id
        this.fetchDataById(id)
      } else {
        this.bannerInfo = { ...defaultForm }
      }
    },
    // 添加幻灯片
    createBanner() {
      this.saveBtnDisabled = true
      banner.saveBanner(this.bannerInfo).then(res => {
        this.$message({
          type: 'success',
          message: res.message
        })
        this.$router.push({ path: '/edu/banner/list' })
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
      this.bannerInfo.imageUrl = res.data.uploadUrl
    },
  }
}
</script>