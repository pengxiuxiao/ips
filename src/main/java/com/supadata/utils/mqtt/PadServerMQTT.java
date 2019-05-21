package com.supadata.utils.mqtt;

import com.supadata.constant.LRUCache;
import com.supadata.constant.Mqtt;
import com.supadata.mq.NoticeProducer;
import com.supadata.mq.PushCallback;
import net.sf.json.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: PadServerMQTT
 * @Description:
 * @Author: pxx
 * @Date: 2019/5/21 17:10
 * @Description:
 */
public class PadServerMQTT {

    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://118.178.84.40:1883";
    //定义一个主题
    public static final String TOPIC = "/messagesub/event";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "server-admin";

    private MqttClient client;
    private MqttTopic topic11;
    private String userName = "admin";
    private String passWord = "admin";

    private MqttMessage message;

    /**
     * 构造函数
     * @throws MqttException
     */
    public PadServerMQTT() {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        connect();
    }

    /**
     *  用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);

            topic11 = client.getTopic(TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! " + token.isComplete());
    }

    public void sendMesage(Map map) {
        MqttMessage msg = new MqttMessage();
        msg.setQos(1);
        msg.setRetained(true);

        JSONObject json = JSONObject.fromObject(map);
        msg.setPayload((json.toString()).getBytes());
        MqttTopic topic1 = client.getTopic(TOPIC);
        MqttDeliveryToken token = null;
        try {
            token = topic1.publish(msg);
            token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws MqttException, InterruptedException {
        PadServerMQTT server = new PadServerMQTT();


        Map<String,String> map = new LinkedHashMap();
        map.put("event","audio");
        map.put("value","500");
        server.sendMesage(map);

//        for (int i = 0; i < 100; i++) {
//            Thread.sleep(5000);
//            server.message.setPayload((i + "hello,pxx").getBytes());
//            server.publish(server.topic11 , server.message);
//            System.out.println(i + "hello,pxx");
//        }
    }


}
