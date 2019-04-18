package com.supadata.service;

import com.supadata.pojo.StudentCard;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IStudentCardService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/25 23:16
 * @Description:
 */
public interface IStudentCardService {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(StudentCard record);

    StudentCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentCard record);

    List<StudentCard> selectAllList(Map<String, String> map);

    StudentCard selectByNumber(String number);
}
