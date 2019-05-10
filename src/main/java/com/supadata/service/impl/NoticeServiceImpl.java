package com.supadata.service.impl;

import com.supadata.dao.NoticeMapper;
import com.supadata.pojo.Notice;
import com.supadata.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: NoticeServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/12 10:47
 * @Description:
 */
@Service
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 功能描述:查询已推送的消息
     *
     * @param status
     * @param type
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 23:58
     */
    @Override
    public List<Notice> queryPushNotice(String status, String type) {
        return noticeMapper.selectByStatusAndType(status, type);
    }

    /**
     * 功能描述:添加一条消息记录
     *
     * @param notice
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:04
     */
    @Override
    public int addNotice(Notice notice) {
        return noticeMapper.insertSelective(notice);
    }


    /**
     * 功能描述:根据id查询消息记录
     *
     * @param i
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:04
     */
    @Override
    public Notice queryById(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    /**
     * 功能描述: 编辑消息记录
     *
     * @param notice
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:08
     */
    @Override
    public int editNotice(Notice notice) {
        return noticeMapper.updateByPrimaryKeySelective(notice);
    }

    /**
     * 功能描述:删除一条消息记录
     *
     * @param id
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:10
     */
    @Override
    public int deleteNotice(Integer id) {
        return noticeMapper.deleteByPrimaryKey(id);
    }

    /**
     * 功能描述:查询全部消息，含模糊查询
     *
     * @param type
     * @param key
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:17
     */
    @Override
    public List<Notice> queryAllContainsKey(String type, String key) {
        return noticeMapper.selectByTypeContainsKey(type,key);
    }

    @Override
    public List<Notice> queryPushNoticeByRoomId(String status, String type, Integer roomId) {
        return noticeMapper.selectByRoomIdStatusAndType(status, type, roomId);
    }
}
