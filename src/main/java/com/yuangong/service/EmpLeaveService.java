package com.yuangong.service;

import com.yuangong.pojo.EmpLeave;
import com.yuangong.pojo.LeaveQueryParam;
import com.yuangong.pojo.PageResult;

import java.util.List;

public interface EmpLeaveService {

    /**
     * 分页查询请假记录
     */
    PageResult<EmpLeave> page(LeaveQueryParam param);

    /**
     * 根据ID查询请假记录
     */
    EmpLeave getById(Integer id);

    /**
     * 新增请假申请
     */
    void add(EmpLeave empLeave);

    /**
     * 审批请假 (status: 2-已批准, 3-已拒绝)
     */
    void approve(Integer id, Integer status);

    /**
     * 批量删除请假记录
     */
    void delete(List<Integer> ids);
}
