package com.supadata.dao;

import com.supadata.pojo.StudentCard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentCard record);

    int insertSelective(StudentCard record);

    StudentCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentCard record);

    int updateByPrimaryKey(StudentCard record);
}