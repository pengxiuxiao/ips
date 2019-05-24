package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.*;
import com.supadata.service.IPadService;
import com.supadata.service.IRoomService;
import com.supadata.service.IRoomsettingService;
import com.supadata.service.ISettingService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.enums.EventType;
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

    private static Logger logger = Logger.getLogger(CourseController.class);

    @Autowired
    public IRoomService roomService;

    @Autowired
    public ISettingService settingService;

    @Autowired
    public IRoomsettingService roomsettingService;

    @Autowired
    public IPadService padService;

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
        MsgJson msg = new MsgJson(0,"添加成功！");
        String user_id = request.getParameter("user_id");
        String name = request.getParameter("name");
        String ip = request.getParameter("ip");
        String location = request.getParameter("location");
        String remark = request.getParameter("remark");
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("user_id为空！");
            return msg;
        }
        if (StringUtils.isEmpty(name)) {
            msg.setCode(1);
            msg.setMsg("name为空！");
            return msg;
        }
        if (StringUtils.isEmpty(ip)) {
            msg.setCode(1);
            msg.setMsg("ip为空！");
            return msg;
        }
        Room room = new Room();
        room.setrName(name);
        room.setrIp(ip);
        room.setUpdateTime(DateUtil.getCurDate());
        room.setrLocation(location);
        room.setrRemark(remark);
        if (roomService.add(room) != 1){
            msg.setCode(2);
            msg.setMsg("添加失败！");
            return msg;
        }
        logger.info("添加教室成功：name=" + room.getrName() + ",code=" + room.getrIp());
        return msg;
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
        MsgJson msg = new MsgJson(0,"修改成功！");
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");
        String name = request.getParameter("name");
        String ip = request.getParameter("ip");
        String location = request.getParameter("location");

        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("user_id为空！");
            return msg;
        }
        if (StringUtils.isEmpty(room_id)) {
            msg.setCode(1);
            msg.setMsg("room_id为空！");
            return msg;
        }

        Room room = roomService.queryRoomById(Integer.parseInt(room_id));
        if (room == null) {
            msg.setCode(2);
            msg.setMsg("更新失败！");
            return msg;
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
            msg.setCode(2);
            msg.setMsg("更新失败！");
            return msg;
        }
        logger.info("编辑教室成功：name=" + room.getrName() + ",code=" + room.getrIp());
        return msg;
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
        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));
            System.out.println(id);
            res = roomService.deleteRoom(id);
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
        MsgJson msg = new MsgJson(0,"删除成功！");
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");

        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("user_id为空！");
            return msg;
        }
        if (StringUtils.isEmpty(room_id)) {
            msg.setCode(1);
            msg.setMsg("room_id为空！");
            return msg;
        }
        if (roomService.deleteRoom(Integer.parseInt(room_id)) != 1){
            msg.setCode(2);
            msg.setMsg("删除失败！");
            return msg;
        }
        logger.info("删除教室成功：room_id=" + room_id);
        return msg;
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
        MsgJson msg = new MsgJson(0,"查询成功！");


        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("user_id为空！");
            return msg;
        }
        if (limit == null) {
            msg.setCode(1);
            msg.setMsg("limit为空！");
            return msg;
        }
        if (page == null) {
            msg.setCode(1);
            msg.setMsg("page为空！");
            return msg;
        }
        PageHelper.startPage(page,limit);
        List<Room> rooms = roomService.queryRoom(key);
        PageInfo<Room> pageInfo = new PageInfo<Room>(rooms);
        msg.setData(rooms);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }


    /**
     * 功能描述:设置教室显示模块
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/19 15:33
     */
    @RequestMapping("/set")
    public @ResponseBody
    MsgJson setRoomModule (HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String s_module = request.getParameter("s_module");

        logger.info("全部设置显示="+ s_module );
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("usre_id为空！");
        }
        int res = 0;
        List<Room> rooms = roomService.slelectAllRoom();
        for (Room room : rooms) {
            room.setrModule(s_module);
            roomService.updateRoom(room);
            res ++;
        }
        if (res > 0) {
            //发送消息 通知全部pad 修改显示模块
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("event", EventType.getName(s_module));
            padServerMQTT.publishMessage(mqtt.getSubTopic(), map);
            return MsgJson.success("设置成功！");
        }

        return MsgJson.fail("设置失败！");
    }

    /**
     * 功能描述:设置教室显示模块
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/19 15:33
     */
    @RequestMapping("/setone")
    public @ResponseBody
    MsgJson setOneRoomModule (HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String s_module = request.getParameter("s_module");
        String room_id = request.getParameter("room_id");

        logger.info("全部设置显示="+ s_module );
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("usre_id为空！");
        }
        if (StringUtils.isEmpty(room_id)) {
            return MsgJson.fail("room_id！");
        }
        Room room = new Room();
        room.setId(Integer.parseInt(room_id));
        room.setrModule(s_module);
        int res = roomService.updateRoom(room);
        List<Pad> pads = padService.queryByRoomId(Integer.parseInt(room_id));
        for (Pad pad : pads) {
            //发送消息 通知全部pad 修改显示模块
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("event", EventType.getName(s_module));
            padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
        }
        return res > 0 ? MsgJson.success("设置成功！") : MsgJson.fail("设置失败！");

    }
}
