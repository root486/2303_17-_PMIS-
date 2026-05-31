<template>
  <el-container style="height: 100vh">
    <!-- 顶部标题栏 -->
    <el-header class="header">
      <span class="title">员工管理系统</span>
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
  computed: {
    activeMenu() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.header {
  background-color: #409eff;
  display: flex;
  align-items: center;
  padding: 0 24px;
  height: 60px;
}
.title {
  color: #fff;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 2px;
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
