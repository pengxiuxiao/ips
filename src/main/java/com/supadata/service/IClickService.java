package com.supadata.service;/**
 * @author pxx
 * @date 2019/9/6 17:54
 * @version 1.0
 */

import com.supadata.pojo.Click;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IClickService
 * @Description:
 * @Author: pxx
 * @Date: 2019/9/6 17:54
 * @Description:
 */
public interface IClickService {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Click record);

    Click selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Click record);

    List<Click> queryAllClick(Map<String, String> map);
}
