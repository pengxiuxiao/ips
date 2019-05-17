package com.supadata.service;

import com.supadata.pojo.MImg;

/**
 * @Author: pxx
 * @Date: 2019/5/17 22:53
 * @Version 1.0
 */
public interface IMImgService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MImg record);

    MImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MImg record);
}
