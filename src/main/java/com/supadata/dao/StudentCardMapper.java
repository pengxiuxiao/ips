package com.supadata.dao;

import com.supadata.pojo.StudentCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentCard record);

    int insertSelective(StudentCard record);

    StudentCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentCard record);

    int updateByPrimaryKey(StudentCard record);

    List<StudentCard> selectAllList(Map<String, String> map);

    StudentCard selectByNumber(@Param("number") String number);

    int deleteByCourseId(@Param("course_id") Integer course_id);
}