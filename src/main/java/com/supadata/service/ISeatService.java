package com.supadata.service;

import com.supadata.pojo.Seat;

/**
 * @ClassName: ISeatService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/13 17:21
 * @Description:
 */
public interface ISeatService {
    /**
     * 功能描述: 根据卡号和教室id查找座位信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 18:17
     */
    Seat queryByCNoAndRoomId(String card_number, Integer course_id, Integer room_id);
}
