package com.supadata.service;

import com.supadata.pojo.Setting;

/**
 * @ClassName: ISettingService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/12 19:36
 * @Description:
 */
public interface ISettingService {
    /**
     * 功能描述:
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 19:38
     */
    Setting querySetting();

    /**
     * 功能描述:添加设置
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/26 20:13
     */
    int add(Setting setting);
}
