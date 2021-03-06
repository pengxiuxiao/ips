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
import com.supadata.utils.enums.ModuleType;
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
    public INoticeService noticeService;

    @Autowired
    public ICourseService courseService;

    @Autowired
    public ISeatService seatService;

    @Autowired
    public IStudentCardService studentCardService;

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

    @Autowired
    public IClickService clickService;

    /**
     * 功能描述:App登录接口
     *
     * @auther: pxx
     * @param: code clientId
     * @return:
     * @date: 2018/6/11 9:09
     */
    @RequestMapping("/login")
    public @ResponseBody
    MsgJson applogin(String code, String clientId) {
        logger.info("App登录step1:code=" + code + ",clientId = " + clientId);
        if (StringUtils.isEmpty(code)) {
            return MsgJson.fail("code为空！");
        }
        if (StringUtils.isEmpty(clientId)) {
            return MsgJson.fail("clientId为空！");
        }
        Room room = roomService.queryRoomByIp(code);
        logger.info("App登录step2:code=" + code + ",查询到教室roomName = " + room.getrName());
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
            pad.setStartTime("2019-07-02 00:00:00");
            pad.setEndTime("2019-07-02 00:00:00");
            pad.setUpdateTime(new Date());
            pad.setpModule("1");
            pad.setpAudio(0);
            pad.setpState("1");
            pad.setIsBlack("1");
            pad.setpClickCard("1");
            int res = padService.add(pad);
            logger.info("App登录step3:code=" + code + ",注册新的设备PadId = " + pad.getId());
            if (res > 0) {
                // public Notice(String nTitle, String nType, String publishRoom, String publishRoomId, Date updateTime) {
                noticeService.addNotice(new Notice(room.getrName(),"code=" + code + ",ip=" + room.getrLocation(), "1", room.getrName(), room.getId()+"", new Date()));
            }
        }
        logger.info("App登录step3:code=" + code + ",查询到设备PadId = " + pad.getId());
        if (StringUtils.isEmpty(pad.getClientId()) || !clientId.equals(pad.getClientId())) {
            pad.setClientId(clientId);
            int res = padService.update(pad);
        }
        lruCache.put(code, pad.getRoomId());

        //查询后台该pad的设置信息 返回其要执行的事件
        Map<String, Long> map = DateUtil.handleOpenClosePadTime(DateUtil.dateToLong(DateUtil.changeToDate(pad.getStartTime(), "yyyy-MM-dd HH:mm:ss")),
                DateUtil.dateToLong(DateUtil.changeToDate(pad.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
        SystemInfo si = new SystemInfo(System.currentTimeMillis(), pad.getCode(),
                pad.getRoomId().toString(), pad.getRoomName(),
                //显示模块
                EventType.getName(pad.getpModule()),
                //锁屏 navigation
                "0".equals(pad.getpState()) ? "open" : "close",
                //黑屏 0:黑， 1：不黑 power
                "0".equals(pad.getIsBlack()) ? "close" : "open",
                //音量
                (pad.getpAudio()) + "",
                //开机时间
                map.get("open"),
                //关机shijain
                map.get("close"),
                //是否显示打卡提示
                "0".equals(pad.getpClickCard()) ? true : false
        );
        logger.info("App登录step4:code=" + code + ",登录成功，info = " + si.toString());
        return MsgJson.success(si,"登录成功！");
    }


    /**
     * 功能描述:后台登陆接口
     *
     * @auther: pxx
     * @param: [name, pwd]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/22 15:48
     */
    @RequestMapping("/pclogin")
    public @ResponseBody
    MsgJson pcLogin(String name, String pwd) {
        logger.info("PC登录:name=" + name + ",pwd=" + pwd);
        if (StringUtils.isEmpty(name)) {
            return MsgJson.fail("name为空！");
        }
        if (StringUtils.isEmpty(pwd)) {
            return MsgJson.fail("密码为空！");
        }
        Map<String, Integer> map = new HashMap<>();

        if ("admin".equals(name) && "admin".equals(pwd)) {
            map.put("user_id", 1007);
            return MsgJson.success(map,"操作成功！");
        }
        if ("canting".equals(name) && "canting".equals(pwd)) {
            map.put("user_id", 1008);
            return MsgJson.success(map,"操作成功！");
        }

        return MsgJson.success(map,"操作成功！");
    }

    /**
     * 功能描述:置为黑屏接口
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/7/31 10:58
     */
    @RequestMapping("/blackPad")
    public @ResponseBody
    MsgJson blackScreenPad(String id) {
        logger.info("黑屏操作:padId=" + id);
        if (StringUtils.isEmpty(id)) {
            return MsgJson.fail("code为空！");
        }

        Pad pad = padService.queryById(Integer.parseInt(id));
        if (pad == null) {
            return MsgJson.fail("code有误！");
        }

        pad.setIsBlack(pad.getIsBlack().equals("1") ? "0" : "1");
        int res = padService.update(pad);
        if (res > 0) {
            //发送黑屏切换消息
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("event", "power");
            map.put("state", pad.getIsBlack().equals("1") ? "open" : "close");
            padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
        }
        return MsgJson.success("操作成功！");
    }

    /**
     * 功能描述: 批量置为黑屏接口
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/7/31 11:12
     */
    @RequestMapping("/bchblackPad")
    public @ResponseBody
    MsgJson batchBlackScreenPad(String idList, String user_id) {
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }
        logger.info("批量黑屏:idList=" + idList);
        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));

            Pad pad = padService.queryById(id);
            if (pad == null) {
                return MsgJson.fail("请求失败.");
            }

            pad.setIsBlack(pad.getIsBlack().equals("1") ? "0" : "1");
            res = padService.update(pad);
            if (res > 0) {
                //发送黑屏切换消息
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("event", "power");
                map.put("state", pad.getIsBlack().equals("1") ? "open" : "close");
                padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
            }
        }
        return MsgJson.success("请求成功.");
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
    MsgJson getNotice(String code, String type) {
//        logger.info("获取发布信息:code=" + code + ",type=" + type);
        if (StringUtils.isEmpty(code)) {
            return MsgJson.fail("code为空！");
        }
        if (StringUtils.isEmpty(type)) {
            return MsgJson.fail("type为空！");
        }
        if (!getPadIdByCode(code)) {
            return MsgJson.fail("code有误！");
        }
        if ("5".equals(type)) {
            Course cources = courseService.queryCourseByRoomId(lruCache.get(code));
            return MsgJson.success(cources,"操作成功！");
        } else {
            List<Notice> notices = noticeService.queryPushNoticeByRoomId("2", type, lruCache.get(code));
            return MsgJson.success(notices,"操作成功！");
        }
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
    MsgJson clickCard(String code, String card_number, Integer room_id) {
        logger.info("打卡查座次:code=" + code + ",card_number=" + card_number + ",room_id=" + room_id);
        if (StringUtils.isEmpty(code)) {
            return MsgJson.fail("code为空！");
        }
        if (StringUtils.isEmpty(card_number)) {
            return MsgJson.fail("card_number为空！");
        }
        if (room_id == null) {
            return MsgJson.fail("room_id为空！");
        }
        //TODO 先查询教室属性 是否是餐厅
        Room room = roomService.queryRoomById(room_id);
        if (room != null && room.getrType() == 0) {//普通教室
            //通过考勤卡找到人或者要上的课程，取最近的或者正在上的课程，只取一条,先查当前教室有无课程，有则取当前教室，无则查询其他教室
            //先根据卡号查当前教室的课程
            Course course = courseService.queryStudentComingCourse(card_number, room_id, DateUtil.getCurrentDateTime());
            //根据课程id查询座次表信息
            if (course == null) {
                return MsgJson.fail("暂未查到您的培训课！");
            }
            Seat seat = seatService.queryByCNoAndRoomId(card_number, course.getId(), course.getcRoomId());
            if (seat == null) {
                return MsgJson.fail("暂未查到您的培训课！");
            }
            seat.setcTitle(course.getcName());
            seat.setsFlag("0");//培训打卡
            return MsgJson.success(seat,"操作成功！");
        }else if(room != null && room.getrType() == 1) {//餐厅
            Seat seat = new Seat();
            StudentCard studentCard = studentCardService.selectByNumber(card_number);
            if(studentCard == null){
                return MsgJson.fail("未查到卡片信息！");
            }
            if(studentCard.getCourseId() == null){
                return MsgJson.fail("未查到课程信息！");
            }
            Course course = courseService.queryById(studentCard.getCourseId());
            if(course == null){
                return MsgJson.fail("未查到课程！");
            }

            Date curDate = DateUtil.getCurDate();
            String type = DateUtil.judgeTimeType(DateUtil.getCurHms(),course.getZaoTime(),course.getWuTime(),course.getWanTime());
            Click click = new Click(studentCard.getStudentName(),card_number,studentCard.getCourseId(),studentCard.getCourseName(),curDate);

            seat.setcTitle(studentCard.getCourseName());
            seat.setStuName(studentCard.getStudentName());
            seat.setCardNo(card_number);
            seat.setsFlag("1");
            seat.setsCall("学员");
            seat.setsMsg(course.getcType() == 1 ? "祝您用餐愉快" : "签到成功");
            seat.setuPic(studentCard.getScRemark());

            if (!"0".equals(type)) {//无效打卡，不入库
                click.setcType(type);
                //根据区间查询是否已经有记录了
                Map<String, String> map = new HashMap<>();
                map.put("type",type);
                map.put("startDate", DateUtil.getCurrentDate() + " 00:00:01");
                map.put("endDate", DateUtil.getCurrentDate() + " 23:59:59");
                map.put("cardNumber", card_number);
                map.put("courseId", course.getId()+"");
                Click isClick = clickService.selectByTypeAndDate(map);
                if (isClick == null) {
                    clickService.insertSelective(click);
                }
            }
            return MsgJson.success(seat,"操作成功！");
        }else {//上课打卡
            Seat seat = new Seat();
            //直接查当前教室的打卡课程
            Course course = courseService.queryClickCardCourse( room_id,"2");
            //根据课程id查询座次表信息
            if (course == null) {
                return MsgJson.fail("暂未查到您的培训课！");
            }
            StudentCard studentCard = studentCardService.selectByNumber(card_number);
            if(studentCard == null){
                return MsgJson.fail("未查到卡片信息！");
            }
            if(studentCard.getCourseId() == null){
                return MsgJson.fail("未查到课程信息！");
            }
            Date curDate = DateUtil.getCurDate();
            String type = DateUtil.judgeTimeType(DateUtil.getCurHms(),course.getZaoTime(),course.getWuTime(),course.getWanTime());
            Click click = new Click(studentCard.getStudentName(),card_number,course.getId(),course.getcName(),curDate);

            seat.setcTitle(course.getcName());
            seat.setStuName(studentCard.getStudentName());
            seat.setCardNo(card_number);
            seat.setsFlag("1");
            seat.setsCall("学员");
            seat.setsMsg(course.getcType() == 1 ? "祝您用餐愉快" : "签到成功");
            seat.setuPic(studentCard.getScRemark());

            int res = 0;
            if (!"0".equals(type)) {//无效打卡，不入库
                click.setcType(type);
                //根据区间查询是否已经有记录了
                Map<String, String> map = new HashMap<>();
                map.put("type", type);
                map.put("startDate", DateUtil.getCurrentDate() + " 00:00:01");
                map.put("endDate", DateUtil.getCurrentDate() + " 23:59:59");
                map.put("cardNumber", card_number);
                map.put("courseId", course.getId() + "");
                Click isClick = clickService.selectByTypeAndDate(map);
                if (isClick == null) {
                    res = clickService.insertSelective(click);
                }
            } else {
                seat.setsMsg(course.getcType() == 1 ? "祝您用餐愉快" : "未到签到时间");
            }

            return MsgJson.success(seat,"操作成功！");
        }

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
//        logger.info("获取pad列表:user_id=" + user_id + ",key=" + key);
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("usre_id为空！");
        }
        if (page == null) {
            return MsgJson.fail("page为空！");

        }
        if (limit == null) {
            return MsgJson.fail("limit为空！");
        }
        if (StringUtils.isEmpty(key)) {
            key = "";
        }

        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        List<Pad> pads = padService.queryAll(key);

        /**status不传时 判断在线状态*/
        if (StringUtils.isEmpty(status)) {
            for (Pad pad : pads) {
                SessionMapUtil.SESSION_MAP.put(pad.getCode(), "离线");

                //发送消息 询问是否在线
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("event", "onoffline");
                padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);

            }
            //休眠，重新取一下
            try {
                Thread.sleep(1500);
                for (Pad pad : pads) {
                    pad.setpStatus(SessionMapUtil.SESSION_MAP.get(pad.getCode()));
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

        } else {
            for (Pad pad : pads) {
                pad.setpModule(ModuleType.getName(pad.getpModule()));
                if ("0".equals(pad.getpState())) {
                    pad.setpState("打开");
                } else {
                    pad.setpState("关闭");
                }
                if ("0".equals(pad.getpClickCard())) {
                    pad.setpClickCard("显示");
                } else {
                    pad.setpClickCard("关闭");
                }

            }
        }
        PageInfo<Pad> pageInfo = new PageInfo<>(pads);
        return new MsgJson(0,"操作成功！",pads,pageInfo.getTotal());
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

        logger.info("获取监控画面请求:code=" + code);
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
        /** 黑屏 直接返回一个黑图片*/
        if ("0".equals(pad.getIsBlack())) {
            Map<String,String> map = new HashedMap();
            map.put("url", "http://10.69.3.200:8080/ips/img/black.png");
            map.put("pModule", pad.getpModule());
            return MsgJson.success(map, "请求成功！");
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
        String code = request.getParameter("code");
        logger.info("上传监控图片:code=" + code);
        if (StringUtils.isEmpty(code)) {
            return MsgJson.fail("code为空！");
        }
        String fileName = file.getOriginalFilename();//文件名
        String suffix =  fileName.substring(fileName.lastIndexOf("."));//文件后缀名

        fileName = code + suffix;
        logger.info("上传监控图片:code=" + code + ",fileName=" + fileName);
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
            return MsgJson.fail("文件上传失败！");
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
        return MsgJson.success(mImg,"文件上传成功！");

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
