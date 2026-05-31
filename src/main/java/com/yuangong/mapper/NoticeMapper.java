package com.yuangong.mapper;

import com.yuangong.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 公告Mapper
 */
@Mapper
public interface NoticeMapper {

    /**
     * 条件查询公告
     */
    List<Notice> list(String title);

    /**
     * 根据ID查询公告
     */
    @Select("select id, title, content, publisher_id, create_time, update_time from notice where id = #{id}")
    Notice getById(Integer id);

    /**
     * 新增公告
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into notice(title, content, publisher_id, create_time, update_time) " +
            "values(#{title}, #{content}, #{publisherId}, #{createTime}, #{updateTime})")
    void insert(Notice notice);

    /**
     * 修改公告
     */
    void update(Notice notice);

    /**
     * 批量删除公告
     */
    void deleteByIds(List<Integer> ids);
}
