package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.supadata.pojo.Course;
import com.supadata.pojo.StudentCard;
import com.supadata.service.ICourseService;
import com.supadata.service.IStudentCardService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.ExcelUtil;
import com.supadata.utils.MsgJson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CardController
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/18 15:30
 * @Description:
 */
@RestController
@RequestMapping("/card")
public class CardController {

    private static Logger logger = Logger.getLogger(CardController.class);

    @Autowired
    public IStudentCardService studentCardService;

    @Autowired
    public ICourseService courseService;

    /**
     * 功能描述:添加卡片信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/add")
    public MsgJson AddCard(String user_id, String cardNo, String course_id,  String name){
        if (StringUtils.isEmpty(user_id) || StringUtils.isEmpty(cardNo) || StringUtils.isEmpty(name)) {
            return MsgJson.fail("参数包含空值！");
        }

        String secretNo = com.supadata.utils.StringUtil.HexToLongString(com.supadata.utils.StringUtil.overturnHexString(cardNo));
        StudentCard card = studentCardService.selectByNumber(cardNo);
        if (card == null) {
            StudentCard sc = new StudentCard(name, cardNo, secretNo, DateUtil.getCurDate());
            Course course = courseService.queryById(Integer.parseInt(course_id));
            if (course != null) {
                sc.setCourseId(Integer.parseInt(course_id));
                sc.setCourseName(course.getcName());
            }
            int res = 0;
            try {
                res = studentCardService.insertSelective(sc);
                if (res > 0) {
                    logger.info("添加卡片：id=" + sc.getId() + ",name=" + name + ",number=" + cardNo);
                    return MsgJson.success( "卡片添加成功！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return MsgJson.fail("姓名重复!");
            }

        }
        return MsgJson.fail("卡号重复!");
    }

    /**
     * 功能描述:编辑卡片信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/edit")
    public MsgJson editCard(String user_id, Integer id,  String name){
        if (StringUtils.isEmpty(user_id) || id == null || StringUtils.isEmpty(name)) {
            return MsgJson.fail("参数包含空值！");
        }
        StudentCard sc = studentCardService.selectByPrimaryKey(id);
        if (sc == null) {
            return MsgJson.success( "卡片id有误！");
        }
        sc.setStudentName(name);
        sc.setUpdateTime(DateUtil.getCurDate());
        int res = studentCardService.updateByPrimaryKeySelective(sc);
        if(res > 0){
            logger.info("编辑卡片：id=" + sc.getId() + ",name=" + name  + ",number=" + sc.getCardNumber());
            return MsgJson.success( "修改成功!");
        }
        return MsgJson.fail("修改失败!");
    }


    /**
     * 功能描述:删除卡片信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/delete")
    public MsgJson deleteCard(String user_id, Integer id){
        if (StringUtils.isEmpty(user_id) || id == null) {
            return MsgJson.fail("参数包含空值！");
        }
        int sc = studentCardService.deleteByPrimaryKey(id);
        if (sc > 0) {
            logger.info("删除卡片：id=" + id);
            return MsgJson.success( "删除成功!");
        }
        return MsgJson.fail("删除失败!");
    }


    /**
     * 功能描述:批量删除卡片信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/bdc")
    public MsgJson batchDeleteCard(String user_id, String idList){
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }
        JSONArray idArry = JSONArray.fromObject(idList);
        logger.info("批量删除卡片：idList=" + idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));
            System.out.println(id);
            res = studentCardService.deleteByPrimaryKey(id);
        }
        if (res > 0) {
            return MsgJson.success("批量删除成功!");
        }
        return MsgJson.fail("批量删除失败!");
    }

    /**
     * 功能描述:查询卡片列表信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/list")
    public MsgJson listCards(String user_id, Integer page, Integer limit, String number){
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("参数包含空值！");
        }
        Map<String, String> map = new HashedMap();
        if (StringUtils.isNotEmpty(number)) {
            map.put("key", number);
        }
        PageHelper.startPage(page,limit);
        List<StudentCard> cards = studentCardService.selectAllList(map);
        PageInfo<StudentCard> pageInfo = new PageInfo<StudentCard>(cards);
        MsgJson msgJson = MsgJson.success(cards, "查询成功！");
        msgJson.setCount(pageInfo.getTotal());
        return msgJson;

    }
    /**
     * 功能描述:导出卡片列表信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/export")
    public void exportCards(String user_id,  HttpServletResponse response){
        if (StringUtils.isEmpty(user_id)) {
            return;
        }
        List<StudentCard> cards = studentCardService.selectAllList(new HashedMap());
        String sheetName = "付费用户";
        String fileName = "卡片信息表-" + DateUtil.getTimestamp();
//        int columnNumber = 3;
//        int[] columnWidth = {20, 20, 20};
//        String[] columnName = {"姓名", "卡号", "暗码"};

        int columnNumber = 2;
        int[] columnWidth = {20, 20};
        String[] columnName = {"姓名", "卡号"};

        try {
            ExcelUtil.ExportWithResponse(sheetName, fileName, columnNumber, columnWidth, columnName, cards, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
