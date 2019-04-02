package com.supadata.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @ClassName: MQProducer
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/2 14:05
 * @Description:
 */
public class MQProducer {

    private static final String url = "tcp://118.178.84.40:61616";
    private static final String queueName = "peng-test";

    public static void main(String[] args) throws JMSException {
        //1创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        //2创建connection
        Connection connection = connectionFactory.createConnection();

        //3.启动链接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //5. 创建一个目标
        Destination destination = session.createQueue(queueName);

        //6.创建生产者
        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 100; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("peng" + i);

            //8.发布消息
            producer.send(textMessage);

            System.out.println("已发送消息："+textMessage.getText());
        }

        //9.关闭连接
        connection.close();

    }
}
