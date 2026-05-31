package com.yuangong.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuangong.exception.BusinessException;
import com.yuangong.mapper.EmpLeaveMapper;
import com.yuangong.pojo.EmpLeave;
import com.yuangong.pojo.LeaveQueryParam;
import com.yuangong.pojo.PageResult;
import com.yuangong.service.EmpLeaveService;
import com.yuangong.utils.CurrentHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpLeaveServiceImpl implements EmpLeaveService {

    private static final Logger log = LoggerFactory.getLogger(EmpLeaveServiceImpl.class);

    @Autowired
    private EmpLeaveMapper empLeaveMapper;

    @Override
    public PageResult<EmpLeave> page(LeaveQueryParam param) {
        //1. 设置分页参数
        PageHelper.startPage(param.getPage(), param.getPageSize());

        //2. 执行查询
        List<EmpLeave> leaveList = empLeaveMapper.list(param);

        //3. 解析查询结果, 并封装
        Page<EmpLeave> p = (Page<EmpLeave>) leaveList;
        return new PageResult<EmpLeave>(p.getTotal(), p.getResult());
    }

    @Override
    public EmpLeave getById(Integer id) {
        return empLeaveMapper.getById(id);
    }

    @Override
    public void add(EmpLeave empLeave) {
        //1. 校验empId
        if (empLeave.getEmpId() == null) {
            throw new BusinessException("员工ID不能为空");
        }

        //2. 补全基础属性
        empLeave.setStatus(1);
        empLeave.setCreateTime(LocalDateTime.now());
        empLeave.setUpdateTime(LocalDateTime.now());

        //3. 调用Mapper接口方法插入数据
        empLeaveMapper.insert(empLeave);
        log.info("新增请假申请成功, empId: {}, id: {}", empLeave.getEmpId(), empLeave.getId());
    }

    @Override
    public void approve(Integer id, Integer status) {
        //1. 校验审批状态
        if (status != 2 && status != 3) {
            throw new BusinessException("审批状态非法，仅允许 2(已批准) 或 3(已拒绝)");
        }

        //2. 组装审批信息
        EmpLeave empLeave = new EmpLeave();
        empLeave.setId(id);
        empLeave.setStatus(status);
        empLeave.setApproverId(CurrentHolder.getCurrentId());
        empLeave.setApproveTime(LocalDateTime.now());
        empLeave.setUpdateTime(LocalDateTime.now());

        //3. 调用Mapper接口方法更新
        empLeaveMapper.approve(empLeave);
        log.info("请假审批完成, id: {}, status: {}", id, status);
    }

    @Override
    public void delete(List<Integer> ids) {
        empLeaveMapper.deleteByIds(ids);
        log.info("批量删除请假记录成功, ids: {}", ids);
    }
}
