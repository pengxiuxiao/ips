package com.supadata.mq;


import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;


/**
 * @ClassName: NoticeConsumer
 * @Description:
 * @Author: pxx
 * @Date: 2019/3/27 16:24
 * @Description:
 */
@Component
@EnableJms
public class NoticeConsumer {

    /**
     * 功能描述:使用JmsListener配置消费者监听的队列，其中text是接收到的消息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/3/27 18:14
     */
//    @JmsListener(destination = "pxx.test1")
//    public void consumerMessage(String text) {
//        System.out.println("收到的消息======" + text);
//    }
//
//    @JmsListener(destination = "/messagesub/event")
//    public void readActiveTopic(MqttMessage text) {
//        System.out.println("收到消息======" + text);
//    }

}
