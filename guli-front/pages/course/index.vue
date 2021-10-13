<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li>
                  <a title="全部" href="javascript:void(0);"  @click="resetSearchObj">全部</a>
                </li>
                <li v-for="(item, index) in subjectNestedList" :key="index" :class="{active:oneIndex === index}">
                  <a :title="item.title" href="javascript:void(0);" @click="queryParentSubjectAndfetchSubSubjectData(item.id, index)">{{ item.title }}</a>
                </li>
              </ul>
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"></span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li v-for="(item, index) in subSubjectList" :key="index" :class="{active:twoIndex === index}">
                  <a :title="item.title" href="javascript:void(0);" @click="querySubSubject(item.id, index)">{{ item.title }}</a>
                </li>
              </ul>
            </dd>
          </dl>
          <div class="clear"></div>
        </section>
        <div class="js-wrap">
          <!-- <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section> -->
          <section class="fl">
            <ol class="js-tap clearfix">
              <li :class="{'current bg-orange':defaultSort!=''}">
                <a title="默认" href="javascript:void(0);" @click="searchDefault()">默认
                </a>
              </li>
              <li :class="{'current bg-orange':buyCountSort!=''}">
                <a title="销量" href="javascript:void(0);" @click="searchBuyCount()">销量
                <span :class="{hide:buyCountSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-orange':gmtCreateSort!=''}">
                <a title="最新" href="javascript:void(0);" @click="searchGmtCreate()">最新
                <span :class="{hide:gmtCreateSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-orange':priceSort!=''}">
                <a title="价格" href="javascript:void(0);" @click="searchPrice()">价格&nbsp;
                  <span :class="{hide:priceSort==''}">↓</span>
                </a>
              </li>
            </ol>
          </section>
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="data.total === 0">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->
          <article class="comm-course-list">
            <ul class="of" id="bna">
              <li v-for="item in data.items" :key="item.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img :src="item.cover" class="img-responsive" :alt="item.title">
                    <div class="cc-mask">
                      <a :href="'/course/' + item.id" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a :href="'/course/' + item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span class="fr jgTag bg-green" v-if="Number(item.price) === 0">
                      <i class="c-fff fsize12 f-fA">免费</i>
                    </span>
                    <span class="fr jgTag bg-red" v-else>
                      <i class="fsize12 f-fA">￥ {{ item.price }}</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">{{ item.viewCount }} 人浏览</i>
                      |
                      <i class="c-999 f-fA">{{ item.buyCount }} 人购买</i>
                    </span>
                  </section>
                </div>
              </li>
            </ul>
            <div class="clear"></div>
          </article>
        </div>
        <!-- 公共分页 开始 -->
        <div>
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
        </div>
        <!-- 公共分页 结束 -->
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>
<script>
import courseApi from '@/api/course'

export default {
  data() {
    return {
      data:{},
      subjectNestedList: [], // 一级分类列表
      subSubjectList: [], // 二级分类列表
      searchObj: {}, // 查询表单对象
      oneIndex:-1,
      twoIndex:-1,
      defaultSort: 'active',
      buyCountSort:"",
      gmtCreateSort:"",
      priceSort:""
    }
  },
  created() {
    this.fetchCourseData()
    this.fetchSubjectData()
  },
  methods: {
    // 查询课程列表
    fetchCourseData() {
      courseApi.getPageList(1, 8, this.searchObj).then(res => {
        this.data = res.data
      })
    },
    // 查询分类列表
    fetchSubjectData() {
      courseApi.getNestedTreeList().then(res => {
        this.subjectNestedList = res.data.items
      })
    },
    // 根据一级分类查询课程并获取对应的二级分类
    queryParentSubjectAndfetchSubSubjectData(id, index) {
      this.oneIndex = index
      // 清空二级分类的搜索条件和二级分类列表
      this.twoIndex = -1
      this.searchObj.subjectId = ''
      this.subSubjectList = []
      // 把当前的一级分类ID作为查询条件
      this.searchObj.subjectParentId = id
      // 查询课程
      this.gotoPage(1)
      // 获取对应的二级分类
      for (let i = 0; i < this.subjectNestedList.length; i++) {
        if (id === this.subjectNestedList[i].id) {
          this.subSubjectList = this.subjectNestedList[i].children
        }
      }
    },
    // 根据二级分类查询课程
    querySubSubject(id, index) {
      this.twoIndex = index
      this.searchObj.subjectId = id
      this.gotoPage(1)
    },
    // 分页跳转
    gotoPage(page) {
      courseApi.getPageList(page, 8, this.searchObj).then(res => {
        this.data = res.data
      })
    },
    // 根据销量排序
    searchBuyCount() {
      this.defaultSort = ''
      this.buyCountSort = 'active'
      this.gmtCreateSort = ''
      this.priceSort = ''
      this.searchObj.buyCountSort = this.buyCountSort
      this.searchObj.gmtCreateSort = this.gmtCreateSort
      this.searchObj.priceSort = this.priceSort
      this.gotoPage(1)
    },
    // 根据发布时间排序
    searchGmtCreate() {
      this.defaultSort = ''
      this.buyCountSort = ''
      this.gmtCreateSort = 'active'
      this.priceSort = ''
      this.searchObj.buyCountSort = this.buyCountSort
      this.searchObj.gmtCreateSort = this.gmtCreateSort
      this.searchObj.priceSort = this.priceSort
      this.gotoPage(1)
    },
    // 根据价格排序
    searchPrice() {
      this.defaultSort = ''
      this.buyCountSort = ''
      this.gmtCreateSort = ''
      this.priceSort = 'active'
      this.searchObj.buyCountSort = this.buyCountSort
      this.searchObj.gmtCreateSort = this.gmtCreateSort
      this.searchObj.priceSort = this.priceSort
      this.gotoPage(1)
    },
    // 默认排序
    searchDefault() {
      this.defaultSort = 'active'
      this.buyCountSort = ''
      this.gmtCreateSort = ''
      this.priceSort = ''
      this.searchObj.buyCountSort = this.buyCountSort
      this.searchObj.gmtCreateSort = this.gmtCreateSort
      this.searchObj.priceSort = this.priceSort
      this.gotoPage(1)
    },
    // 重置查询条件,查所有
    resetSearchObj() {
      this.searchObj = {}
      this.oneIndex = -1
      this.twoIndex = -1
      this.gotoPage(1)
    }
  },
}
</script>

<style scoped>
  .active {
    background: #bdbdbd;
  }
  .hide {
    display: none;
  }
  .show {
    display: block;
  }
</style>