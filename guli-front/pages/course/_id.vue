<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程详情 开始 -->
    <section class="container">
      <section class="path-wrap txtOf hLh30">
        <a href="/" title class="c-999 fsize14">首页</a>
        \
        <a href="/course" title class="c-999 fsize14">课程列表</a>
        \
        <span class="c-333 fsize14">{{ course.title }}</span>
      </section>
      <div>
        <article class="c-v-pic-wrap" style="height: 357px;">
          <section class="p-h-video-box" id="videoPlay">
            <img :src="course.cover" :alt="course.title" class="dis c-v-pic">
          </section>
        </article>
        <aside class="c-attr-wrap">
          <section class="ml20 mr15">
            <h2 class="hLh30 txtOf mt15">
              <span class="c-fff fsize24">{{ course.title }}</span>
            </h2>
            <section class="c-attr-jg">
              <span class="c-fff">价格：</span>
              <b class="c-yellow" style="font-size:24px;">￥ {{ course.price }}</b>
            </section>
            <section class="c-attr-mt c-attr-undis">
              <span class="c-fff fsize14">主讲： {{ course.teacherName }}&nbsp;&nbsp;&nbsp;</span>
            </section>
            <section class="c-attr-mt of">
              <span class="ml10 vam">
                <em class="icon18 scIcon"></em>
                <a class="c-fff vam" title="收藏" href="javascript:void(0);" >收藏</a>
              </span>
            </section>
            <section class="c-attr-mt">
              <a href="javascript:void(0);" title="立即观看" class="comm-btn c-btn-3" v-if="Number(course.price) === 0 || isBuy">立即观看</a>
              <a href="javascript:void(0);" title="立即购买" class="comm-btn c-btn-3" v-else @click="createOrder(course.id)">立即购买</a>
            </section>
          </section>
        </aside>
        <aside class="thr-attr-box">
          <ol class="thr-attr-ol">
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">购买数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{ course.buyCount }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">课时数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{ course.lessonNum }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">浏览数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{ course.viewCount }}</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div class="clear"></div>
      </div>
      <!-- /课程封面介绍 -->
      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div class="i-box">
              <div>
                <section id="c-i-tabTitle" class="c-infor-tabTitle c-tab-title">
                  <a name="c-i" class="current" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10 pt20">
                <div>
                  <h6 class="c-i-content c-infor-title">
                    <span>课程介绍</span>
                  </h6>
                  <div class="course-txt-body-wrap">
                    <section class="course-txt-body">
                      <p v-html="course.description">{{ course.description }}</p>
                    </section>
                  </div>
                </div>
                <!-- /课程介绍 -->
                <div class="mt50">
                  <h6 class="c-g-content c-infor-title">
                    <span>课程大纲</span>
                  </h6>
                  <section class="mt20">
                    <div class="lh-menu-wrap">
                      <menu id="lh-menu" class="lh-menu mt10 mr10">
                        <ul>
                          <!-- 文件目录 -->
                          <li class="lh-menu-stair" v-for="chapter in chapterList" :key="chapter.id">
                            <a href="javascript: void(0);" :title="chapter.title" class="current-1">
                              <em class="lh-menu-i-1 icon18 mr10"></em>{{ chapter.title }}
                            </a>
                            <ol class="lh-menu-ol" style="display: block;">
                              <li class="lh-menu-second ml30" v-for="video in chapter.children" :key="video.id">
                                <a :href="'/video/' + video.videoSourceId" :title="video.title" target="_blank" v-if="video.videoSourceId !== ''">
                                  <span class="fr" v-if="video.isFree === 1">
                                    <i class="free-icon vam mr10">免费试听</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                </a>
                                <a href="javascript: void(0);" title="该章节还没有视频哦" v-else>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                </a>
                              </li>
                            </ol>
                          </li>
                        </ul>
                      </menu>
                    </div>
                  </section>
                </div>
                <!-- /课程大纲 -->

                <!-- 评论区域 开始 -->
                <div class="mt50 commentHtml">
                  <div>
                    <h6 class="c-c-content c-infor-title" id="i-art-comment">
                      <span class="commentTitle">课程评论</span>
                    </h6>
                    <section class="lh-bj-list pr mt20 replyhtml">
                      <ul>
                        <li class="unBr">
                          <aside class="noter-pic">
                            <img width="50" height="50" class="picImg" :src="avatar">
                            </aside>
                          <div class="of">
                            <section class="n-reply-wrap">
                              <fieldset>
                                <textarea name="" v-model="comment.content" placeholder="输入您要评论的文字" id="commentContent"></textarea>
                              </fieldset>
                              <p class="of mt5 tar pl10 pr10">
                                <span class="fl "><tt class="c-red commentContentmeg" style="display: none;"></tt></span>
                                <a @click="addComment()" class="lh-reply-btn" style="cursor:pointer;">发表评论</a>
                              </p>
                            </section>
                          </div>
                        </li>
                      </ul>
                    </section>
                    <section class="">
                      <section class="question-list lh-bj-list pr">
                        <ul class="pr10">
                          <li v-for="(comment,index) in data.items" v-bind:key="index">
                              <aside class="noter-pic">
                                <img width="50" height="50" class="picImg" :src="comment.avatar">
                                </aside>
                              <div class="of">
                                <span class="fl"> 
                                <font class="fsize12 c-blue"> 
                                  {{comment.nickname}}</font>
                                </span>
                              </div>
                              <div class="noter-txt mt5">
                                <p>{{comment.content}}</p>
                              </div>
                              <div class="of mt5">
                                <span class="fr"><font class="fsize12 c-999 ml5">{{comment.gmtCreate}}</font></span>
                              </div>
                            </li>
                          </ul>
                      </section>
                    </section>
                    
                    <!-- 公共分页 开始 -->
                    <div class="paging">
                      <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
                      <a
                      :class="{undisable: !data.hasPrevious}"
                      href="javascript:void(0);"
                      title="首页"
                      @click.prevent="gotoPage(1)">首</a>
                      <a
                      :class="{undisable: !data.hasPrevious}"
                      href="javascript:void(0);"
                      title="前一页"
                      @click.prevent="gotoPage(data.current-1)">&lt;</a>
                      <a
                      v-for="page in data.pages"
                      :key="page"
                      :class="{current: data.current == page, undisable: data.current == page}"
                      :title="'第'+page+'页'"
                      href="javascript:void(0);"
                      @click.prevent="gotoPage(page)">{{ page }}</a>
                      <a
                      :class="{undisable: !data.hasNext}"
                      href="javascript:void(0);"
                      title="后一页"
                      @click.prevent="gotoPage(data.current+1)">&gt;</a>
                      <a
                      :class="{undisable: !data.hasNext}"
                      href="javascript:void(0);"
                      title="末页"
                      @click.prevent="gotoPage(data.pages)">末</a>
                      <div class="clear"/>
                    </div>
                    <!-- 公共分页 结束 -->
                  </div>
                </div>
                <!-- /评论区域 结束 -->
              </article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">
            <div>
              <section class="c-infor-tabTitle c-tab-title">
                <a title href="javascript:void(0);">主讲讲师</a>
              </section>
              <section class="stud-act-list">
                <ul style="height: auto;">
                  <li>
                    <div class="u-face">
                      <a :href="'/teacher/' + course.teacherId">
                        <img :src="course.avatar" width="50" height="50" alt>
                      </a>
                    </div>
                    <section class="hLh30 txtOf">
                      <a class="c-333 fsize16 fl" :href="'/teacher/' + course.teacherId">{{ course.teacherName }}</a>
                    </section>
                    <section class="hLh20 txtOf">
                      <span class="c-999">{{ course.career }}</span>
                    </section>
                  </li>
                </ul>
              </section>
            </div>
          </div>
        </aside>
        <div class="clear"></div>
      </div>
    </section>
    <!-- /课程详情 结束 -->
  </div>
</template>

<script>
import courseApi from '@/api/course'
import commentApi from '@/api/comment'
import MemberApi from '@/api/login'
import orderApi from '@/api/order'
import cookie from 'js-cookie'

export default {
  data() {
    return {
      course: {},
      chapterList: [],
      isBuy: false,
      comment: {
        content: '',
        courseId: '',
        teacherId: ''
      },
      data: {},
      avatar: require('@/assets/img/avatar-boy.gif')
    }
  },
  created() {
    this.fetchCourseDetails()
    this.gotoPage(1)
    this.getMemberAvatar()
  },
  methods: {
    fetchCourseDetails() {
      courseApi.getCourseDetailsById(this.$route.params.id).then(res => {
        this.isBuy = res.data.isBuy
        this.course = res.data.course
        this.chapterList = res.data.chapterList
      })
    },
    gotoPage(page) {
      commentApi.listPageComments(this.course.id, page, 5).then(res => {
        this.data = res.data
      })
    },
    addComment() {
      if(this.comment.content === '') {
        this.$message({
          type: 'error',
          message: '评论内容不能为空'
        })
      } else {
        this.comment.courseId = this.course.id
        this.comment.teacherId = this.course.teacherId
        commentApi.saveComment(this.comment).then(() => {
          this.comment.content = ''
          this.gotoPage(1)
        })
      }
    },
    getMemberAvatar() {
      if(cookie.get('token')) {
        MemberApi.getUserInfoByToken().then(res => {
          this.avatar = res.data.item.avatar
        })
      } else {
        this.avatar = require('@/assets/img/avatar-boy.gif')
      }
    },
    createOrder(courseId) {
      orderApi.createOrder(courseId).then(res => {
        this.$router.push({ path: '/order/' + res.data.orderNo })
      })
    }
  }
}
</script>

<style scope>
.lh-reply-btn {
    width: 60px;
}
</style>
