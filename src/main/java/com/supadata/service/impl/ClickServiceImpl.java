package com.supadata.service.impl;

import com.supadata.dao.ClickMapper;
import com.supadata.dao.CourseMapper;
import com.supadata.pojo.Click;
import com.supadata.service.IClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ClickServiceImpl
 * @Description:
 * @Author: pxx
 * @Date: 2019/9/6 17:55
 * @Description:
 */

@Service
@Transactional
public class ClickServiceImpl implements IClickService {

    @Autowired
    private ClickMapper clickMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return clickMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(Click record) {
        return clickMapper.insertSelective(record);
    }

    @Override
    public Click selectByPrimaryKey(Integer id) {
        return clickMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Click record) {
        return clickMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Click> queryAllClick(Map<String, String> map) {
        return clickMapper.queryAllClick(map);
    }

    @Override
    public Click selectByTypeAndDate(Map<String, String> map) {
        return clickMapper.selectByTypeAndDate(map);
    }

}
