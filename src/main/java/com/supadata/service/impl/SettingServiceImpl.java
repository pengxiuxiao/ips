package com.supadata.service.impl;

import com.supadata.dao.SettingMapper;
import com.supadata.pojo.Setting;
import com.supadata.service.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SettingServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/12 19:36
 * @Description:
 */
@Service
public class SettingServiceImpl implements ISettingService {

    @Autowired
    private SettingMapper settingMapper;

    /**
     * 功能描述:
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 19:37
     */
    @Override
    public Setting querySetting() {
        return settingMapper.selectNewest();
    }

    /**
     * 功能描述:添加设置
     *
     * @param setting
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/26 20:13
     */
    @Override
    public int add(Setting setting) {
        return settingMapper.insertSelective(setting);
    }
}
