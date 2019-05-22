package com.supadata.controller;

import com.supadata.constant.LRUCache;
import com.supadata.constant.Mqtt;
import com.supadata.mq.NoticeProducer;
import com.supadata.mq.PushCallback;
import com.supadata.utils.MsgJson;
import com.supadata.utils.mqtt.PadServerMQTT;
import org.apache.activemq.command.ActiveMQQueue;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private LRUCache lruCache;

    @Autowired
    private PadServerMQTT padServerMQTT;

    @RequestMapping("/login")
    public MsgJson ActionToMessage(String type, String content) {

        if ("1".equals(type)) {

            Map<String,Object> map = new LinkedHashMap();
            map.put("event","audio");
            map.put("value","400");
            padServerMQTT.publishMessage("/messagesub/event",map);
            return new MsgJson(0,"sucess");
        }
        return new MsgJson(1,"fail");
    }

    @RequestMapping("/tt")
    public MsgJson ActionToTTMessage(String type, String content) {

        try {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            MqttClient client = new MqttClient(mqtt.getHost(), mqtt.getClientId(), new MemoryPersistence());
            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(mqtt.getUserName());
            // 设置连接的密码
            options.setPassword(mqtt.getPassWord().toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调
            client.setCallback(new PushCallback());
            MqttTopic topic = client.getTopic(mqtt.getSubTopic());
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            options.setWill(topic, "close".getBytes(), 2, true);

            client.connect(options);
            //订阅消息
            int[] Qos  = {1};
            String[] topic1 = {mqtt.getSubTopic()};
            client.subscribe(topic1, Qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MsgJson(1,"fail");
    }

}
