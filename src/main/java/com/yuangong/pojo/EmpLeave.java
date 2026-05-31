package com.yuangong.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工请假
 */
public class EmpLeave {
    private Integer id;
    private Integer empId;
    private String empName;
    private Integer leaveType;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String reason;
    private Integer status;
    private Integer approverId;
    private LocalDateTime approveTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public EmpLeave() {
    }

    public EmpLeave(Integer id, Integer empId, String empName, Integer leaveType, LocalDate beginDate,
                    LocalDate endDate, String reason, Integer status, Integer approverId,
                    LocalDateTime approveTime, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.empId = empId;
        this.empName = empName;
        this.leaveType = leaveType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.approverId = approverId;
        this.approveTime = approveTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public LocalDateTime getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(LocalDateTime approveTime) {
        this.approveTime = approveTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "EmpLeave{" +
                "id=" + id +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", leaveType=" + leaveType +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", approverId=" + approverId +
                ", approveTime=" + approveTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
