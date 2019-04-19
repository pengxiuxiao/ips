package com.supadata.service.impl;

import com.supadata.dao.RoomNoticeMapper;
import com.supadata.pojo.RoomNotice;
import com.supadata.service.IRoomNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: pxx
 * @Date: 2019/4/18 22:13
 * @Version 1.0
 */
@Service
public class RoomNoticeServiceImpl implements IRoomNoticeService {

    @Autowired
    private RoomNoticeMapper roomNoticeMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return roomNoticeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(RoomNotice record) {
        return roomNoticeMapper.insertSelective(record);
    }

    @Override
    public RoomNotice selectByPrimaryKey(Integer id) {
        return roomNoticeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RoomNotice record) {
        return roomNoticeMapper.updateByPrimaryKeySelective(record);
    }
}
