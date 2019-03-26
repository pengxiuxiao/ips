package com.supadata.dao;

import com.supadata.pojo.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SeatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Seat record);

    int insertSelective(Seat record);

    Seat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Seat record);

    int updateByPrimaryKey(Seat record);

    Seat selectByCNoAndRoomId(@Param("card_number") String card_number, @Param("course_id") Integer course_id, @Param("room_id") Integer room_id);
}