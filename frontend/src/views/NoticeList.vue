<template>
  <div>
    <h2 style="margin-bottom: 16px; color: #303133;">公告管理</h2>
    <el-card style="margin-bottom: 16px;">
      <el-form :inline="true" :model="query">
        <el-form-item label="标题">
          <el-input v-model="query.title" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="openAdd">发布公告</el-button>
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <div class="action-btns">
              <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
            </div>
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="公告标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input type="textarea" v-model="form.content" :rows="6" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
const API = '/api'

export default {
  name: 'NoticeList',
  data() {
    return {
      query: { page: 1, pageSize: 10, title: '' },
      tableData: [],
      total: 0,
      loading: false,
      selectedIds: [],
      dialogVisible: false,
      dialogTitle: '发布公告',
      form: { id: null, title: '', content: '' }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await axios.get(`${API}/notices`, { params: this.query })
        if (res.data.code === 1) {
          this.tableData = res.data.data.rows
          this.total = res.data.data.total
        }
      } finally {
        this.loading = false
      }
    },
    resetQuery() {
      this.query = { page: 1, pageSize: 10, title: '' }
      this.loadData()
    },
    handleSelectionChange(val) {
      this.selectedIds = val.map(v => v.id)
    },
    openAdd() {
      this.form = { id: null, title: '', content: '' }
      this.dialogTitle = '发布公告'
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑公告'
      this.dialogVisible = true
    },
    async handleSave() {
      if (!this.form.title || !this.form.content) {
        this.$message.warning('请填写完整信息')
        return
      }
      if (this.form.id) {
        await axios.put(`${API}/notices`, this.form)
        this.$message.success('修改成功')
      } else {
        await axios.post(`${API}/notices`, this.form)
        this.$message.success('发布成功')
      }
      this.dialogVisible = false
      this.loadData()
    },
    async handleDelete(id) {
      await this.$confirm('确定删除该公告吗？', '提示', { type: 'warning' })
      await axios.delete(`${API}/notices`, { params: { ids: [id].join(',') } })
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleBatchDelete() {
      await this.$confirm(`确定删除选中的 ${this.selectedIds.length} 条公告吗？`, '提示', { type: 'warning' })
      await axios.delete(`${API}/notices`, { params: { ids: this.selectedIds.join(',') } })
      this.$message.success('删除成功')
      this.loadData()
    }
  }
}
</script>

<style scoped>
.action-btns {
  display: flex;
  gap: 6px;
}
</style>
