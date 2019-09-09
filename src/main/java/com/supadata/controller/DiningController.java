package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supadata.constant.Config;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.Course;
import com.supadata.pojo.Pad;
import com.supadata.pojo.Room;
import com.supadata.service.*;
import com.supadata.utils.MsgJson;
import com.supadata.utils.StringUtil;
import com.supadata.utils.mqtt.PadServerMQTT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DiningController
 * @Description: 就餐打卡控制器
 * @Auther: pxx
 * @Date: 2019/9/05 19:31
 * @Description:
 */
@RestController
@RequestMapping("/dining")
public class DiningController {

    private static Logger logger = Logger.getLogger(DiningController.class);

    @Autowired
    public ICourseService courseService;

    @Autowired
    public ISeatService seatService;

    @Autowired
    public IStudentCardService studentCardService;

    @Autowired
    public IRoomService roomService;

    @Autowired
    private Config config;

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private PadServerMQTT padServerMQTT;

    @Autowired
    public IPadService padService;

    /**
     * 添加餐厅培训课
     * @param request
     * @return
     */
    @RequestMapping("/add")
    public @ResponseBody
    MsgJson addCourse(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");
        String name = request.getParameter("name");
        String word_size = request.getParameter("word_size");
        String zao_time = request.getParameter("zao_time");
        String wu_time = request.getParameter("wu_time");
        String wan_time = request.getParameter("wan_time");
        logger.info("发布就餐打卡课程:room_id=" + room_id + ",name=" + name + ",zao_time=" + zao_time + ",wu_time=" + wu_time + ",wan_time=" + wan_time + ",word_size=" + word_size);
        if (StringUtils.isEmpty(user_id) || StringUtils.isEmpty(room_id) || StringUtils.isEmpty(name)
                || StringUtils.isEmpty(zao_time) || StringUtils.isEmpty(wu_time) || StringUtils.isEmpty(wan_time)
                || StringUtils.isEmpty(word_size)) {
            return MsgJson.fail("参数包含空值！");
        }
        Room room = roomService.queryRoomById(Integer.parseInt(room_id));
        if (room == null) {
            return MsgJson.fail("教室不存在！");
        }
        Course course = new Course(name,Integer.parseInt(room_id),room.getrName(),0,Integer.parseInt(word_size),1,zao_time,wu_time,wan_time);
        int res = courseService.addCourse(course);

        return MsgJson.success("添加成功！");
    }

    /**
     * 编辑餐厅培训课
     * @param request
     * @return
     */
    @RequestMapping("/edit")
    public @ResponseBody
    MsgJson editCourse(HttpServletRequest request) {
        String id = request.getParameter("nId");
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");
        String name = request.getParameter("name");
        String zao_time = request.getParameter("zao_time");
        String word_size = request.getParameter("word_size");
        String wan_time = request.getParameter("wan_time");
        String wu_time = request.getParameter("wu_time");
        logger.info("编辑就餐打卡课程:room_id=" + room_id + ",name=" + name + ",zao_time=" + zao_time + ",wu_time=" + wu_time + ",wan_time=" + wan_time + ",word_size=" + word_size);
        if (StringUtils.isEmpty(user_id) || StringUtils.isEmpty(room_id) || StringUtils.isEmpty(name)
                || StringUtils.isEmpty(zao_time) || StringUtils.isEmpty(wu_time) || StringUtils.isEmpty(wan_time)
                || StringUtils.isEmpty(word_size)|| StringUtils.isEmpty(id)) {
            return MsgJson.fail("参数包含空值！");
        }
        boolean isUpdate = false;

        Course oCourse = courseService.queryById(Integer.parseInt(id));
        if (!oCourse.getcName().equals(name)) {
            oCourse.setcName(name);
            isUpdate = true;
        }
        if (!oCourse.getZaoTime().equals(zao_time)) {
            oCourse.setZaoTime(zao_time);
            isUpdate = true;
        }
        if (!oCourse.getWuTime().equals(wu_time)) {
            oCourse.setWuTime(wu_time);
            isUpdate = true;
        }
        if (!oCourse.getWanTime().equals(wan_time)) {
            oCourse.setWanTime(wan_time);
            isUpdate = true;
        }
        if (oCourse.getcWordSize() != Integer.parseInt(word_size)) {
            oCourse.setcWordSize(Integer.parseInt(word_size));
            isUpdate = true;
        }
        Room room = roomService.queryRoomById(Integer.parseInt(room_id));
        if (room == null) {
            return MsgJson.fail("教室不存在！");
        }
        if (oCourse.getcRoomId() != Integer.parseInt(room_id)) {

            oCourse.setcRoomId(Integer.parseInt(room_id));
            oCourse.setcRoomName(room.getrName());
            isUpdate = true;
        }
        if (isUpdate) {
            int res = courseService.updateCourse(oCourse);
            room.setrType(2);//标记为餐厅
            res = roomService.updateRoom(room);
        }
        return MsgJson.success("更新成功！");
    }

    /**
     * 功能描述: 获取后台发布的课程
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 15:33
     */
    @RequestMapping("/list")
    public @ResponseBody
    MsgJson listCourse (String user_id, String key, String page, String limit) {
        MsgJson msg = new MsgJson(0,"查询成功！");
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("usre_id为空！");
            return msg;
        }if (page == null) {
            msg.setCode(1);
            msg.setMsg("page为空！");
            return msg;
        }if (limit == null) {
            msg.setCode(1);
            msg.setMsg("limit为空！");
            return msg;
        }
        if (StringUtils.isEmpty(key)) {
            key = "";
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        List<Course> cources = courseService.queryAllCourse(key,1);
        PageInfo<Course> pageInfo = new PageInfo<>(cources);
        msg.setData(cources);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }

    /**
     * 功能描述: 删除发布的课程
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 15:33
     */
    @RequestMapping("/delete")
    public @ResponseBody
    MsgJson deleteCourse (String user_id, Integer course_id) {
        MsgJson msg = new MsgJson(0,"删除成功！");
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("usre_id为空！");
            return msg;
        }if (course_id == null) {
            msg.setCode(1);
            msg.setMsg("course_id为空！");
            return msg;
        }
        logger.info("删除课程:course_id=" + course_id );
        int res = courseService.deleteCourse(course_id);
        if (res != 0) {
            res = studentCardService.deleteByCourseId(course_id);
        }
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
    @RequestMapping(value = "/bdc")
    public MsgJson batchDeleteCourse(String idList, String user_id) {
        if (StringUtils.isEmpty(user_id) || com.github.pagehelper.util.StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }
        logger.info("批量删除课程:idList=" + idList );
        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));
            System.out.println(id);
            res = courseService.deleteCourse(id);
            if (res != 0) {
                res = studentCardService.deleteByCourseId(id);
            }
        }

        if (res > 0) {
            return MsgJson.success( "培训删除成功");
        }
        return MsgJson.success( "培训删除失败");
    }

    /**
     * 功能描述: 课程表模板下载
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 23:31
     */
    @RequestMapping("/efd")
    public ResponseEntity<byte[]> excelFileDownLoad(HttpServletRequest request) throws FileNotFoundException {
        String fileName = "course_seat_cardNo.xlsx";


        String path = ResourceUtils.getURL("classpath:static").getPath()
                + File.separator + "file"+ File.separator  + fileName;


        InputStream in = null;//将该文件加入到输入流之中
        byte[] body=null;
        try {
            in = new FileInputStream(new File(path));

            body=new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
            in.read(body);//读入到输入流里面
            fileName=new String(fileName.getBytes("gbk"),"iso8859-1");//防止中文乱码
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers=new HttpHeaders();//设置响应头
        headers.add("Content-Disposition", "attachment;filename="+fileName);
        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
//
    }


    /**
     * 功能描述:存储文件
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 22:49
     */
    private File storageFile(String fileName, MultipartFile file) {

        String suffix =  fileName.substring(fileName.lastIndexOf("."));//文件后缀名

        fileName = StringUtil.getRandom() + suffix;
        String loacalPath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
            loacalPath = config.getWINPATH();
        } else {
            loacalPath = config.getLINUXDOCPATH();
        }
        String filePath =  "course" + File.separator;
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
        }
        return new File(loacalPath + filePath, fileName);
    }

}
