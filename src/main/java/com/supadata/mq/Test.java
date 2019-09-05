package com.supadata.mq;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: Test
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/2 15:20
 * @Description:
 */
public class Test {

    private static final String HOST = "tcp://118.178.84.40:1883";
    private String TOPIC = "/messagesub/event/";
    private String MESSAGE = "123";
    private static final String clientid = "server";

    private MqttClient client;
    private MqttTopic topic;

    private String userName = "admin";
    private String passWord = "admin";

    private MqttMessage message;

    public String doPublish() throws MqttException {

        //new mqttClient
        //MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        //与activeMQ连接的方法
        connect();
        //new mqttMessage
        message = new MqttMessage();
        //设置服务质量
        message.setQos(2);
        //设置是否在服务器中保存消息体
        message.setRetained(true);
        //设置消息的内容
        message.setPayload(MESSAGE.getBytes());
        //发布
        publish(topic, message);

        System.out.println("已发送");

        return "result";
    }

    private void connect() {
        // new mqttConnection 用来设置一些连接的属性
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        // 换而言之，设置为false时可以客户端可以接受离线消息
        options.setCleanSession(false);
        // 设置连接的用户名和密码
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            // 设置回调类
                client.setCallback(new PushCallback());
            // 连接
            client.connect(options);
            // 获取activeMQ上名为TOPIC的topic
            topic = client.getTopic(TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
        // 发布的方法
        // new mqttDeliveryToken
        MqttDeliveryToken token = topic.publish(message);
        // 发布
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }

    public static void main(String[] args) throws MqttException {
        Test test = new Test();
        test.doPublish();
    }
}
