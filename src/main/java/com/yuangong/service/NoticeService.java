package com.yuangong.service;

import com.yuangong.pojo.Notice;
import com.yuangong.pojo.PageResult;

import java.util.List;

public interface
NoticeService {

    /**
     * 分页查询公告
     */
    PageResult<Notice> page(Integer page, Integer pageSize, String title);

    /**
     * 根据ID查询公告
     */
    Notice getById(Integer id);

    /**
     * 新增公告
     */
    void add(Notice notice);

    /**
     * 修改公告
     */
    void update(Notice notice);

    /**
     * 批量删除公告
     */
    void delete(List<Integer> ids);
}
