package com.supadata.service.impl;

import com.supadata.dao.StudentCardMapper;
import com.supadata.service.IStudentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
