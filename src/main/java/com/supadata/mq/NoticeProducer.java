package com.supadata.mq;


import com.supadata.utils.MsgJson;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @ClassName: NoticeProducer
 * @Description:
 * @Author: pxx
 * @Date: 2019/3/27 16:17
 * @Description:
 */
@Service("noticeProducer")
public class NoticeProducer {

    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private JmsMessagingTemplate jmsTemplate;


    /**
     * 功能描述:发送消息，destination是发送到的队列，message是待发送的消息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/3/27 17:55
     */
    public void sendMessage(Destination destination, final String message){
        jmsTemplate.convertAndSend(destination, message);
    }



}
