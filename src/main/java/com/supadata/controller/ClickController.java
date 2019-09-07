package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supadata.pojo.Click;
import com.supadata.service.IClickService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.ExcelUtil;
import com.supadata.utils.MsgJson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
    MsgJson lisyClick(String user_id, String key, String course_id, String page, String limit) {
        if (StringUtils.isEmpty(user_id) || page == null || limit == null) {
            return MsgJson.fail("参数包含空值！");
        }
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isEmpty(key)) {
            key = "";
        }
        map.put("key",key);
        map.put("course_id",course_id);
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        List<Click> clicks = clickService.queryAllClick(map);
        PageInfo<Click> pageInfo = new PageInfo<>(clicks);
        MsgJson msg = new MsgJson(0,"请求成功！");
        msg.setData(clicks);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }

    /**
     * 功能描述:导出打卡记录列表信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/export")
    public void exportClicks(String user_id,  String key, HttpServletResponse response){
        if (StringUtils.isEmpty(user_id)) {
            return;
        }
        if (StringUtils.isEmpty(key)) {
            key = "";
        }
        Map<String,String> map = new HashMap<>();
        map.put("key",key);
        List<Click> clicks = clickService.queryAllClick(map);
        String sheetName = "签到表";
        String fileName = "签到记录表-" + DateUtil.getTimestamp();


        int columnNumber = 6;
        int[] columnWidth = {5, 30, 20, 20, 10, 20,};
        String[] columnName = {"id", "课程名", "学员姓名", "卡号", "签到区间", "打卡时间"};

        try {
            ExcelUtil.ExportClicksWithResponse(sheetName, fileName, columnNumber, columnWidth, columnName, clicks, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
