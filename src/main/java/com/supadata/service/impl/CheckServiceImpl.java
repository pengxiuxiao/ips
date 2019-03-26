package com.supadata.service.impl;

import com.supadata.dao.CheckMapper;
import com.supadata.pojo.Check;
import com.supadata.service.ICheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: CheckServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/12 18:37
 * @Description:
 */
@Service
public class CheckServiceImpl implements ICheckService {
    @Autowired
    private CheckMapper checkMapper;

    /**
     * 功能描述:查询最新的一条数据
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 18:39
     */
    @Override
    public Check queryNewest() {
        return checkMapper.selectNewest();
    }

    /**
     * 功能描述:添加轮询数据
     *
     * @param check
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 10:48
     */
    @Override
    public int add(Check check) {
        return checkMapper.insertSelective(check);
    }

    /**
     * 功能描述:查询截止时间后的更新数据
     *
     * @param check_time
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 11:24
     */
    @Override
    public List<Check> queryModifyCheck(String check_time) {
        return checkMapper.selectAfterCheckTime(check_time);
    }
}
