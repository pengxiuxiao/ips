package com.supadata.service;

import com.supadata.pojo.Check;

import java.util.List;

/**
 * @ClassName: ICheckService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/12 18:37
 * @Description:
 */
public interface ICheckService {
    /**
     * 功能描述:查询最新的一条数据
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 18:39
     */
    Check queryNewest();

    /**
     * 功能描述:添加轮询数据
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/25 10:48
     */
    int add(Check check);

    /**
     * 功能描述:查询截止时间后的更新数据
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/25 11:24
     */
    List<Check> queryModifyCheck(String check_time);
}
