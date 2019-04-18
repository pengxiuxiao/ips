package com.supadata.service;

import com.supadata.pojo.RoomNotice;

/**
 * @Author: pxx
 * @Date: 2019/4/18 22:12
 * @Version 1.0
 */
public interface IRoomNoticeService {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(RoomNotice record);

    RoomNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomNotice record);
}
