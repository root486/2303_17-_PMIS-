<template>
  <div>
    <h2 style="margin-bottom: 16px; color: #303133;">员工管理</h2>
    <el-card style="margin-bottom: 16px;">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="query.gender" placeholder="全部" clearable style="width: 100px;">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker v-model="query.begin" type="date" placeholder="开始" value-format="YYYY-MM-DD" style="width: 140px;" />
          <span style="margin: 0 8px;">至</span>
          <el-date-picker v-model="query.end" type="date" placeholder="结束" value-format="YYYY-MM-DD" style="width: 140px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="openAdd">新增员工</el-button>
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column label="性别" width="70">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="salary" label="薪资" width="100" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="用户名">
              <el-input v-model="form.username" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" placeholder="请选择" style="width: 100%;">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select v-model="form.job" placeholder="请选择" style="width: 100%;">
                <el-option label="班主任" :value="1" />
                <el-option label="讲师" :value="2" />
                <el-option label="学工主管" :value="3" />
                <el-option label="教研主管" :value="4" />
                <el-option label="咨询师" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资">
              <el-input-number v-model="form.salary" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="form.entryDate" type="date" value-format="YYYY-MM-DD" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门">
              <el-select v-model="form.deptId" placeholder="请选择" style="width: 100%;">
                <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item v-if="!form.id" label="密码">
          <el-input v-model="form.password" placeholder="默认123456" />
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
  name: 'EmpList',
  data() {
    return {
      query: { page: 1, pageSize: 10, name: '', gender: null, begin: null, end: null },
      tableData: [],
      total: 0,
      loading: false,
      selectedIds: [],
      deptList: [],
      dialogVisible: false,
      dialogTitle: '新增员工',
      form: { id: null, username: '', name: '', password: '123456', gender: null, phone: '', job: null, salary: null, entryDate: null, deptId: null }
    }
  },
  mounted() {
    this.loadData()
    this.loadDepts()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await axios.get(`${API}/emps`, { params: this.query })
        if (res.data.code === 1) {
          this.tableData = res.data.data.rows
          this.total = res.data.data.total
        }
      } finally {
        this.loading = false
      }
    },
    async loadDepts() {
      const res = await axios.get(`${API}/depts`)
      if (res.data.code === 1) this.deptList = res.data.data
    },
    resetQuery() {
      this.query = { page: 1, pageSize: 10, name: '', gender: null, begin: null, end: null }
      this.loadData()
    },
    handleSelectionChange(val) {
      this.selectedIds = val.map(v => v.id)
    },
    openAdd() {
      this.form = { id: null, username: '', name: '', password: '123456', gender: null, phone: '', job: null, salary: null, entryDate: null, deptId: null }
      this.dialogTitle = '新增员工'
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑员工'
      this.dialogVisible = true
    },
    async handleSave() {
      if (!this.form.username || !this.form.name) {
        this.$message.warning('请填写必填项')
        return
      }
      if (this.form.id) {
        await axios.put(`${API}/emps`, this.form)
        this.$message.success('修改成功')
      } else {
        await axios.post(`${API}/emps`, this.form)
        this.$message.success('新增成功')
      }
      this.dialogVisible = false
      this.loadData()
    },
    async handleDelete(id) {
      await this.$confirm('确定删除该员工吗？', '提示', { type: 'warning' })
      await axios.delete(`${API}/emps`, { params: { ids: [id].join(',') } })
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleBatchDelete() {
      await this.$confirm(`确定删除选中的 ${this.selectedIds.length} 名员工吗？`, '提示', { type: 'warning' })
      await axios.delete(`${API}/emps`, { params: { ids: this.selectedIds.join(',') } })
      this.$message.success('删除成功')
      this.loadData()
    }
  }
}
</script>
