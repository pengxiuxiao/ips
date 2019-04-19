package com.supadata.service;

import com.supadata.pojo.Room;

import java.util.List;

/**
 * 功能描述:
 * @auther: pxx
 * @param: 
 * @return:
 * @date: 2018/6/11 22:12
 */
public interface IRoomService {
    Room queryRoomByIp(String ip);

    int add(Room room);

    Room queryRoomById(Integer id);

    int updateRoom(Room room);

    int deleteRoom(Integer id);

    List<Room> queryRoom(String key);

    List<Room> slelectAllRoom();
}
