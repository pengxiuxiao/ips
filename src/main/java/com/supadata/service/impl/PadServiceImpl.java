package com.supadata.service.impl;

import com.supadata.dao.PadMapper;
import com.supadata.pojo.Pad;
import com.supadata.service.IPadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: PadServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/10 23:46
 * @Description:
 */
@Service
public class PadServiceImpl implements IPadService {

    @Autowired
    private PadMapper padMapper;

    @Override
    public Pad queryByCode(String code) {
        return padMapper.selectByCode(code);
    }

    /**
     * 功能描述: 添加pad数据
     *
     * @param pad
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/11 22:41
     */
    @Override
    public int add(Pad pad) {
        return padMapper.insertSelective(pad);
    }

    /**
     * 功能描述:查询全部pad
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/7/2 11:44
     * @param key
     */
    @Override
    public List<Pad> queryAll(String key) {
        return padMapper.selectAll(key);
    }

    @Override
    public int updateTimeByCode(String code, String curDate) {
        return padMapper.updateTimeByCode(code, curDate);
    }

    /**
     * 功能描述:更新
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/7/12 20:39
     */
    @Override
    public int update(Pad pad) {
        return padMapper.updateByPrimaryKeySelective(pad);
    }

    /**
     * 功能描述:根据id查找
     *
     * @param id
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/7/12 20:41
     */
    @Override
    public Pad queryById(Integer id) {
        return padMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Pad> queryByRoomId(Integer roomId) {
        return padMapper.selectByRoomId(roomId);
    }
}
