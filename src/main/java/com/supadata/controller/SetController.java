package com.supadata.controller;

import com.supadata.constant.LRUCache;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.Check;
import com.supadata.pojo.RoomSetting;
import com.supadata.pojo.Setting;
import com.supadata.service.ICheckService;
import com.supadata.service.IRoomsettingService;
import com.supadata.service.ISettingService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.enums.EventType;
import com.supadata.utils.mqtt.PadServerMQTT;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public ISettingService settingService;

    @Autowired
    public ICheckService checkService;

    @Autowired
    public IRoomsettingService roomsettingService;

    @Autowired
    private LRUCache lruCache;

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private PadServerMQTT padServerMQTT;

    @RequestMapping("/set")
    public @ResponseBody
    MsgJson setting (HttpServletRequest request) {
        MsgJson msg = new MsgJson(0,"设置成功！");
        String user_id = request.getParameter("user_id");
        /**开机时间*/
        String start_time = request.getParameter("start_time");
        /**关机时间*/
        String end_time = request.getParameter("end_time");
//        String display_date = request.getParameter("display_date");
        /**刷卡显示座位*/
        String display_card = request.getParameter("display_card");
//        String display_daojishi = request.getParameter("display_daojishi");
        /**音量*/
        String daojishi = request.getParameter("daojishi");
        /**锁屏设置*/
        String word_font = request.getParameter("word_font");
//        String word_size = request.getParameter("word_size");
//        String word_color = request.getParameter("word_color");
//        String seRemark = request.getParameter("seRemark");
//        String s_module = request.getParameter("s_module");
//        String rotation_time = request.getParameter("rotation_time");
        String room_id = request.getParameter("room_id");

        logger.info("upload:start_time=" + start_time + ",end_time="+ end_time
                + ",word_font="+ word_font + ",display_daojishi="+  ",daojishi="+ daojishi
               + ",room_id="+ room_id);
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("usre_id为空！");
            return msg;
        }
        Setting setting = new Setting();
        if (StringUtils.isNotEmpty(start_time)) {//开机时间
            setting.setsStartTime(DateUtil.changeDateByStr(start_time));
        }
        if (StringUtils.isNotEmpty(end_time)) {//关机时间
            setting.setsEndTime(DateUtil.changeDateByStr(end_time));
        }
//        if (StringUtils.isNotEmpty(display_date) && "true".equals(display_date)) {//是否显示日期，0显示，1不显示
//            setting.setDisplayDate("0");
//        }
        if (StringUtils.isNotEmpty(display_card) && "true".equals(display_card)) {//是否显示刷卡提示
            setting.setDisplayCard("0");
        }
//        if (StringUtils.isNotEmpty(display_daojishi) && "true".equals(display_daojishi)) {//是否显示倒计时提示
//            setting.setDisplayDaojishi("0");
//        }
//        if (StringUtils.isNotEmpty(seRemark) && "true".equals(seRemark)) {//是否静音
//            setting.setSeRemark("0");
//        }else{
//            setting.setSeRemark("1");
//        }
        if (StringUtils.isNotEmpty(daojishi)) {//倒计时时长
            setting.setDaojishi((Integer.parseInt(daojishi)/10) + "");
        }

//        if (StringUtils.isNotEmpty(word_size)) {//字体大小
//            setting.setWordSize(word_size);
//        }
        if (StringUtils.isNotEmpty(word_font)) {//字体---改为锁屏用
            if ("true".equals(word_font)){
                setting.setWordFont("0");//锁
            }else{
                setting.setWordFont("1");//开
            }
        }
//        if (StringUtils.isNotEmpty(word_color)) {//字体颜色
//            setting.setWordColor(word_color);
//        }
//        if (StringUtils.isNotEmpty(rotation_time)) {//轮播时间
//            setting.setRotationTime(rotation_time);
//        }
//        if (StringUtils.isNotEmpty(s_module)) {//轮播模块
//            setting.setsModule(s_module);
//        }


        setting.setUpdateTime(DateUtil.getCurDate());

        //查询上一条设置
        Setting newstSet = settingService.querySetting();
        if (newstSet == null) {
            newstSet.setsModule("1");
            newstSet.setDaojishi("5");
            newstSet.setWordFont("1");
        }

        setting.setsModule(newstSet.getsModule());
        int res = settingService.add(setting);
        if (res > 0) {
            RoomSetting rs = new RoomSetting();
            rs.setRoomId(Integer.parseInt(room_id));
            rs.setSetId(setting.getId());
            rs.setUpdateTime(DateUtil.getCurDate());
            res = roomsettingService.insertSelective(rs);
        }
        if (res > 0) {

            //发送消息 通知全部pad 修改显示模块
            Map<String, Object> map = new LinkedHashMap<>();
            if (!newstSet.getDaojishi().equals(setting.getDaojishi())) {//音量变更
                map.put("event", "audio");
                map.put("value", setting.getDaojishi()+"0");
            }
            if (!newstSet.getWordFont().equals(setting.getWordFont())) {//锁屏变更
                map.clear();
                map.put("event", "navigation");
                map.put("state", "0".equals(setting.getWordFont()) ? "close" : "open");
            }
            if (!newstSet.getDisplayCard().equals(setting.getDisplayCard())) {//打卡提示变更
                map.clear();
                map.put("event", "cardNotice");
                map.put("isShow", "0".equals(setting.getWordFont()) ? true : false);
            }

            padServerMQTT.publishMessage(mqtt.getSubTopic(), map);
        }
        return msg;
    }


}
