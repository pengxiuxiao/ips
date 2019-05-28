package com.supadata.mq;

import com.supadata.constant.PadCache;
import com.supadata.service.IPadService;
import com.supadata.utils.SessionMapUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: PushCallback
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/2 16:19
 * @Description:
 */

public class PushCallback implements MqttCallback {

    private static Logger logger = Logger.getLogger(PushCallback.class);

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));


        try {
            String content = new String(message.getPayload());
            if (!"close".equals(content)) {
                JSONObject j = JSONObject.fromObject(content);
                String event = j.getString("event");
                System.out.println("event=========" + event);
                String mac = j.getString("mac");
                System.out.println("mac=========" + mac );
                String code = j.getString("code");
                System.out.println("code=========" + code);
                SessionMapUtil.SESSION_MAP.put(code, "在线");
                logger.info("设备在线状态：" + code + "===在线");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
