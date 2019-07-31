package com.supadata;

import com.supadata.mq.NoticeProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

/**
 * @ClassName: SpringbootJmsApplicationTests
 * @Description:
 * @Author: pxx
 * @Date: 2019/3/27 16:29
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootJmsApplicationTests {

    @Autowired
    private NoticeProducer producer;


    @Test
    public void contextLoads() throws InterruptedException {
        Destination destination = new ActiveMQQueue("mytest.queue");

        for (int i = 0; i < 10; i++) {
            producer.sendMessage(destination, "myname is pxx" + i);
        }
    }


}
