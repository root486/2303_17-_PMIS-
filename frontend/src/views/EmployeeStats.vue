<template>
  <div>
    <h2 style="margin-bottom: 24px; color: #303133;">员工信息统计</h2>
    <el-row :gutter="24">
      <!-- 职位统计柱状图 -->
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>职位人数统计</span>
            </div>
          </template>
          <div ref="jobChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <!-- 性别统计环形图 -->
      <el-col :span="10">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>员工性别占比</span>
            </div>
          </template>
          <div ref="genderChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import axios from 'axios'

const API_BASE = '/api'

export default {
  name: 'EmployeeStats',
  data() {
    return {
      jobChart: null,
      genderChart: null
    }
  },
  mounted() {
    this.initJobChart()
    this.initGenderChart()
    this.loadJobData()
    this.loadGenderData()
  },
  beforeUnmount() {
    if (this.jobChart) this.jobChart.dispose()
    if (this.genderChart) this.genderChart.dispose()
  },
  methods: {
    initJobChart() {
      this.jobChart = echarts.init(this.$refs.jobChartRef)
    },
    initGenderChart() {
      this.genderChart = echarts.init(this.$refs.genderChartRef)
    },
    async loadJobData() {
      try {
        const res = await axios.get(`${API_BASE}/report/empJobData`)
        if (res.data.code === 1) {
          const { jobList, dataList } = res.data.data
          this.jobChart.setOption({
            title: { text: '各职位员工数量', left: 'center', textStyle: { fontSize: 16 } },
            tooltip: { trigger: 'axis' },
            xAxis: {
              type: 'category',
              data: jobList,
              axisLabel: { rotate: 15 }
            },
            yAxis: {
              type: 'value',
              name: '人数',
              minInterval: 1
            },
            series: [{
              name: '人数',
              type: 'bar',
              data: dataList,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#409eff' },
                  { offset: 1, color: '#79bbff' }
                ])
              },
              barWidth: '40%'
            }]
          })
        }
      } catch (e) {
        console.error('加载职位数据失败', e)
      }
    },
    async loadGenderData() {
      try {
        const res = await axios.get(`${API_BASE}/report/empGenderData`)
        if (res.data.code === 1) {
          const genderData = res.data.data
          const names = genderData.map(item => item.name)
          const values = genderData.map(item => item.value)
          this.genderChart.setOption({
            title: { text: '性别分布', left: 'center', textStyle: { fontSize: 16 } },
            tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
            legend: { bottom: 10 },
            series: [{
              name: '性别',
              type: 'pie',
              radius: ['45%', '70%'],
              center: ['50%', '50%'],
              label: { show: true, formatter: '{b}\n{c}人' },
              data: [
                { value: values[0] || 0, name: names[0] || '男性员工', itemStyle: { color: '#409eff' } },
                { value: values[1] || 0, name: names[1] || '女性员工', itemStyle: { color: '#f56c6c' } }
              ],
              emphasis: {
                itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' }
              }
            }]
          })
        }
      } catch (e) {
        console.error('加载性别数据失败', e)
      }
    }
  }
}
</script>

<style scoped>
.card-header {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
</style>
