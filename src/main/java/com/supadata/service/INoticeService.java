package com.supadata.service;

import com.supadata.pojo.Notice;

import java.util.List;

/**
 * @ClassName: INoticeService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/12 10:47
 * @Description:
 */
public interface INoticeService {

    /**
     * 功能描述:查询已推送的消息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 23:58
     */
    List<Notice> queryPushNotice(String status, String type);

    /**
     * 功能描述:添加一条消息记录
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:04
     */
    int addNotice(Notice notice);

    /**
     * 功能描述:根据id查询消息记录
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:04
     */
    Notice queryById(Integer id);

    /**
     * 功能描述: 编辑消息记录
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:08
     */
    int editNotice(Notice notice);

    /**
     * 功能描述:删除一条消息记录
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:10
     */
    int deleteNotice(Integer id);

    /**
     * 功能描述:查询全部消息，含模糊查询
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 14:17
     */
    List<Notice> queryAllContainsKey(String type, String key);

    /**
     * 功能描述:根据roomid、类型查询对应的消息，可能多条
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/17 16:09
     */
    List<Notice> queryPushNoticeByRoomId(String status, String type, Integer roomId);

    /**
     * 功能描述: 按照pad分组，查询最大的视频
     * @auther:
     * @param:
     * @return:
     * @date: 2019/7/29 17:20
     */
    Notice queryMaxVideo();
}
