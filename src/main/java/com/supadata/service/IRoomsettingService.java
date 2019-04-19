package com.supadata.service;

import com.supadata.pojo.RoomSetting;

/**
 * @ClassName: IRoomsettingService
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/19 16:11
 * @Description:
 */
public interface IRoomsettingService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(RoomSetting record);

    RoomSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomSetting record);
}
