package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supadata.constant.Config;
import com.supadata.constant.Mqtt;
import com.supadata.pojo.App;
import com.supadata.pojo.Check;
import com.supadata.service.IAppService;
import com.supadata.service.ICheckService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.FileUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.mqtt.PadServerMQTT;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AppController
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/30 21:12
 * @Description:
 */
@RestController
@RequestMapping("/app")
public class AppController {

    private static Logger logger = Logger.getLogger(AppController.class);

    @Autowired
    public IAppService appService;

    @Autowired
    public ICheckService checkService;

    @Autowired
    private Config config;

    @Autowired
    private Mqtt mqtt;

    @Autowired
    private PadServerMQTT padServerMQTT;


    /**
     * 功能描述: 查询发版记录
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/30 21:36
     */
    @RequestMapping("/list")
    public @ResponseBody
    MsgJson AppList(HttpServletRequest request){
        MsgJson msg = new MsgJson(0,"请求成功!");
        String user_id = request.getParameter("user_id");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        if (StringUtils.isEmpty(user_id)) {
            msg.setCode(1);
            msg.setMsg("usre_id为空！");
            return msg;
        }if (StringUtils.isEmpty(page)) {
            msg.setCode(1);
            msg.setMsg("page为空！");
            return msg;
        }if (StringUtils.isEmpty(limit)) {
            msg.setCode(1);
            msg.setMsg("limit为空！");
            return msg;
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        List<App> apps = appService.queryAll();
        PageInfo<App> pageInfo = new PageInfo<>(apps);
        msg.setData(apps);
        msg.setCount(pageInfo.getTotal());
        return msg;
    }

    @RequestMapping("/upload")
    public @ResponseBody
    MsgJson uploadCourseFile(@RequestParam(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"文件上传成功！");
        String user_id = request.getParameter("user_id");
        String version = request.getParameter("version");
        String desc = request.getParameter("desc");
        logger.info("upload:version=" + version + ",desc="+ desc);
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
        String fileName = file.getOriginalFilename();
        String suffix =  fileName.substring(fileName.lastIndexOf("."));//文件后缀名
        fileName = DateUtil.getOutTradeNo() + suffix;
        String loacalPath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
            loacalPath = config.getWINPATH();
        } else {
            loacalPath = config.getLINUXDOCPATH();
        }
        String filePath =  "app" + File.separator;
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
        App app = new App();
        app.setaVersion(version);
        app.setaDesc(desc);
        if(fileName.contains("apk")){
            app.setaType("1");
        }else {
            app.setaType("0");
        }
        app.setaUrl(loacalPath + filePath + fileName);
        app.setUpdateTime(DateUtil.getCurDate());
        app.setSeRemark(fileName);
        int res = appService.add(app);

        if (res > 0) {
            //发送黑屏切换消息
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("event", "update_apk");
            padServerMQTT.publishMessage(mqtt.getSubTopic(), map);
        }
        return msgJson;
    }


    /**
     * 功能描述: 下载安装包
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/20 23:31
     */
    @RequestMapping("/down")
    public ResponseEntity<byte[]> downApp(HttpServletRequest request) throws Exception{
//        String code = request.getParameter("code");
//        if (StringUtils.isEmpty(code)) {
//           return null;
//        }
//        logger.info("App Down, code="+code);
        App app = appService.queryNewest("1");
        // 本地文件路径
        String filePath = "";
        String fileName = app.getSeRemark();
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
            filePath = config.getWINPATH();
        } else {
            filePath = config.getLINUXDOCPATH();
        }
        filePath = filePath + "app" + File.separator + fileName;
        InputStream in = new FileInputStream(new File(filePath));//将该文件加入到输入流之中
        byte[] body=null;
        body=new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
        in.read(body);//读入到输入流里面
        fileName=new String(fileName.getBytes("gbk"),"iso8859-1");//防止中文乱码
        HttpHeaders headers=new HttpHeaders();//设置响应头
        headers.add("Content-Disposition", "attachment;filename="+fileName);
        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
    }

    /**
     * 功能描述: 查询最新版本
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/30 21:36
     */
    @RequestMapping("/check")
    public @ResponseBody
    MsgJson AppCheck(HttpServletRequest request){
        MsgJson msg = new MsgJson(0,"请求成功!");
        String code = request.getParameter("code");
        if (StringUtils.isEmpty(code)) {
           return null;
        }
        logger.info("App Down, code="+code);
        App app = appService.queryNewest("1");
        app.setaUrl(config.getSERVICEURL() + "ips/app/down");
        msg.setData(app);
        return msg;
    }
}
