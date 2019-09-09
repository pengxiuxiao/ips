package com.supadata.dao;

import com.supadata.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Course record);

    int insertSelective(Course record);

    Course selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKey(Course record);

    List<Course> selectCourseAfterTime(@Param("time") String time);

    Course selectCourseByRoomIdAndCNo(@Param("card_number") String card_number, @Param("room_id") Integer room_id, @Param("currentDateTime") String currentDateTime);

    Course selectCourseByCNo(@Param("card_number") String card_number, @Param("currentDateTime") String currentDateTime);

    List<Course> selectAllCourse(@Param("key") String key, @Param("type") Integer type);

    int updateDeleteStatus(@Param("id") Integer id);

    Course selectCourseByRoomId(@Param("roomId") Integer roomId);
}