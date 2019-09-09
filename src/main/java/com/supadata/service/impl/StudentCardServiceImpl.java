package com.supadata.service.impl;

import com.supadata.dao.StudentCardMapper;
import com.supadata.pojo.StudentCard;
import com.supadata.service.IStudentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: StudentCardServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/25 23:16
 * @Description:
 */
@Service
public class StudentCardServiceImpl implements IStudentCardService {

    @Autowired
    private StudentCardMapper studentCardMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return studentCardMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(StudentCard record) {
        return studentCardMapper.insertSelective(record);
    }

    @Override
    public StudentCard selectByPrimaryKey(Integer id) {
        return studentCardMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(StudentCard record) {
        return studentCardMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<StudentCard> selectAllList(Map<String, String> map) {
        return studentCardMapper.selectAllList(map);
    }

    @Override
    public StudentCard selectByNumber(String number) {
        return studentCardMapper.selectByNumber(number);
    }

    @Override
    public int deleteByCourseId(Integer course_id) {
        return studentCardMapper.deleteByCourseId(course_id);
    }
}
