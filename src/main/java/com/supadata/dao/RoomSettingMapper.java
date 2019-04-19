package com.supadata.dao;

import com.supadata.pojo.RoomSetting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomSetting record);

    int insertSelective(RoomSetting record);

    RoomSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomSetting record);

    int updateByPrimaryKey(RoomSetting record);
}