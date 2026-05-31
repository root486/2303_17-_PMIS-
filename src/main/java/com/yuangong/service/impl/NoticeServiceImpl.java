package com.yuangong.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuangong.mapper.NoticeMapper;
import com.yuangong.pojo.Notice;
import com.yuangong.pojo.PageResult;
import com.yuangong.service.NoticeService;
import com.yuangong.utils.CurrentHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public PageResult<Notice> page(Integer page, Integer pageSize, String title) {
        //1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        //2. 执行查询
        List<Notice> noticeList = noticeMapper.list(title);

        //3. 解析查询结果, 并封装
        Page<Notice> p = (Page<Notice>) noticeList;
        return new PageResult<Notice>(p.getTotal(), p.getResult());
    }

    @Override
    public Notice getById(Integer id) {
        return noticeMapper.getById(id);
    }

    @Override
    public void add(Notice notice) {
        //1. 补全基础属性
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        notice.setPublisherId(CurrentHolder.getCurrentId());

        //2. 调用Mapper接口方法插入数据
        noticeMapper.insert(notice);
        log.info("新增公告成功, id: {}", notice.getId());
    }

    @Override
    public void update(Notice notice) {
        //1. 补全基础属性
        notice.setUpdateTime(LocalDateTime.now());

        //2. 调用Mapper接口方法更新公告
        noticeMapper.update(notice);
        log.info("修改公告成功, id: {}", notice.getId());
    }

    @Override
    public void delete(List<Integer> ids) {
        noticeMapper.deleteByIds(ids);
        log.info("批量删除公告成功, ids: {}", ids);
    }
}
