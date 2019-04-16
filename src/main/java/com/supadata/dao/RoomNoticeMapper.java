package com.supadata.dao;

import com.supadata.pojo.RoomNotice;

public interface RoomNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomNotice record);

    int insertSelective(RoomNotice record);

    RoomNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomNotice record);

    int updateByPrimaryKey(RoomNotice record);
}