package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supadata.constant.Config;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.Click;
import com.supadata.pojo.Course;
import com.supadata.pojo.Room;
import com.supadata.service.*;
import com.supadata.utils.MsgJson;
import com.supadata.utils.mqtt.PadServerMQTT;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ClickController
 * @Description:
 * @Author: pxx
 * @Date: 2019/9/6 17:52
 * @Description:
 */

@RestController
@RequestMapping("/click")
public class ClickController {

    private static Logger logger = Logger.getLogger(ClickController.class);

    @Autowired
    public IClickService clickService;

    /**
     * 功能描述: 查询打卡列表
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/9/6 17:59
     */
    @RequestMapping("/list")
    public @ResponseBody
    MsgJson lisyClick(String user_id, String key, String page, String limit) {
        if (StringUtils.isEmpty(user_id) || page == null || limit == null) {
            return MsgJson.fail("参数包含空值！");
        }
        if (StringUtils.isEmpty(key)) {
            key = "";
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        Map<String,String> map = new HashMap<>();
        map.put("key",key);
        List<Click> clicks = clickService.queryAllClick(map);
        PageInfo<Click> pageInfo = new PageInfo<>(clicks);
        MsgJson msg = new MsgJson();
        msg.setData(clicks);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }
}
