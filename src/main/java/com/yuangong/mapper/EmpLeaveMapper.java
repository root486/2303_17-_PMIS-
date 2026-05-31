package com.yuangong.mapper;

import com.yuangong.pojo.EmpLeave;
import com.yuangong.pojo.LeaveQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 请假Mapper
 */
@Mapper
public interface EmpLeaveMapper {

    /**
     * 条件分页查询请假记录
     */
    List<EmpLeave> list(LeaveQueryParam param);

    /**
     * 根据ID查询请假记录
     */
    @Select("select id, emp_id, leave_type, begin_date, end_date, reason, status, " +
            "approver_id, approve_time, create_time, update_time from emp_leave where id = #{id}")
    EmpLeave getById(Integer id);

    /**
     * 新增请假申请
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp_leave(emp_id, leave_type, begin_date, end_date, reason, status, create_time, update_time) " +
            "values(#{empId}, #{leaveType}, #{beginDate}, #{endDate}, #{reason}, #{status}, #{createTime}, #{updateTime})")
    void insert(EmpLeave empLeave);

    /**
     * 审批请假
     */
    void approve(EmpLeave empLeave);

    /**
     * 批量删除请假记录
     */
    void deleteByIds(List<Integer> ids);
}
