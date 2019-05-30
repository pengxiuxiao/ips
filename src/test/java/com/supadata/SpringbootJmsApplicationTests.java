package com.supadata;

import com.supadata.mq.NoticeProducer;
import com.supadata.pojo.Setting;
import com.supadata.service.ISettingService;
import com.supadata.utils.DateUtil;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Autowired
    private ISettingService settingService;

    @Test
    public void contextLoads() throws InterruptedException {
        Destination destination = new ActiveMQQueue("mytest.queue");

        for (int i = 0; i < 10; i++) {
            producer.sendMessage(destination, "myname is pxx" + i);
        }
    }


    @Test
    public void testPadCoseTime() {
        Date curDate = new Date();
        Setting setting = settingService.querySetting();
        Setting newSet = new Setting();
        newSet.setsEndTime(curDate);
        newSet.setsStartTime(DateUtil.changeToDate("2019-05-29 10:38:58", "yyyy-MM-dd HH:mm:ss"));




        Map<String, Object> map = new LinkedHashMap<>();

        if (!newSet.getsStartTime().equals(setting.getsStartTime())
                || !newSet.getsEndTime().equals(setting.getsEndTime())) {//开关机时间变更
            map.clear();
            Map<String, Long> dateMap = DateUtil.handleOpenClosePadTime(DateUtil.dateToLong(newSet.getsStartTime()),DateUtil.dateToLong(newSet.getsEndTime()));
            map.put("event", "onoff");
            map.put("wakeTime", dateMap.get("open"));
            map.put("sleepTime", dateMap.get("close"));
        }
        System.out.println(map);
    }
}
