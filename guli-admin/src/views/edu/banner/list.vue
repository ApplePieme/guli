<template>
  <div class="app-container">
    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      row-class-name="myClassList">
      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="幻灯片信息" width="470" align="center">
        <template slot-scope="scope">
          <div class="info">
            <div class="pic">
              <img :src="scope.row.imageUrl" alt="scope.row.title" width="150px">
            </div>
            <div class="title">
              <span>{{ scope.row.title }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="linkUrl" label="链接地址" width="100" align="center" >
        <template slot-scope="scope">
          <span v-if="scope.row.linkUrl === '/course'">课程</span>
          <span v-if="scope.row.linkUrl === '/teacher'">名师</span>
          <!-- {{ scope.row.linkUrl === '/course' ? '课程' : '名师' }} -->
        </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtCreate }}
        </template>
      </el-table-column>
      <el-table-column label="最后更新时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtModified }}
        </template>
      </el-table-column>

      <el-table-column prop="sort" label="排序" width="60" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/edu/banner/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑</el-button>
          </router-link>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeBannerById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />
  </div>
</template>

<script>
import banner from '@/api/edu/banner'

export default {
  data() {
    return {
      listLoading: true, // 是否显示loading信息
      list: null, // 数据列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 5, // 每页记录数
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取 banner 列表
    fetchData(page = 1) {
      this.page = page
      this.listLoading = true
      banner.queryPage(this.page, this.limit).then(res => {
        this.total = res.data.total
        this.list = res.data.items
        this.listLoading = false
      })
    },
    // 根据 ID 删除 banner
    removeBannerById(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return banner.removeBannerById(id)
      }).then(() => {
        this.fetchData()
        this.$message({
            type: 'success',
            message: '删除成功'
        })
      }).catch((response) => { // 失败
        if (response === 'cancel') {
          this.$message({
              type: 'info',
              message: '已取消删除'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.myClassList .info {
    width: 450px;
    overflow: hidden;
}
.myClassList .info .pic {
    width: 150px;
    height: 90px;
    overflow: hidden;
    float: left;
}
.myClassList .info .pic a {
    display: block;
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
}
.myClassList .info .pic img {
    display: block;
    width: 100%;
}
.myClassList td .info .title {
    width: 280px;
    float: right;
    height: 90px;
}
.myClassList td .info .title span {
    display: block;
    height: 90px;
    line-height: 90px;
    overflow: hidden;
    color: #00baf2;
}
</style>