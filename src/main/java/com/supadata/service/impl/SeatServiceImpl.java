package com.supadata.service.impl;

import com.supadata.dao.SeatMapper;
import com.supadata.pojo.Seat;
import com.supadata.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SeatServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/13 17:21
 * @Description:
 */
@Service
public class SeatServiceImpl implements ISeatService {

    @Autowired
    private SeatMapper seatMapper;
    /**
     * 功能描述: 根据卡号和教室id查找座位信息
     *
     * @param card_number
     * @param courseId
     * @param room_id
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 18:17
     */
    @Override
    public Seat queryByCNoAndRoomId(String card_number, Integer course_id, Integer room_id) {
        return seatMapper.selectByCNoAndRoomId(card_number, course_id, room_id);
    }
}
