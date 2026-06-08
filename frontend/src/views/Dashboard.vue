<template>
  <div>
    <h2 style="margin-bottom: 24px; color: #303133;">工作台</h2>

    <!-- 统计卡片 -->
    <el-row :gutter="24" style="margin-bottom: 24px;">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" :body-style="{ padding: '20px' }">
          <div class="stat-card">
            <div class="stat-icon" :style="{ background: card.color }">
              <el-icon :size="28"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-value">{{ card.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 近期公告 -->
    <el-row :gutter="24" style="margin-bottom: 24px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>📢 近期公告</span>
              <el-button type="primary" link @click="$router.push('/notices')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentNotices" stripe size="small" v-loading="noticeLoading" empty-text="暂无公告">
            <el-table-column prop="title" label="标题" min-width="400" show-overflow-tooltip />
            <el-table-column prop="createTime" label="发布时间" width="180" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 近期请假 -->
    <el-row :gutter="24">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>📋 近期请假</span>
              <el-button type="primary" link @click="$router.push('/leaves')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentLeaves" stripe size="small" v-loading="leaveLoading" empty-text="暂无请假记录">
            <el-table-column prop="empName" label="员工" width="100" />
            <el-table-column label="类型" width="90">
              <template #default="{ row }">
                <el-tag :type="leaveTypeTag(row.leaveType)" size="small">{{ leaveTypeText(row.leaveType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="beginDate" label="开始日期" width="120" />
            <el-table-column prop="endDate" label="结束日期" width="120" />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="statusTag(row.status)" size="small">{{ statusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
import { User, OfficeBuilding, Calendar, Bell } from '@element-plus/icons-vue'

const API = '/api'

export default {
  name: 'Dashboard',
  components: { User, OfficeBuilding, Calendar, Bell },
  data() {
    return {
      statCards: [
        { label: '员工总数', value: 0, icon: 'User', color: '#409eff' },
        { label: '部门总数', value: 0, icon: 'OfficeBuilding', color: '#67c23a' },
        { label: '待审批请假', value: 0, icon: 'Calendar', color: '#e6a23c' },
        { label: '公告总数', value: 0, icon: 'Bell', color: '#f56c6c' }
      ],
      recentNotices: [],
      recentLeaves: [],
      noticeLoading: false,
      leaveLoading: false
    }
  },
  mounted() {
    this.loadStats()
    this.loadRecentNotices()
    this.loadRecentLeaves()
  },
  methods: {
    async loadStats() {
      try {
        const [empRes, deptRes, leaveRes, noticeRes] = await Promise.all([
          axios.get(`${API}/emps`, { params: { page: 1, pageSize: 1 } }),
          axios.get(`${API}/depts`),
          axios.get(`${API}/leaves`, { params: { status: 1, page: 1, pageSize: 1 } }),
          axios.get(`${API}/notices`, { params: { page: 1, pageSize: 1 } })
        ])
        if (empRes.data.code === 1) this.statCards[0].value = empRes.data.data.total || 0
        if (deptRes.data.code === 1) this.statCards[1].value = (deptRes.data.data || []).length
        if (leaveRes.data.code === 1) this.statCards[2].value = leaveRes.data.data.total || 0
        if (noticeRes.data.code === 1) this.statCards[3].value = noticeRes.data.data.total || 0
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    async loadRecentNotices() {
      this.noticeLoading = true
      try {
        const res = await axios.get(`${API}/notices`, { params: { page: 1, pageSize: 5 } })
        if (res.data.code === 1) this.recentNotices = res.data.data.rows || []
      } finally {
        this.noticeLoading = false
      }
    },
    async loadRecentLeaves() {
      this.leaveLoading = true
      try {
        const res = await axios.get(`${API}/leaves`, { params: { page: 1, pageSize: 5 } })
        if (res.data.code === 1) this.recentLeaves = res.data.data.rows || []
      } finally {
        this.leaveLoading = false
      }
    },
    leaveTypeText(type) {
      return { 1: '事假', 2: '病假', 3: '年假' }[type] || '未知'
    },
    leaveTypeTag(type) {
      return { 1: 'info', 2: 'warning', 3: 'success' }[type] || 'info'
    },
    statusText(status) {
      return { 1: '待审批', 2: '已批准', 3: '已拒绝' }[status] || '未知'
    },
    statusTag(status) {
      return { 1: 'info', 2: 'success', 3: 'danger' }[status] || 'info'
    }
  }
}
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 6px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
</style>
