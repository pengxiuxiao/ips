package com.supadata.controller;

import com.supadata.mq.NoticeProducer;
import com.supadata.utils.MsgJson;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

/**
 * @ClassName: MQController
 * @Description:
 * @Author: pxx
 * @Date: 2019/3/27 17:58
 * @Description:
 */
@RestController
@RequestMapping("/mq")
public class MQController {

    @Autowired
    private NoticeProducer producer;


    @RequestMapping("/action")
    public MsgJson ActionToMessage(String type, String content) {

        if ("1".equals(type)) {
            Destination destination = new ActiveMQQueue("pxx.test1");

            producer.sendMessage(destination, content);

            return new MsgJson(0,"sucess");
        }
        return new MsgJson(1,"fail");
    }

}
