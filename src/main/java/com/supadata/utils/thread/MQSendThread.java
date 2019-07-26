package com.supadata.utils.thread;

import com.supadata.constant.Mqtt;
import com.supadata.pojo.Pad;
import com.supadata.service.IPadService;
import com.supadata.utils.mqtt.PadServerMQTT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

    private IPadService padService;

    private JSONArray idList;

    public MQSendThread(Mqtt mqtt, PadServerMQTT padServerMQTT, IPadService padService, JSONArray idList) {
        this.mqtt = mqtt;
        this.padServerMQTT = padServerMQTT;
        this.padService = padService;
        this.idList = idList;
    }

    @Override
    public void run() {

        //针对选中的所有的pad
        int index = 0;
        int region = Integer.parseInt(mqtt.getRegion());
        int loop = idList.size() / region;
        int remainder = (idList.size() % region) > 0 ? 1: 0;
        loop = loop + remainder;

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
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
