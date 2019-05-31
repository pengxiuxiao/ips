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
 * @ClassName: CourseController
 * @Description: 课程控制器
 * @Auther: pxx
 * @Date: 2018/6/21 19:31
 * @Description:
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    private static Logger logger = Logger.getLogger(CourseController.class);

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

    @RequestMapping("/upload")
    public @ResponseBody
    MsgJson uploadCourseFile(@RequestParam(value = "file", required = false) MultipartFile file,
                         HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"文件上传成功！");
        String user_id = request.getParameter("user_id");
        String room_id = request.getParameter("room_id");
        logger.info("upload:room_id=" + room_id );
        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }
        if (file.getSize() <= 0) {
            msgJson.setCode(1);
            msgJson.setMsg("file为空！");
            return msgJson;
        }
        //存储文件
        File targetFile = storageFile(file.getOriginalFilename(),file);
        //读取文件
        // 需要解析的Excel文件
        try {
            // 创建Excel，读取文件内容
            Workbook workbook = WorkbookFactory.create(FileUtils.openInputStream(targetFile));
            Room room = roomService.queryRoomById(Integer.parseInt(room_id));
            MsgJson res = courseService.handleCourseExcel(workbook, room);
            if (res.getCode() == 1) {
                return res;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return msgJson;
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
//        PageHelper.startPage(page, limit);
        List<Course> cources = courseService.queryAllCourse(key);
        PageInfo<Course> pageInfo = new PageInfo<>(cources);
        msg.setData(cources);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }

    /**
     * 功能描述: 获取后台发布的课程
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
        logger.info("delete:course_id=" + course_id );
        int res = courseService.deleteCourse(course_id);
        if (res != 1) {
            msg.setCode(1);
            msg.setMsg("删除失败！");
            return msg;
        }

        return msg;
    }

    /**
     * 功能描述: 编辑课程
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 15:33
     */
    @RequestMapping("/edit")
    public @ResponseBody
    MsgJson editCourse (HttpServletRequest request) {
        MsgJson msg = new MsgJson(0,"更新成功！");
        String course_id = request.getParameter("course_id");
        String user_id = request.getParameter("user_id");
        String name = request.getParameter("name");
        String word_size = request.getParameter("word_size");
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("usre_id为空！");
            return msg;
        }
        if (StringUtils.isEmpty(course_id)) {
            msg.setCode(1);
            msg.setMsg("course_id为空！");
            return msg;
        }
        Course oldCourse = courseService.queryById(Integer.parseInt(course_id));
        Course course = new Course();
        course.setId(Integer.parseInt(course_id));
        boolean isUpdate = false;
        if (StringUtils.isNotEmpty(name) && !name.equals(oldCourse.getcName())) {
            course.setcName(name);
            isUpdate = true;
        }

        if (StringUtils.isNotEmpty(word_size) && !word_size.equals(oldCourse.getcWordSize())) {
            course.setcWordSize(Integer.parseInt(word_size));
            isUpdate = true;
        }
        if (isUpdate) {
            int res = courseService.editById(course);
            if (res != 1) {
                msg.setCode(1);
                msg.setMsg("删除失败！");
                return msg;
            }else {
                List<Pad> pads = padService.queryByRoomId(oldCourse.getcRoomId());
                for (Pad pad : pads) {
                    //发送消息 通知对应的pad 修改课程字体
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("event", "textSize");
                    map.put("size", course.getcWordSize());
                    padServerMQTT.publishMessage(mqtt.getSubTopic() + "/" + pad.getClientId(), map);
                }
            }
        }

        return msg;
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
