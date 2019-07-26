package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.supadata.constant.Config;
import com.supadata.constant.LRUCache;
import com.supadata.constant.LRUDATACache;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.*;
import com.supadata.service.*;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.SessionMapUtil;
import com.supadata.utils.enums.EventType;
import com.supadata.utils.mqtt.PadServerMQTT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: PadController
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/10 23:12
 * @Description:
 */
@RestController
@RequestMapping(value = "/pad")
public class PadController {

    private static Logger logger = Logger.getLogger(PadController.class);

    @Autowired
    public IPadService padService;

    @Autowired
    public IRoomService roomService;

    @Autowired
    public ISettingService settingService;

    @Autowired
    public INoticeService noticeService;

    @Autowired
    public ICourseService courseService;

    @Autowired
    public ISeatService seatService;

    @Autowired
    public IMImgService mImgService;

    @Autowired
    private Config config;
    /**
     *构建内存缓存对象
     */
    @Autowired
    private LRUCache lruCache;

    @Autowired
    private LRUDATACache lrudataCache;

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private PadServerMQTT padServerMQTT;

    /**
     * 功能描述:
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/11 9:09
     */
    @RequestMapping("/login")
    public @ResponseBody
    MsgJson login(String code, String clientId) {
        logger.info("App登录:code=" + code + ",clientId = " + clientId);
        if (StringUtils.isEmpty(code)) {
            return new MsgJson(1, "code为空！");
        }
        if (StringUtils.isEmpty(clientId)) {
            return new MsgJson(1, "clientId为空！");
        }
        Room room = roomService.queryRoomByIp(code);
        //通过code查找pad数据，有则返回，无则添加
        Pad pad = padService.queryByCode(code);
        if (pad == null) {
            if (room == null) {
                return new MsgJson(1, "code有误或未录入教室数据！");
            }
            pad = new Pad();
            pad.setCode(code);
            pad.setpIp(code);
            pad.setRoomId(room.getId());
            pad.setRoomName(room.getrName());
            pad.setClientId(clientId);
            int res = padService.add(pad);
        }
        if (StringUtils.isEmpty(pad.getClientId()) || !clientId.equals(pad.getClientId())) {
            pad.setClientId(clientId);
            int res = padService.update(pad);
        }
        lruCache.put(code, pad.getRoomId());
        System.out.println(lruCache);

        //查询后台该pad的设置信息 返回其要执行的事件
        Map<String, Long> map = DateUtil.handleOpenClosePadTime(DateUtil.dateToLong(DateUtil.changeToDate(pad.getStartTime(), "yyyy-MM-dd HH:mm:ss")),
                DateUtil.dateToLong(DateUtil.changeToDate(pad.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
        SystemInfo si = new SystemInfo(System.currentTimeMillis(), pad.getCode(),
                pad.getRoomId().toString(), pad.getRoomName(),
                //显示模块
                EventType.getName(room.getrModule()),
                //锁屏
                "关闭".equals(pad.getpState()) ? "close" : "open",
                //黑屏 0:黑， 1：不黑
                "0".equals(pad.getIsBlack()) ? "close" : "open",
                //音量
                (pad.getpAudio() * 10) + "",
                //开机时间
                map.get("open"),
                //关机shijain
                map.get("close"),
                //是否显示打卡提示
                "显示".equals(pad.getpClickCard()) ? true : false
        );

        return new MsgJson(0, "登录成功！", si);
    }


    /**
     * 功能描述:
     *
     * @auther: pxx
     * @param: [name, pwd]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/22 15:48
     */
    @RequestMapping("/pclogin")
    public @ResponseBody
    MsgJson pcLogin(String name, String pwd) {
        MsgJson msgJson = new MsgJson(0, "登录成功！");
        logger.info("PC登录:name=" + name + ",pwd=" + pwd);
        if (StringUtils.isEmpty(name)) {
            msgJson.setCode(1);
            msgJson.setMsg("用户名为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(pwd)) {
            msgJson.setCode(1);
            msgJson.setMsg("密码为空！");
            return msgJson;
        }
        if (!"admin".equals(name) || !"admin".equals(pwd)) {
            msgJson.setCode(1);
            msgJson.setMsg("用户密码错误！");
            return msgJson;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("user_id", 1007);
        msgJson.setData(map);
        return msgJson;
    }

    /**
     * 功能描述:查询设置接口
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 19:24
     */
    @RequestMapping("/setting")
    public @ResponseBody
    MsgJson setting(String code) {
        MsgJson msg = new MsgJson(0, "查询成功！");
        logger.info("setting:code=" + code);
        if (StringUtils.isEmpty(code)) {
            msg.setCode(1);
            msg.setMsg("code为空！");
            return msg;
        }
        Setting setting = settingService.querySetting();
        msg.setData(setting);
        return msg;
    }

    /**
     * 置为黑屏接口
     * @param id
     * @return
     */
    @RequestMapping("/closePad")
    public @ResponseBody
    MsgJson closePad(String id) {
        MsgJson msg = new MsgJson(0, "查询成功！");
        logger.info("closePad:id=" + id);
        if (StringUtils.isEmpty(id)) {
            msg.setCode(1);
            msg.setMsg("code为空！");
            return msg;
        }
        Pad pad = padService.queryById(Integer.parseInt(id));
        if (pad == null) {
            msg.setCode(1);
            msg.setMsg("请求失败！");
            return msg;
        }
        String state = "close";
        if (StringUtils.isEmpty(pad.getIsBlack()) || pad.getIsBlack().equals("1")) {//未黑屏
            pad.setIsBlack("0");
        } else {//已黑屏
            pad.setIsBlack("1");
            state = "open";
        }
        int res = padService.update(pad);
        if (res > 0) {
            //发送黑屏切换消息
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("event", "power");
            map.put("state", state);
            padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
        }

        return msg;
    }

    /**
     * 置为黑屏接口
     * @param idList
     * @return
     */
    @RequestMapping("/bclosePad")
    public @ResponseBody
    MsgJson batchclosePad(String idList, String user_id) {
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }
        logger.info("batchclosePad:idList=" + idList);
        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));

            Pad pad = padService.queryById(id);
            if (pad == null) {
                return MsgJson.fail("请求失败.");
            }
            String state = "close";
            Integer audio = 0;
            if (StringUtils.isEmpty(pad.getIsBlack()) || pad.getIsBlack().equals("1")) {//未黑屏
                pad.setIsBlack("0");
                audio = 0;
            } else {//已黑屏
                pad.setIsBlack("1");
                state = "open";
                audio = pad.getpAudio();
            }
            res = padService.update(pad);
            if (res > 0) {
                //发送黑屏切换消息
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("event", "power");
                map.put("state", state);
                map.put("audio", audio);
                padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
            }
        }
        return MsgJson.success("请求成功.");
    }

    /**
     * 功能描述:查看是否锁屏
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/12 19:24
     */
    @RequestMapping("/checkPad")
    public @ResponseBody
    MsgJson checkPad(String code) {
        MsgJson msg = new MsgJson(0, "查询成功！");
        logger.info("closePad:id=" + code);
        if (StringUtils.isEmpty(code)) {
            msg.setCode(1);
            msg.setMsg("code为空！");
            return msg;
        }
        Pad pad = padService.queryByCode(code);
        if (pad == null) {
            msg.setCode(1);
            msg.setMsg("请求失败！");
            return msg;
        }
        msg.setData(pad);
        return msg;
    }

    /**
     * 功能描述: 获取后台发布的文字消息列表
     *
     * @auther: pxx
     * @param: [code, type]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/12 21:30
     */
    @RequestMapping("/notice")
    public @ResponseBody
    MsgJson notice(String code, String type) {
        MsgJson msg = new MsgJson(0, "查询消息成功！");
        logger.info("notice:code=" + code);
        if (StringUtils.isEmpty(code)) {
            return new MsgJson(1,"code为空！");
        }
        if (StringUtils.isEmpty(type)) {
            return new MsgJson(1,"type为空！");
        }

        if (!getPadIdByCode(code)) {
            return new MsgJson(1,"code错误!");
        }
        if ("5".equals(type)) {
            Course cources = courseService.queryCourseByRoomId(lruCache.get(code));
            msg.setData(cources);
        } else {
            List<Notice> notices = noticeService.queryPushNoticeByRoomId("2", type, lruCache.get(code));
            msg.setData(notices);
        }
        return msg;
    }

    /**
     * 功能描述: 获取后台发布的课程，取课程结束时间在当前时间之前的课程
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 15:33
     */
    @RequestMapping("/course")
    public @ResponseBody
    MsgJson course(String code) {
        MsgJson msg = new MsgJson(0, "查询成功！");
        logger.info("course:code=" + code);
        if (StringUtils.isEmpty(code)) {
            msg.setCode(1);
            msg.setMsg("code为空！");
            return msg;
        }
        if (!getPadIdByCode(code)) {
            return new MsgJson(1,"code错误!");
        }
        Course cources = courseService.queryCourseByRoomId(lruCache.get(code));
        msg.setData(cources);
        return msg;
    }


    /**
     * 功能描述: 打卡查看座位号
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 15:43
     */
    @RequestMapping("/click")
    public @ResponseBody
    MsgJson click(String code, String card_number, Integer room_id) {
        MsgJson msg = new MsgJson(0, "查询成功！");
        logger.info("click:code=" + code + ",card_number=" + card_number + ",room_id" + room_id);
        if (StringUtils.isEmpty(code)) {
            msg.setCode(1);
            msg.setMsg("code为空！");
            return msg;
        }
        if (StringUtils.isEmpty(card_number)) {
            msg.setCode(1);
            msg.setMsg("card_number为空！");
            return msg;
        }
        if (room_id == null) {
            msg.setCode(1);
            msg.setMsg("room_id为空！");
            return msg;
        }
        //通过考勤卡找到人或者要上的课程，取最近的或者正在上的课程，只取一条,先查当前教室有无课程，有则取当前教室，无则查询其他教室
        //先根据卡号查当前教室的课程
        Course course = courseService.queryStudentComingCourse(card_number, room_id, DateUtil.getCurrentDateTime());
        //根据课程id查询座次表信息
        if (course == null) {
            msg.setCode(1);
            msg.setMsg("暂未查到您的培训课！");
            return msg;
        }
        Seat seat = seatService.queryByCNoAndRoomId(card_number, course.getId(), course.getcRoomId());
        if (seat == null) {
            msg.setCode(1);
            msg.setMsg("暂未查到您的培训课！");
        }
        seat.setcTitle(course.getcName());
        msg.setData(seat);
        return msg;
    }


    /**
     * 功能描述: 获取pad列表,并且判断在线状态
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 15:33
     */
    @RequestMapping("/list")
    public @ResponseBody
    MsgJson listPad(String user_id, String key, String page, String limit, String status) {
        MsgJson msg = new MsgJson(0, "查询成功！");
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("usre_id为空！");
            return msg;
        }
        if (page == null) {
            msg.setCode(1);
            msg.setMsg("page为空！");
            return msg;
        }
        if (limit == null) {
            msg.setCode(1);
            msg.setMsg("limit为空！");
            return msg;
        }
        if (StringUtils.isEmpty(key)) {
            key = "";
        }

        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        List<Pad> pads = padService.queryAll(key);

        /**status不传时 判断在线状态*/
        if (StringUtils.isEmpty(status)) {
            for (Pad pad : pads) {
                SessionMapUtil.SESSION_MAP.put(pad.getCode(),"离线");

                //发送消息 询问是否在线
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("event", "onoffline");
                padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);

            }
            //休眠，重新取一下
            try {
                Thread.sleep(1500);
                for (Pad pad : pads) {
                    pad.setpStatus( SessionMapUtil.SESSION_MAP.get(pad.getCode()));
                    pad.setUpdateTime(DateUtil.getCurDate());
                    if (StringUtils.isEmpty(pad.getIsBlack()) || pad.getIsBlack().equals("0")) {
                        pad.setIsBlack("已黑屏");
                    } else {
                        pad.setIsBlack("亮屏");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        PageInfo<Pad> pageInfo = new PageInfo<>(pads);
        msg.setData(pads);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }

    /**
     * 功能描述:获取pad监控画面
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/26 11:40
     */
    @RequestMapping("/monitor")
    public @ResponseBody
    MsgJson monitorPad(String user_id, String code) {

        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("usre_id为空！");
        }
        if (StringUtils.isEmpty(code)) {
            return MsgJson.fail("code为空！");
        }
        Pad pad = padService.queryByCode(code);
        String appendTopic = "";
        if (pad != null) {
            appendTopic = "/" + pad.getClientId();
        }
        //发送消息到指定pad 通知上传图片消息
        Map<String, Object> lmap = new LinkedHashMap<>();
        lmap.put("event", "remote_observe");
        lmap.put("photoName", code + ".png");
        padServerMQTT.publishMessage(mqtt.getSubTopic() + appendTopic, lmap);
        /** 循环定时查询图片上传完成情况 */
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                MImg mImg = (MImg) lrudataCache.get("mimg-" + code);
                if (mImg != null) {
                    /** 用完就移除掉，避免下次有重复 */
                    lrudataCache.remove("mimg-" + code);
                    logger.info(i + "s请求到, mimg-" + code + ",mImgId=" + mImg.getId());
                    Map<String,String> map = new HashedMap();
                    map.put("url", mImg.getmUrl());
                    map.put("pModule", pad.getpModule());
                    return MsgJson.success(map, "请求成功！");
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return MsgJson.fail("请求失败！");

    }

    /**
     * 功能描述:上传监控图片
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/26 11:40
     */
    @RequestMapping("/mimg")
    public @ResponseBody
    MsgJson monitorImg(@RequestParam(value = "file", required = false) MultipartFile file,
                       HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"文件上传成功！");
        String code = request.getParameter("code");
        if (StringUtils.isEmpty(code)) {
            msgJson.setCode(1);
            msgJson.setMsg("code为空！");
            return msgJson;
        }
        String fileName = file.getOriginalFilename();//文件名
        String suffix =  fileName.substring(fileName.lastIndexOf("."));//文件后缀名

        fileName = code + suffix;
        logger.info("mimg:code=" + code + ",fileName=" + fileName);
        String loacalPath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
//            loacalPath = FileUtil.getProperValue("WINPATH");
            loacalPath =  config.getWINPATH();

        } else {
//            loacalPath = FileUtil.getProperValue("LINUXDOCPATH");
            loacalPath = config.getLINUXDOCPATH();
        }
        String filePath =  "monitor" + File.separator;
        File targetFile = new File(loacalPath + filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        // 保存
        try {
            //输入流
            InputStream in = file.getInputStream();
            //输出流
            FileOutputStream out = new FileOutputStream(loacalPath + filePath + fileName);
            //创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) > 0){
                out.write(buffer,0, len);
            }
            //关闭流
            in.close();
            out.close();
            if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != 0) {
                Runtime.getRuntime().exec("chmod -R 777 " + loacalPath + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msgJson.setCode(2);
            msgJson.setMsg("文件上传失败！");
            return msgJson;
        }
        if (!getPadIdByCode(code)) {
            return new MsgJson(1,"code错误!");
        }
        MImg mImg = new MImg(lruCache.get(code), config.getSERVICEURL() + "notice/fileDownLoad?name=" + fileName + "&type=1", DateUtil.getCurDate());
        int res = mImgService.insertSelective(mImg);
        if (res > 0) {
            //存入图片信息
            lrudataCache.put("mimg-" + code, mImg);
            logger.info("mimg-" + code + ",mImgId=" + mImg.getId());
        }

        msgJson.setData(mImg);
        return msgJson;

    }

    /**
     * 从 lruCache获取code 未存时添加进去
     * @param code
     * @return
     */
    public boolean getPadIdByCode(String code){
        if (lruCache.get(code) == null) {
            Pad pad = padService.queryByCode(code);
            if(pad == null){
                return false;
            }
            lruCache.put(code,pad.getRoomId());
        }
        return true;
    }




    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        Date close  = null;
        Date open  = null;
        //这里会有一个异常，所以要用try catch捕获异常
        try {
            close  = sdf.parse("2019-05-26");
            open  = sdf.parse("2019-05-27");
        }catch (Exception e){
            e.printStackTrace();
        }

        Map map = DateUtil.handleOpenClosePadTime(DateUtil.dateToLong(open),DateUtil.dateToLong(close));
        System.out.println(map);



        Map<Integer, String> maps = new HashedMap();
        maps.put(1,"A");
        System.out.println(maps.get(1));
        System.out.println(maps.get(Integer.valueOf(1)));
        System.out.println(maps.get(1L));

    }
}
