package com.supadata.utils.mqtt;

import com.supadata.constant.LRUCache;
import com.supadata.constant.Mqtt;
import com.supadata.controller.CourseController;
import com.supadata.mq.NoticeProducer;
import com.supadata.mq.PushCallback;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.sun.MagicInstantiator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: PadServerMQTT
 * @Description:
 * @Author: pxx
 * @Date: 2019/5/21 17:10
 * @Description:
 */
@Component
public class PadServerMQTT {

    private static Logger logger = Logger.getLogger(PadServerMQTT.class);

    @Autowired
    private Mqtt mqtt;

    private  MqttClient client;

    private MqttConnectOptions options;


    @PostConstruct
    public void init(){
        try {
            client = new MqttClient(mqtt.getHost(), "server-admin", new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(mqtt.getUserName());
            options.setPassword(mqtt.getPassWord().toCharArray());
            // 设置超时时间
            options.setConnectionTimeout(10);
            // 设置会话心跳时间
            options.setKeepAliveInterval(20);
//            client.setCallback(new PushCallback());
            client.connect(options);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述:发布消息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/5/22 15:38
     */
    public void publishMessage(String topic, Map<String, Object> map) {
        try {
            MqttTopic topic11 = this.client.getTopic(topic);
            MqttMessage message = new MqttMessage();
            message.setQos(0);
//            message.setRetained(true);
            JSONObject json = JSONObject.fromObject(map);
            message.setPayload((json.toString()).getBytes());

            MqttDeliveryToken token = topic11.publish(message);
            token.waitForCompletion();

            logger.info("发布消息：topic = " + topic + ", content = " + json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws MqttException {
        MqttClient client = new MqttClient("tcp://118.178.84.40:1883", "server-admin", new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);

            MqttTopic topic11 = client.getTopic("/messagesub/event");
            MqttMessage message = new MqttMessage();
            message.setQos(1);
            message.setRetained(true);
            Map<String,String> map = new LinkedHashMap();
            map.put("event","audio");
            map.put("value","400");
            JSONObject json = JSONObject.fromObject(map);
            message.setPayload((json.toString()).getBytes());

            MqttDeliveryToken token = topic11.publish(message);
            token.waitForCompletion();

            System.out.println("message is published completely! " + token.isComplete());



        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
