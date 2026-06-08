<template>
  <el-container style="height: 100vh">
    <!-- 顶部标题栏 -->
    <el-header class="header">
      <span class="title">员工管理系统</span>
      <div class="header-right">
        <span class="user-info">
          <el-icon><UserFilled /></el-icon>
          {{ userName }}
        </span>
        <el-button type="danger" size="small" plain @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>
    <el-container>
      <!-- 左侧导航栏 -->
      <el-aside width="220px" class="aside">
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          <el-menu-item index="/stats">
            <el-icon><DataAnalysis /></el-icon>
            <span>员工信息统计</span>
          </el-menu-item>
          <el-menu-item index="/depts">
            <el-icon><OfficeBuilding /></el-icon>
            <span>部门管理</span>
          </el-menu-item>
          <el-menu-item index="/emps">
            <el-icon><User /></el-icon>
            <span>员工管理</span>
          </el-menu-item>
          <el-menu-item index="/notices">
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </el-menu-item>
          <el-menu-item index="/leaves">
            <el-icon><Calendar /></el-icon>
            <span>请假管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <!-- 右侧内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  data() {
    return {
      userName: ''
    }
  },
  created() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    this.userName = user.name || '管理员'
  },
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleLogout() {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      this.$message.success('已退出登录')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.header {
  background-color: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px;
}
.title {
  color: #fff;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 2px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-info {
  color: #fff;
  font-size: 15px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.aside {
  background-color: #304156;
  overflow-y: auto;
}
.main {
  background-color: #f0f2f5;
  padding: 24px;
}
</style>
