package com.supadata.service.impl;

import com.supadata.dao.AppMapper;
import com.supadata.pojo.App;
import com.supadata.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: AppServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/30 21:15
 * @Description:
 */
@Service
public class AppServiceImpl implements IAppService {
    @Autowired
    private AppMapper appMapper;

    /**
     * 功能描述: 查询全部记录
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/30 21:29
     */
    @Override
    public List<App> queryAll() {
        return appMapper.selectAll();
    }

    @Override
    public int add(App app) {
        return appMapper.insertSelective(app);
    }

    @Override
    public App queryNewest(String type) {
        return appMapper.selectNewest(type);
    }
}
