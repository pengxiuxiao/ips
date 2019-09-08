package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.Notice;
import com.supadata.pojo.Pad;
import com.supadata.pojo.Room;
import com.supadata.service.INoticeService;
import com.supadata.service.IPadService;
import com.supadata.service.IRoomService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.mqtt.PadServerMQTT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RoomController
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/19 18:47
 * @Description:
 */
@RestController
@RequestMapping("/room")
public class RoomController {

    private static Logger logger = Logger.getLogger(RoomController.class);

    @Autowired
    public IRoomService roomService;

    @Autowired
    public IPadService padService;

    @Autowired
    public INoticeService noticeService;

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private PadServerMQTT padServerMQTT;

    /**
     * 功能描述:插入单条教室信息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/19 19:34
     */
    @RequestMapping("/add")
    public @ResponseBody
    MsgJson addRoom(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String ip = request.getParameter("ip");
        String remark = request.getParameter("remark");
        logger.info("添加教室:name=" + name);
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("user_id为空！");
        }
        if (StringUtils.isEmpty(name)) {
            return MsgJson.fail("name为空！");
        }
        if (StringUtils.isEmpty(ip)) {
            return MsgJson.fail("ip为空！");
        }
        Room room = new Room();
        room.setrName(name);
        room.setrIp(ip);
        room.setUpdateTime(DateUtil.getCurDate());
        room.setrLocation(location);
        room.setrRemark(remark);
        if (roomService.add(room) != 1){
            return MsgJson.fail("user_id为空！");
        }
        logger.info("添加教室成功：" + room.toString());
        return MsgJson.success("添加成功！");
    }

    /**
     * 功能描述:编辑教室信息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/19 19:46
     */
    @RequestMapping("/edit")
    public @ResponseBody
    MsgJson editRoom(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");
        String name = request.getParameter("name");
        String ip = request.getParameter("ip");
        String location = request.getParameter("location");

        logger.info("编辑教室:name=" + name);
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("user_id为空！");
        }
        if (StringUtils.isEmpty(room_id)) {
            return MsgJson.fail("room_id为空！");
        }

        Room room = roomService.queryRoomById(Integer.parseInt(room_id));
        String oldCode = room.getrIp();
        if (room == null) {
            return MsgJson.fail("更新失败！");
        }
        if (StringUtils.isNotEmpty(name)) {
            room.setrName(name);
        }
        if (StringUtils.isNotEmpty(ip)) {
            room.setrIp(ip);
        }
        if (StringUtils.isNotEmpty(location)) {
            room.setrLocation(location);
        }
        if (roomService.updateRoom(room) != 1){
            return MsgJson.fail("更新失败！");
        }
        /** 如果code发生变化 则pad需要重新登录 需要将旧的code对应的pad删除*/
        if (!ip.equals(oldCode)) {
            Pad pad = padService.queryByRoomId(Integer.parseInt(room_id));
            if (pad != null) {
                padService.deleteByRoomId(Integer.parseInt(room_id));
                //发送code切换消息
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("event", "room_code");
                map.put("code", ip);
                padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
                logger.info("编辑教室成功:code=" + oldCode + "===>" + ip + ",删除padId = " + pad.getId() + room.toString());
            }
        } else {
            logger.info("编辑教室成功：" + room.toString());
        }
        return MsgJson.success("操作成功！");
    }


    /**
     * 功能描述:批量删除教室列表数据
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/12/11 16:39
     */
    @RequestMapping(value = "/bdr")
    public MsgJson batchDeleteRoom(String idList, String user_id) {
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }
        logger.info("批量删除教室：idList=" + idList);
        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));
            System.out.println(id);
            res = roomService.deleteRoom(id);
            if (res == 1){
                res = padService.deleteByRoomId(id);
            }
        }

        if (res > 0) {
            return MsgJson.success( "视频删除成功");
        }
        return MsgJson.success( "视频删除失败");
    }


    /**
     * 功能描述:删除教室
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/19 19:50
     */
    @RequestMapping("/delete")
    public @ResponseBody
    MsgJson deleteRoom(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");
        logger.info("删除教室：room_id=" + room_id);
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("room_id为空！");
        }
        if (StringUtils.isEmpty(room_id)) {
            return MsgJson.fail("room_id为空！");
        }
        int res = roomService.deleteRoom(Integer.parseInt(room_id));
        if (res == 1){
            res = padService.deleteByRoomId(Integer.parseInt(room_id));
            return MsgJson.success("操作成功！");
        }
        return MsgJson.fail("删除失败！");
    }

    /**
     * 功能描述:查找教室列表
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/19 19:50
     */
    @RequestMapping("/list")
    public @ResponseBody
    MsgJson listRoom(String user_id, String key, Integer limit, Integer page) {
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("user_id为空！");
        }
        if (limit == null) {
            return MsgJson.fail("limit为空！");
        }
        if (page == null) {
            return MsgJson.fail("page为空！");
        }
        PageHelper.startPage(page,limit);
        List<Room> rooms = roomService.queryRoom(key);
        PageInfo<Room> pageInfo = new PageInfo<Room>(rooms);
        return new MsgJson(0,"操作成功！",rooms,pageInfo.getTotal());
    }


}
