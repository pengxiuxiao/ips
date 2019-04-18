package com.supadata.dao;

import com.supadata.pojo.Setting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Setting record);

    int insertSelective(Setting record);

    Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Setting record);

    int updateByPrimaryKey(Setting record);

    Setting selectNewest();

    Setting selectSettingByRoomId(@Param("roomId") Integer roomId);
}