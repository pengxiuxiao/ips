package com.supadata.service.impl;

import com.supadata.dao.MImgMapper;
import com.supadata.pojo.MImg;
import com.supadata.service.IMImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: pxx
 * @Date: 2019/5/17 22:54
 * @Version 1.0
 */
@Service
public class MImgServiceImpl implements IMImgService {

    @Autowired
    private MImgMapper mImgMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return mImgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(MImg record) {
        return mImgMapper.insertSelective(record);
    }

    @Override
    public MImg selectByPrimaryKey(Integer id) {
        return mImgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(MImg record) {
        return mImgMapper.updateByPrimaryKeySelective(record);
    }
}
