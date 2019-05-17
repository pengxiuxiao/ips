package com.supadata.dao;

import com.supadata.pojo.MImg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MImg record);

    int insertSelective(MImg record);

    MImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MImg record);

    int updateByPrimaryKey(MImg record);
}