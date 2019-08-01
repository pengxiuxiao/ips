package com.supadata.controller;

import com.github.pagehelper.util.StringUtil;
import com.supadata.constant.IntervalType;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.Notice;
import com.supadata.pojo.Pad;
import com.supadata.service.INoticeService;
import com.supadata.service.IPadService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.enums.EventType;
import com.supadata.utils.mqtt.PadServerMQTT;
import com.supadata.utils.thread.MQSendThread;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName: SetController
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/26 19:46
 * @Description:
 */
@RestController
@RequestMapping("/set")
public class SetController {

    private static Logger logger = Logger.getLogger(CourseController.class);

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private PadServerMQTT padServerMQTT;

    @Autowired
    public IPadService padService;

    @Autowired
    public INoticeService noticeService;

    /**
     * 功能描述:批量设置
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/7/24 16:24
     */
    @RequestMapping("/bscard")
    public @ResponseBody
    MsgJson batchSetCard(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String idList = request.getParameter("idList");
        String status = request.getParameter("status");//1：不显示，0显示
        String state = request.getParameter("state");
        String audio = request.getParameter("audio");
        String end_time = request.getParameter("end_time");
        String start_time = request.getParameter("start_time");
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }

        logger.info("批量设置 step1:idList=" + idList);

        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            Map<String, Object> map = new LinkedHashMap<>();
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));
            Pad pad = padService.queryById(id);
            if (pad == null) {
                return MsgJson.fail("请求失败");
            }
            Pad tmpPad = new Pad();
            tmpPad.setId(id);
            if (StringUtils.isNotEmpty(status)) {
                logger.info("批量设置-打卡提示 step2:status=" + status);
                tmpPad.setpClickCard(status);
                res = padService.update(tmpPad);
                if (res > 0) {
                    //发送打卡提示切换消息
                    map.put("event", "cardNotice");
                    map.put("isShow", "0".equals(status) ? true : false);
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
                }
            } else if (StringUtils.isNotEmpty(state)) {
                logger.info("批量设置-显示锁屏 step2:state=" + state);
                tmpPad.setpState(state);
                res = padService.update(tmpPad);
                if (res > 0) {
                    //发送锁屏消息
                    map.put("event", "navigation");
                    map.put("state", "1".equals(state) ? "close" : "open");
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
                }
            }else if (StringUtils.isNotEmpty(audio)) {
                logger.info("批量设置-音量 step2:audio=" + audio);
                tmpPad.setpAudio(Integer.parseInt(audio));
                res = padService.update(tmpPad);
                if (res > 0) {
                    //发送音量消息
                    map.put("event", "audio");
                    map.put("value", audio);
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
                }
            }else if (StringUtils.isNotEmpty(start_time) && StringUtils.isNotEmpty(end_time) ) {
                logger.info("批量设置-开关机时间 step2:start_time=" + start_time + ",end_time" + end_time);
                Date sDate = DateUtil.changeToDate(start_time, "yyyy-MM-dd HH:mm:ss");
                Date eDate = DateUtil.changeToDate(end_time, "yyyy-MM-dd HH:mm:ss");
                Map<String, Long> dateMap = DateUtil.handleOpenClosePadTime(
                        DateUtil.dateToLong(sDate),
                        DateUtil.dateToLong(eDate));
                if (dateMap.get("open").equals(0L)) {
                    return MsgJson.fail("时间差不得小于2分钟！");
                }
                tmpPad.setEndTime(end_time);
                tmpPad.setStartTime(start_time);
                res = padService.update(tmpPad);
                if (res > 0) {
                    //发送关机消息
                    map.put("event", "onoff");
                    map.put("wakeTime", dateMap.get("open"));
                    map.put("sleepTime", dateMap.get("close"));
                    logger.info("开关机=code：" + pad.getCode() + "，set: sleepTime=" + end_time + ", wakeTime " + start_time);
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
                }

            }
        }
        return MsgJson.success("请求成功.");
    }


 /**
     * 功能描述:批量设置视频显示
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/7/24 16:24
     */
    @RequestMapping("/bsv")
    public @ResponseBody
    MsgJson batchSetVideo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String idList = request.getParameter("idList");
        String module = request.getParameter("modules");
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值。");
        }
        Notice notice = noticeService.queryMaxVideo();
        logger.info("批量设置-显示视频 step1:idList=" + idList);

        JSONArray idArry = JSONArray.fromObject(idList);
        Integer interval = 0;
        int region = Integer.parseInt(mqtt.getRegion());
        int loop = idArry.size() / region;
        int remainder = (idArry.size() % region) > 0 ? 1: 0;
        loop = loop + remainder;
        interval = IntervalType.getInterval(notice.getFileSize());
        int seconds = interval/1000*(loop == 0 ? 1 : loop);
        logger.info("批量设置-显示视频 step2:module=" + module);
        if ("3".equals(module)) {
            MQSendThread mqThread = new MQSendThread(mqtt, padServerMQTT, padService, idArry, interval, loop);
            mqThread.start();
            return MsgJson.success("视频下载约需要" + seconds/60 + "分" + seconds%60 + "秒，在此期间请勿切换显示模块！");
        } else {
            int res = 0;
            for (Object idData : idArry) {
                Map<String, Object> map = new LinkedHashMap<>();
                JSONObject idObj = JSONObject.fromObject(idData);
                Integer id = Integer.valueOf(idObj.getString("id"));
                Pad tmpPad = new Pad();
                tmpPad.setId(id);
                tmpPad.setpModule(module);
                res = padService.update(tmpPad);
                if (res > 0) {
                    //发送消息 通知pad 修改显示模块
                    map.put("event", EventType.getName(module));
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + idObj.getString("clientId"), map);
                }
            }
            return MsgJson.success("操作成功！");
        }
    }



}
