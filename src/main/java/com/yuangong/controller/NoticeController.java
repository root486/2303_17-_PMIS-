package com.yuangong.controller;

import com.yuangong.pojo.Notice;
import com.yuangong.pojo.PageResult;
import com.yuangong.pojo.Result;
import com.yuangong.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告管理Controller
 */
@RequestMapping("/notices")
@RestController
public class NoticeController {

    private static final Logger log = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    /**
     * 分页查询公告
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String title) {
        log.info("分页查询公告, page: {}, pageSize: {}, title: {}", page, pageSize, title);
        PageResult<Notice> pageResult = noticeService.page(page, pageSize, title);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询公告
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询公告: {}", id);
        Notice notice = noticeService.getById(id);
        return Result.success(notice);
    }

    /**
     * 新增公告
     */
    @PostMapping
    public Result add(@RequestBody Notice notice) {
        log.info("新增公告: {}", notice);
        noticeService.add(notice);
        return Result.success();
    }

    /**
     * 修改公告
     */
    @PutMapping
    public Result update(@RequestBody Notice notice) {
        log.info("修改公告: {}", notice);
        noticeService.update(notice);
        return Result.success();
    }

    /**
     * 批量删除公告
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除公告: {}", ids);
        noticeService.delete(ids);
        return Result.success();
    }
}
