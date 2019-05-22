package com.supadata.dao;

import com.supadata.pojo.Pad;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Pad record);

    int insertSelective(Pad record);

    Pad selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pad record);

    int updateByPrimaryKey(Pad record);

    Pad selectByCode(@Param("code") String code);

    List<Pad> selectAll(@Param("key") String key);

    int updateTimeByCode(@Param("code") String code, @Param("curDate") String curDate);

    List<Pad> selectByRoomId(@Param("roomId") Integer roomId);
}