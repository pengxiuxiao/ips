package com.supadata.dao;

import com.supadata.pojo.Click;

import java.util.List;
import java.util.Map;

public interface ClickMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Click record);

    int insertSelective(Click record);

    Click selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Click record);

    int updateByPrimaryKey(Click record);

    List<Click> queryAllClick(Map<String, String> map);
}