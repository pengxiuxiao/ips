package com.supadata.dao;

import com.supadata.pojo.Click;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClickMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Click record);

    Click selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Click record);

    List<Click> queryAllClick(Map<String, String> map);

    Click selectByTypeAndDate(Map<String, String> map);
}