import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import EmployeeStats from '../views/EmployeeStats.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { title: '工作台' }
      },
      {
        path: 'stats',
        name: 'EmployeeStats',
        component: EmployeeStats,
        meta: { title: '员工信息统计' }
      },
      {
        path: 'depts',
        name: 'DeptList',
        component: () => import('../views/DeptList.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'emps',
        name: 'EmpList',
        component: () => import('../views/EmpList.vue'),
        meta: { title: '员工管理' }
      },
      {
        path: 'notices',
        name: 'NoticeList',
        component: () => import('../views/NoticeList.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'leaves',
        name: 'LeaveList',
        component: () => import('../views/LeaveList.vue'),
        meta: { title: '请假管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫: 未登录跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router
