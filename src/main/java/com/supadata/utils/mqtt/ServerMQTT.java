package com.supadata.utils.mqtt;

import com.supadata.mq.PushCallback;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 功能描述:
 * 服务器向多个客户端推送主题，即不同客户端可向服务端订阅相同的主题
 * @auther: pxx
 * @param:
 * @return:
 * @date: 2019/4/2 16:57
 */
public class ServerMQTT {
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
    public ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
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
        System.out.println("message is published completely! "
                + token.isComplete());
    }

    /**
     *  启动入口
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws MqttException, InterruptedException {
        ServerMQTT server = new ServerMQTT();

        server.message = new MqttMessage();
        server.message.setQos(1);
        server.message.setRetained(true);

        Map<String,String> map = new LinkedHashMap();
        map.put("event","audio");
        map.put("value","50");
        JSONObject json = JSONObject.fromObject(map);
        server.message.setPayload((json.toString()).getBytes());
        server.publish(server.topic11 , server.message);

//        for (int i = 0; i < 100; i++) {
//            Thread.sleep(5000);
//            server.message.setPayload((i + "hello,pxx").getBytes());
//            server.publish(server.topic11 , server.message);
//            System.out.println(i + "hello,pxx");
//        }
    }
}
