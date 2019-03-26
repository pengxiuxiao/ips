package com.supadata.service;

import com.supadata.pojo.Pad;

import java.util.List;

public interface IPadService {
    /**
     * 功能描述:通过code查找pad
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/11 22:40
     */
    Pad queryByCode(String code);

    /**
     * 功能描述: 添加pad数据
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/11 22:41
     */
    int add(Pad pad);

    /**
     * 功能描述:查询全部pad
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/7/2 11:44
     * @param key
     */
    List<Pad> queryAll(String key);

    int updateTimeByCode(String code, String curDate);

    /**
     * 功能描述:更新
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/7/12 20:39
     */
    int update(Pad pad);

    /**
     * 功能描述:根据id查找
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/7/12 20:41
     */
    Pad queryById(Integer id);

}
