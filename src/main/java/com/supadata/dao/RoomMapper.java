package com.supadata.dao;

import com.supadata.pojo.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    Room selectByIp(@Param("ip") String ip);

    List<Room> selectListIfKey(@Param("key") String key);

    List<Room> slelectAllRoom();
}