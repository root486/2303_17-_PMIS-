<template>
  <div>
    <h2 style="margin-bottom: 16px; color: #303133;">请假管理</h2>
    <el-card style="margin-bottom: 16px;">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名" v-if="isManager">
          <el-input v-model="query.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="请假类型">
          <el-select v-model="query.leaveType" placeholder="全部" clearable style="width: 110px;">
            <el-option label="事假" :value="1" />
            <el-option label="病假" :value="2" />
            <el-option label="年假" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 110px;">
            <el-option label="待审批" :value="1" />
            <el-option label="已批准" :value="2" />
            <el-option label="已拒绝" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="openAdd">申请请假</el-button>
        <el-button type="danger" v-if="isManager" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column v-if="isManager" type="selection" width="50" />
        <el-table-column prop="empId" label="员工ID" width="80" />
        <el-table-column prop="empName" label="员工姓名" width="100" />
        <el-table-column label="请假类型" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.leaveType === 1" type="info">事假</el-tag>
            <el-tag v-else-if="row.leaveType === 2" type="warning">病假</el-tag>
            <el-tag v-else type="success">年假</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="beginDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="reason" label="原因" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="info">待审批</el-tag>
            <el-tag v-else-if="row.status === 2" type="success">已批准</el-tag>
            <el-tag v-else type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="isManager ? 200 : 80" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 1 && isManager">
              <el-button type="success" size="small" @click="handleApprove(row.id, 2)">批准</el-button>
              <el-button type="danger" size="small" @click="handleApprove(row.id, 3)">拒绝</el-button>
            </template>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 16px; justify-content: flex-end;"
        v-model:current-page="query.page"
        v-model:page-size="query.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @change="loadData"
      />
    </el-card>

    <el-dialog title="申请请假" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item v-if="isManager" label="员工ID">
          <el-input-number v-model="form.empId" :min="1" placeholder="请输入员工ID" style="width: 100%;" @change="onEmpIdChange" />
        </el-form-item>
        <el-form-item v-if="empName" label="员工姓名">
          <el-tag type="success">{{ empName }}</el-tag>
        </el-form-item>
        <el-form-item label="请假类型">
          <el-select v-model="form.leaveType" placeholder="请选择" style="width: 100%;">
            <el-option label="事假" :value="1" />
            <el-option label="病假" :value="2" />
            <el-option label="年假" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.beginDate" type="date" value-format="YYYY-MM-DD" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="请假原因">
          <el-input type="textarea" v-model="form.reason" :rows="3" placeholder="请输入原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
const API = '/api'

export default {
  name: 'LeaveList',
  data() {
    return {
      query: { page: 1, pageSize: 10, name: '', empId: null, leaveType: null, status: null },
      tableData: [],
      total: 0,
      loading: false,
      selectedIds: [],
      dialogVisible: false,
      form: { empId: null, leaveType: null, beginDate: null, endDate: null, reason: '' },
      empName: ''
    }
  },
  computed: {
    currentUser() {
      return JSON.parse(localStorage.getItem('user') || '{}')
    },
    isManager() {
      return this.currentUser.role === 1
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.query }
        if (!this.isManager) {
          params.empId = this.currentUser.id
        }
        const res = await axios.get(`${API}/leaves`, { params })
        if (res.data.code === 1) {
          this.tableData = res.data.data.rows
          this.total = res.data.data.total
        }
      } catch (e) {
        console.error('加载请假数据失败', e)
      } finally {
        this.loading = false
      }
    },
    resetQuery() {
      this.query = { page: 1, pageSize: 10, name: '', empId: null, leaveType: null, status: null }
      this.loadData()
    },
    handleSelectionChange(val) {
      this.selectedIds = val.map(v => v.id)
    },
    openAdd() {
      this.form = { empId: null, leaveType: null, beginDate: null, endDate: null, reason: '' }
      this.empName = ''
      if (!this.isManager) {
        this.form.empId = this.currentUser.id
        this.empName = this.currentUser.name
      }
      this.dialogVisible = true
    },
    async onEmpIdChange(val) {
      this.empName = ''
      if (!val) return
      try {
        const res = await axios.get(`${API}/emps/${val}`)
        if (res.data.code === 1 && res.data.data) {
          this.empName = res.data.data.name
        } else {
          this.$message.warning('未找到该员工')
        }
      } catch (e) {
        this.$message.warning('未找到该员工')
      }
    },
    async handleSave() {
      if (!this.form.empId || !this.empName) {
        this.$message.warning('请填写正确的员工ID')
        return
      }
      if (!this.form.leaveType || !this.form.beginDate || !this.form.endDate) {
        this.$message.warning('请填写请假类型和日期')
        return
      }
      try {
        const res = await axios.post(`${API}/leaves`, this.form)
        if (res.data.code === 1) {
          this.$message.success('提交成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.data.msg || '提交失败')
        }
      } catch (e) {
        this.$message.error('提交失败，请检查员工ID是否正确')
      }
    },
    async handleApprove(id, status) {
      const text = status === 2 ? '批准' : '拒绝'
      await this.$confirm(`确定${text}该请假申请吗？`, '提示', { type: 'warning' })
      await axios.put(`${API}/leaves/approve/${id}/${status}`)
      this.$message.success(`${text}成功`)
      this.loadData()
    },
    async handleDelete(id) {
      await this.$confirm('确定删除该请假记录吗？', '提示', { type: 'warning' })
      await axios.delete(`${API}/leaves`, { params: { ids: [id].join(',') } })
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleBatchDelete() {
      await this.$confirm(`确定删除选中的 ${this.selectedIds.length} 条记录吗？`, '提示', { type: 'warning' })
      await axios.delete(`${API}/leaves`, { params: { ids: this.selectedIds.join(',') } })
      this.$message.success('删除成功')
      this.loadData()
    }
  }
}
</script>
