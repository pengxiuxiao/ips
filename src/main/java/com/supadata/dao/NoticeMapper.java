package com.supadata.dao;

import com.supadata.pojo.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);

    List<Notice> selectByStatusAndType(@Param("status") String status, @Param("type") String type);

    List<Notice> selectByTypeContainsKey(@Param("type") String type, @Param("key") String key);

    List<Notice> selectByRoomIdStatusAndType(@Param("status") String status, @Param("type") String type, @Param("roomId") Integer roomId);

    Notice selectMaxVideo();
}