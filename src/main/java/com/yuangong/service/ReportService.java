package com.yuangong.service;

import com.yuangong.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 统计员工职位人数
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别人数
     */
    List<Map<String, Object>> getEmpGenderData();

    /**
     * 统计各部门员工人数
     */
    List<Map<String, Object>> getEmpDeptData();
}
