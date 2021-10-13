import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRoutes = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  // 首页
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '谷粒学院后台首页', icon: 'dashboard' }
    }]
  }
]

/**
 * 动态路由
 */
// export const asyncRoutes = [
//   // 讲师管理
//   // {
//   //   path: '/teacher',
//   //   component: Layout,
//   //   redirect: '/teacher/list',
//   //   name: 'Teacher',
//   //   meta: { title: '讲师管理', icon: 'peoples' },
//   //   children: [
//   //     {
//   //       path: 'list',
//   //       name: 'TeacherList',
//   //       component: () => import('@/views/edu/teacher/list'),
//   //       meta: { title: '讲师列表' }
//   //     },
//   //     {
//   //       path: 'create',
//   //       name: 'TeacherCreate',  
//   //       component: () => import('@/views/edu/teacher/form'),
//   //       meta: { title: '添加讲师' }
//   //     },
//   //     {
//   //       path: 'edit/:id',   
//   //       name: 'TeacherEdit',
//   //       component: () => import('@/views/edu/teacher/form'),
//   //       meta: { title: '编辑讲师', noCache: true },
//   //       hidden: true
//   //     }
//   //   ]
//   // },

//   // // 课程分类管理
//   // {
//   //   path: '/subject',
//   //   component: Layout,
//   //   redirect: '/subject/list',
//   //   name: 'Subject',
//   //   meta: { title: '课程分类管理', icon: 'nested' },
//   //   children: [
//   //     {
//   //       path: 'list',
//   //       name: 'SubjectList',
//   //       component: () => import('@/views/edu/subject/list'),
//   //       meta: { title: '课程分类列表' }
//   //     },
//   //     {
//   //       path: 'import',
//   //       name: 'SubjectImport',  
//   //       component: () => import('@/views/edu/subject/import'),
//   //       meta: { title: '添加课程分类' }
//   //     }
//   //   ]
//   // },

//   // // 课程管理
//   // {
//   //   path: '/course',
//   //   component: Layout,
//   //   redirect: '/course/list',
//   //   name: 'Course',
//   //   meta: { title: '课程管理', icon: 'form' },
//   //   children: [
//   //     {
//   //       path: 'list',
//   //       name: 'CourseList',
//   //       component: () => import('@/views/edu/course/list'),
//   //       meta: { title: '课程列表' }
//   //     },
//   //     {
//   //       path: 'info',
//   //       name: 'CourseInfo',  
//   //       component: () => import('@/views/edu/course/info'),
//   //       meta: { title: '添加课程' }
//   //     },
//   //     {
//   //       path: 'info/:id',
//   //       name: 'CourseInfoEdit',
//   //       component: () => import('@/views/edu/course/info'),
//   //       meta: { title: '编辑课程基本信息', noCache: true },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'chapter/:id',
//   //       name: 'CourseChapterEdit',
//   //       component: () => import('@/views/edu/course/chapter'),
//   //       meta: { title: '编辑课程大纲', noCache: true },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'publish/:id',
//   //       name: 'CoursePublishEdit',
//   //       component: () => import('@/views/edu/course/publish'),
//   //       meta: { title: '发布课程', noCache: true },
//   //       hidden: true
//   //     }
//   //   ]
//   // },

//   // // 幻灯片管理
//   // {
//   //   path: '/edu/banner',
//   //   component: Layout,
//   //   redirect: '/edu/banner/list',
//   //   name: 'Banner',
//   //   meta: { title: '幻灯片管理', icon: 'nested' },
//   //   children: [
//   //     {
//   //       path: 'list',
//   //       name: 'BannerList',
//   //       component: () => import('@/views/edu/banner/list'),
//   //       meta: { title: '幻灯片列表' }
//   //     },
//   //     {
//   //       path: 'create',
//   //       name: 'BannerCreate',
//   //       component: () => import('@/views/edu/banner/form'),
//   //       meta: { title: '添加幻灯片' }
//   //     },
//   //     {
//   //       path: 'edit/:id',
//   //       name: 'BannerEdit',
//   //       component: () => import('@/views/edu/banner/form'),
//   //       meta: { title: '编辑幻灯片', noCache: true },
//   //       hidden: true
//   //     }
//   //   ]
//   // },

//   // // 统计分析
//   // {
//   //   path: '/statistics',
//   //   component: Layout,
//   //   redirect: '/statistics/create',
//   //   name: 'Statistics',
//   //   meta: { title: '统计分析', icon: 'chart' },
//   //   children: [
//   //     {
//   //       path: 'create',
//   //       name: 'StatisticsCreate',
//   //       component: () => import('@/views/statistics/create'),
//   //       meta: { title: '生成数据' }
//   //     },
//   //     {
//   //       path: 'chart',
//   //       name: 'StatisticsChart',
//   //       component: () => import('@/views/statistics/chart'),
//   //       meta: { title: '图表显示' }
//   //     }
//   //   ]
//   // },

//   // // 权限管理
//   // {
//   //   path: '/acl',
//   //   component: Layout,
//   //   redirect: '/acl/user/list',
//   //   name: 'Acl',
//   //   meta: { title: '权限管理', icon: 'example' },
//   //   children: [
//   //     {
//   //       path: 'user/list',
//   //       name: 'UserList',
//   //       component: () => import('@/views/acl/user/list'),
//   //       meta: { title: '用户管理' }
//   //     },
//   //     {
//   //       path: 'role/list',
//   //       name: 'RoleList',
//   //       component: () => import('@/views/acl/role/list'),
//   //       meta: { title: '角色管理' }
//   //     },
//   //     {
//   //       path: 'role/form',
//   //       name: 'RoleCreate',
//   //       component: () => import('@/views/acl/role/form'),
//   //       meta: { title: '角色添加' },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'role/update/:id',
//   //       name: 'RoleUpdate',
//   //       component: () => import('@/views/acl/role/form'),
//   //       meta: { title: '角色修改' },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'role/distribution/:id',
//   //       name: 'RoleDistribution',
//   //       component: () => import('@/views/acl/role/roleForm'),
//   //       meta: { title: '角色权限' },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'menu/list',
//   //       name: 'MenuList',
//   //       component: () => import('@/views/acl/menu/list'),
//   //       meta: { title: '菜单管理' }
//   //     },
//   //     {
//   //       path: 'user/add',
//   //       name: 'UserCreate',
//   //       component: () => import('@/views/acl/user/form'),
//   //       meta: { title: '用户添加' },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'user/update/:id',
//   //       name: 'UserUpdate',
//   //       component: () => import('@/views/acl/user/form'),
//   //       meta: { title: '用户修改' },
//   //       hidden: true
//   //     },
//   //     {
//   //       path: 'user/role/:id',
//   //       name: 'UserRole',
//   //       component: () => import('@/views/acl/user/roleForm'),
//   //       meta: { title: '用户角色' },
//   //       hidden: true
//   //     }

//   //   ]
//   // },

//   // { path: '*', redirect: '/404', hidden: true }
// ]

// export default new Router({
//   // mode: 'history', //后端支持可开
//   scrollBehavior: () => ({ y: 0 }),
//   routes: constantRouterMap
// })

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export default router