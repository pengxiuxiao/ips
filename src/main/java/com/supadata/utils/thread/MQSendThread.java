package com.supadata.utils.thread;

import com.supadata.constant.Mqtt;
import com.supadata.controller.CourseController;
import com.supadata.pojo.Pad;
import com.supadata.service.IPadService;
import com.supadata.utils.mqtt.PadServerMQTT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MQSendThread
 * @Description:
 * @Author: pxx
 * @Date: 2019/7/12 16:56
 * @Description:
 */

public class MQSendThread extends Thread {

    private static Logger logger = Logger.getLogger(MQSendThread.class);

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

    private Mqtt mqtt;

    private PadServerMQTT padServerMQTT;

    private IPadService padService;

    private JSONArray idList;

    private Integer interval;

    private Integer loop;

    public MQSendThread(Mqtt mqtt, PadServerMQTT padServerMQTT, IPadService padService, JSONArray idList, Integer interval, Integer loop) {
        this.mqtt = mqtt;
        this.padServerMQTT = padServerMQTT;
        this.padService = padService;
        this.idList = idList;
        this.interval = interval;
        this.loop = loop;
    }

    @Override
    public void run() {

        logger.info("thread:" + Thread.currentThread().getName() + ",time:" + format.format(new Date()));

        //针对选中的所有的pad
        int index = 0;
        int region = Integer.parseInt(mqtt.getRegion());

        //发送消息 通知全部pad 修改显示模块
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("event", "video");

        for (int i = 0; i < loop; i++) {
            for (int j = 0; j < region; j++) {
                if (j + index < idList.size()) {
                    JSONObject oPad = idList.getJSONObject(j + index);
                    String clientId = oPad.getString("clientId");
                    Integer id = oPad.getInt("id");
                    Pad tmpPad = new Pad();
                    tmpPad.setId(id);
                    tmpPad.setpModuleFront("3");
                    int res = padService.update(tmpPad);
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + clientId, map);
                }
            }
            index = index + region;
            try {
                logger.info("thread:" + Thread.currentThread().getName() + ",sllep:" + interval + "s.");
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
