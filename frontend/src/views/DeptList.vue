<template>
  <div>
    <h2 style="margin-bottom: 16px; color: #303133;">部门管理</h2>
    <el-card>
      <div style="margin-bottom: 16px; display: flex; justify-content: space-between;">
        <span></span>
        <el-button type="primary" @click="openAdd">新增部门</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="修改时间" width="180" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="部门名称">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
const API = '/api'

export default {
  name: 'DeptList',
  data() {
    return {
      tableData: [],
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增部门',
      form: { id: null, name: '' }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await axios.get(`${API}/depts`)
        if (res.data.code === 1) this.tableData = res.data.data
      } finally {
        this.loading = false
      }
    },
    openAdd() {
      this.form = { id: null, name: '' }
      this.dialogTitle = '新增部门'
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑部门'
      this.dialogVisible = true
    },
    async handleSave() {
      if (!this.form.name) {
        this.$message.warning('请输入部门名称')
        return
      }
      let res
      if (this.form.id) {
        res = await axios.put(`${API}/depts`, this.form)
      } else {
        res = await axios.post(`${API}/depts`, this.form)
      }
      if (res.data.code === 1) {
        this.$message.success(this.form.id ? '修改成功' : '新增成功')
        this.dialogVisible = false
        this.loadData()
      } else {
        this.$message.error(res.data.msg)
      }
    },
    async handleDelete(id) {
      await this.$confirm('确定删除该部门吗？', '提示', { type: 'warning' })
      const res = await axios.delete(`${API}/depts`, { params: { id } })
      if (res.data.code === 1) {
        this.$message.success('删除成功')
        this.loadData()
      } else {
        this.$message.error(res.data.msg)
      }
    }
  }
}
</script>
