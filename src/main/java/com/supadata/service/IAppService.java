package com.supadata.service;

import com.supadata.pojo.App;

import java.util.List;

/**
 * @ClassName: IAppService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/30 21:14
 * @Description:
 */
public interface IAppService {
    /**
     * 功能描述: 查询全部记录
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/30 21:29
     */
    List<App> queryAll();

    int add(App app);

    App queryNewest(String type);
}
