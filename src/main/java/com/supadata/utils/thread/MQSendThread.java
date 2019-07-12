package com.supadata.utils.thread;

import com.supadata.constant.Mqtt;
import com.supadata.pojo.Pad;
import com.supadata.service.IPadService;
import com.supadata.utils.mqtt.PadServerMQTT;

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


    private Mqtt mqtt;

    private PadServerMQTT padServerMQTT;

    public IPadService padService;

    public MQSendThread(Mqtt mqtt, PadServerMQTT padServerMQTT, IPadService padService) {
        this.mqtt = mqtt;
        this.padServerMQTT = padServerMQTT;
        this.padService = padService;
    }

    @Override
    public void run() {

        //查询所有的pad
        List<Pad> pads = padService.queryAll(null);
        int index = 0;
        int region = Integer.parseInt(mqtt.getRegion());
        int loop = pads.size() / region;
        int remainder = (pads.size() % region) > 0 ? 1: 0;
        loop = loop + remainder;

        //发送消息 通知全部pad 修改显示模块
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("event", "video");

        for (int i = 0; i < loop; i++) {
            for (int j = 0; j < region; j++) {
                if (j + index < pads.size()) {
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pads.get(j + index).getClientId(), map);
                }
            }
            index = index + region;
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
