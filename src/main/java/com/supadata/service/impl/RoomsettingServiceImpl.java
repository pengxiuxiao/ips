package com.supadata.service.impl;

import com.supadata.dao.RoomSettingMapper;
import com.supadata.pojo.RoomSetting;
import com.supadata.service.IRoomsettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName: RoomsettingServiceImpl
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/19 16:12
 * @Description:
 */
@Service
public class RoomsettingServiceImpl implements IRoomsettingService {

    @Autowired
    private RoomSettingMapper roomSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return roomSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(RoomSetting record) {
        return roomSettingMapper.insertSelective(record);
    }

    @Override
    public RoomSetting selectByPrimaryKey(Integer id) {
        return roomSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RoomSetting record) {
        return roomSettingMapper.updateByPrimaryKeySelective(record);
    }
}
