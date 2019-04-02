package com.supadata.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @ClassName: MQConsumer
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/2 14:17
 * @Description:
 */
public class MQConsumer {

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

        //6.创建消费者
        MessageConsumer consumer = session.createConsumer(destination);

        //7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("接收消息：" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });


//        //8.关闭连接
//        connection.close();

    }
}
