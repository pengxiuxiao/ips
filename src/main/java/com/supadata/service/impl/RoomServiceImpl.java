package com.supadata.service.impl;

import com.supadata.dao.RoomMapper;
import com.supadata.pojo.Room;
import com.supadata.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: RoomServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/11 22:06
 * @Description:
 */
@Service
public class RoomServiceImpl implements IRoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public Room queryRoomByIp(String ip) {
        return roomMapper.selectByIp(ip);
    }

    @Override
    public int add(Room room) {
        return roomMapper.insertSelective(room);
    }

    @Override
    public Room queryRoomById(Integer id) {
        return roomMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateRoom(Room room) {
        return roomMapper.updateByPrimaryKeySelective(room);
    }

    @Override
    public int deleteRoom(Integer id) {
        return roomMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Room> queryRoom(String key) {
        return roomMapper.selectListIfKey(key);
    }
}
