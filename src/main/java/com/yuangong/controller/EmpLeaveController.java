package com.yuangong.controller;

import com.yuangong.pojo.EmpLeave;
import com.yuangong.pojo.LeaveQueryParam;
import com.yuangong.pojo.PageResult;
import com.yuangong.pojo.Result;
import com.yuangong.service.EmpLeaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 请假管理Controller
 */
@RequestMapping("/leaves")
@RestController
public class EmpLeaveController {

    private static final Logger log = LoggerFactory.getLogger(EmpLeaveController.class);

    @Autowired
    private EmpLeaveService empLeaveService;

    /**
     * 分页查询请假记录
     */
    @GetMapping
    public Result page(LeaveQueryParam param) {
        log.info("分页查询请假记录: {}", param);
        PageResult<EmpLeave> pageResult = empLeaveService.page(param);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询请假记录
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询请假记录: {}", id);
        EmpLeave empLeave = empLeaveService.getById(id);
        return Result.success(empLeave);
    }

    /**
     * 新增请假申请
     */
    @PostMapping
    public Result add(@RequestBody EmpLeave empLeave) {
        log.info("新增请假申请: {}", empLeave);
        empLeaveService.add(empLeave);
        return Result.success();
    }

    /**
     * 审批请假：status=2 批准, status=3 拒绝
     */
    @PutMapping("/approve/{id}/{status}")
    public Result approve(@PathVariable Integer id, @PathVariable Integer status) {
        log.info("审批请假, id: {}, status: {}", id, status);
        empLeaveService.approve(id, status);
        return Result.success();
    }

    /**
     * 批量删除请假记录
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除请假记录: {}", ids);
        empLeaveService.delete(ids);
        return Result.success();
    }
}
