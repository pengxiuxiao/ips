package com.supadata.mq;

import com.supadata.constant.Mqtt;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Date;

/**
 * @Author: pxx
 * @Date: 2019/4/3 22:57
 * @Version 1.0
 */
//@Component
public class TopicConsumer implements ApplicationRunner {

    @Autowired
    private Mqtt mqtt;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }
    public  void init() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(
                mqtt.getUserName(),
                mqtt.getPassWord(),
                "tcp://118.178.84.40:61616"
        );

        Connection conn = factory.createConnection();
//        conn.setClientID("consumer1");
        conn.start();

        Session session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        //与生产者的消息目的地相同
        Destination dest = session.createTopic(mqtt.getSubTopic());

        MessageConsumer messConsumer = session.createConsumer(dest);

        messConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    MapMessage m = (MapMessage)message;
                    System.out.println("consumer1接收到"+m.getString("reqDesc")+"的请求并开始处理，时间是"+new Date());
                    System.out.println("这里会停顿5s，模拟系统处理请求，时间是"+new Date());
                    Thread.sleep(5000);
                    System.out.println("consumer1接收到"+m.getString("reqDesc")+"的请求并处理完毕，时间是"+new Date());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

}
