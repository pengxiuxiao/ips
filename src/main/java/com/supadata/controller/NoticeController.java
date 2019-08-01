package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.supadata.constant.Config;
import com.supadata.pojo.Notice;
import com.supadata.service.INoticeService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.enums.FileType;
import com.supadata.utils.enums.NoticeType;
import com.supadata.utils.thread.ConverPPTFileToImageUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.List;
import java.util.Map;

/**
 * @ClassName: NoticeController
 * @Description: 信息发布控制器
 * @Auther: pxx
 * @Date: 2018/6/20 11:33
 * @Description:
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    private static Logger logger = Logger.getLogger(NoticeController.class);

    @Autowired
    public INoticeService noticeService;

    @Autowired
    private Config config;

    /**
     * 功能描述:添加文字消息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/20 13:57
     */
    @RequestMapping("/addWord")
    public @ResponseBody
    MsgJson addWordNotice(HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"添加成功！");
        String user_id = request.getParameter("user_id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String room_list = request.getParameter("room_list");
        String wordSize = request.getParameter("word_size");
        logger.info("添加文字消息:title=" + title + ",content=" + content + ",room_list=" + room_list + ",wordSize=" + wordSize);
        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(title)) {
            msgJson.setCode(1);
            msgJson.setMsg("title为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(content)) {
            msgJson.setCode(1);
            msgJson.setMsg("content为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(wordSize)) {
            msgJson.setCode(1);
            msgJson.setMsg("wordSize为空！");
            return msgJson;
        }
        JSONArray roomArry = JSONArray.fromObject(room_list);
        for(int i = 0; i < roomArry.size(); i++){
            JSONObject jsonRoom = roomArry.getJSONObject(i);
            String id = jsonRoom.getString("id");
            String name = jsonRoom.getString("name");

            //String nTitle, String nContent, String nType, String publishRoom, String publishRoomId, Integer nWordSize, Date updateTime
            Notice notice = new Notice(title, content, "1", name, id, Integer.parseInt(wordSize), DateUtil.getCurDate());
            int res = noticeService.addNotice(notice);
            if (res != 1) {
                msgJson.setCode(2);
                msgJson.setMsg("添加失败！");
                return msgJson;
            }
        }
        return msgJson;
    }
    /**
     * 功能描述:编辑文字消息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/20 13:57
     */
    @RequestMapping("/editWord")
    public @ResponseBody
    MsgJson editWordNotice(HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"编辑成功！");
        String user_id = request.getParameter("user_id");
        String notice_id = request.getParameter("notice_id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String wordSize = request.getParameter("word_size");
        logger.info("编辑文字消息:notice_id=" + notice_id + ",title=" + title  + ",content=" + content  + ",wordSize=" + wordSize );
        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(notice_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("notice_id为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(wordSize)) {
            msgJson.setCode(1);
            msgJson.setMsg("notice_id为空！");
            return msgJson;
        }
        Notice notice = noticeService.queryById(Integer.parseInt(notice_id));
        if (StringUtils.isNotEmpty(content)) {
            notice.setnContent(content);
        }
        if (StringUtils.isNotEmpty(title)) {
            notice.setnTitle(title);
        }
        if (StringUtils.isNotEmpty(wordSize)) {
            notice.setnWordSize(Integer.parseInt(wordSize));
        }
        notice.setUpdateTime(DateUtil.getCurDate());
        int res = noticeService.editNotice(notice);
        if (res != 1) {
            msgJson.setCode(2);
            msgJson.setMsg("编辑失败！");
            return msgJson;
        }
        return msgJson;
    }
    /**
     * 功能描述:删除消息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/20 13:57
     */
    @RequestMapping("/delete")
    public @ResponseBody
    MsgJson deletedNotice(HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"删除成功！");
        String user_id = request.getParameter("user_id");
        String notice_id = request.getParameter("notice_id");
        logger.info("删除消息:notice_id=" + notice_id );
        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(notice_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("notice_id为空！");
            return msgJson;
        }
        Notice notice = noticeService.queryById(Integer.parseInt(notice_id));
        if (notice == null) {
            msgJson.setCode(2);
            msgJson.setMsg("notice_id不存在！");
            return msgJson;
        }
        int res = noticeService.deleteNotice(Integer.parseInt(notice_id));
        if (res != 1) {
            msgJson.setCode(2);
            msgJson.setMsg("删除失败！");
            return msgJson;
        }

        return msgJson;
    }

    /**
     * 批量删除
     * @param user_id
     * @param idList
     * @return
     */
    @RequestMapping("/bdn")
    public MsgJson batchDeleteNotice(String user_id, String idList){
        if (StringUtils.isEmpty(user_id) || StringUtil.isEmpty(idList) || "[]".equals(idList)) {
            return MsgJson.fail("参数包含空值！");
        }
        logger.info("批量删除消息:idArry=" + idList );
        JSONArray idArry = JSONArray.fromObject(idList);
        int res = 0;
        for (Object idData : idArry) {
            JSONObject idObj = JSONObject.fromObject(idData);
            Integer id = Integer.valueOf(idObj.getString("id"));
            System.out.println(id);
            res = noticeService.deleteNotice(id);
        }
        if (res > 0) {
            logger.info("批量删除文字消息：idList=" + idArry);
            return MsgJson.success("批量删除成功!");
        }
        return MsgJson.fail("批量删除失败!");
    }

    /**
     * 功能描述:查询文字消息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/20 13:57
     */
    @RequestMapping("/list")
    public @ResponseBody
    MsgJson listNotice(HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"查询成功！");
        String user_id = request.getParameter("user_id");
        String limit = request.getParameter("limit");
        String page = request.getParameter("page");
        String type = request.getParameter("type");
        String key = request.getParameter("key");

        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(type)) {
            msgJson.setCode(1);
            msgJson.setMsg("type为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(limit)) {
            msgJson.setCode(1);
            msgJson.setMsg("limit为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(page)) {
            msgJson.setCode(1);
            msgJson.setMsg("page为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(key)) {
           key = "";
        }

        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(limit));
        List<Notice> notices = noticeService.queryAllContainsKey(type, key);
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        msgJson.setData(notices);
        msgJson.setCount(pageInfo.getTotal());
        return msgJson;
    }

    /**
     * 功能描述:上传文件消息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/20 13:57
     */
    @RequestMapping("/upload")
    public @ResponseBody
    MsgJson uploadNotice(@RequestParam(value = "file", required = false) MultipartFile file,
                         HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"文件上传成功！");
        String user_id = request.getParameter("user_id");
        String title = request.getParameter("title");
        String room_list = request.getParameter("room_list");
        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }
        if (StringUtils.isEmpty(room_list)) {
            msgJson.setCode(1);
            msgJson.setMsg("room_list为空！");
            return msgJson;
        }
        if (file.getSize() <= 0) {
            msgJson.setCode(1);
            msgJson.setMsg("file为空！");
            return msgJson;
        }
        logger.info("文件消息上传:title=" + title + ",room_list=" + room_list );
        String fileName = file.getOriginalFilename();//文件名
        String suffix =  fileName.substring(fileName.lastIndexOf("."));//文件后缀名

        fileName = DateUtil.getOutTradeNo() + suffix;
        String loacalPath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
//            loacalPath = FileUtil.getProperValue("WINPATH");
            loacalPath =  config.getWINPATH();

        } else {
//            loacalPath = FileUtil.getProperValue("LINUXDOCPATH");
            loacalPath = config.getLINUXDOCPATH();
        }
        String filePath =  FileType.getKey(suffix) + File.separator;
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

        //如果是ppt，需要转成图片，并存储
        Map<String,Object> map = null;
        if (".pptx".equals(suffix) || ".PPTX".equals(suffix)){
            map = ConverPPTFileToImageUtil.converPPTXtoImage(loacalPath + filePath + fileName,
                    loacalPath + filePath,
                    "png");
        }else if (".ppt".equals(suffix) || ".PPT".equals(suffix) ){
            map = ConverPPTFileToImageUtil.converPPTtoImage(loacalPath + filePath + fileName,
                    loacalPath + filePath,
                    "png");
        }

        /** 存一次图片，添加多条notice、roomnotice*/
        JSONArray roomArry = JSONArray.fromObject(room_list);
        for(int i = 0; i < roomArry.size(); i++){
            JSONObject jsonRoom = roomArry.getJSONObject(i);
            String id = jsonRoom.getString("id");
            String name = jsonRoom.getString("name");

            //String nTitle, String nType, String publishRoom, String publishRoomId, Date updateTime
            Notice notice = new Notice(title, NoticeType.getNoticeIndex(FileType.getKey(suffix))+"",
                    name, id, DateUtil.getCurDate());
            notice.setnUrl(config.getSERVICEURL() + "notice/fileDownLoad?name=" + fileName);
            notice.setUpdateTime(DateUtil.getCurDate());
            double sizeD = (double) file.getSize();
            int size = (int) sizeD /1048576;
            notice.setFileSize(size);

            //将ppt转换的图片信息插入数据库
            if (map != null && (Boolean) map.get("converReturnResult")){
                List<String> imgNames=(List<String>) map.get("imgNames");
                for (String imgName : imgNames) {
                    System.out.println(imgName);
                    notice.setFilePath(loacalPath + filePath + imgName);
                    notice.setnUrl(config.getSERVICEURL() + "notice/fileDownLoad?name=" + imgName);
                    noticeService.addNotice(notice);
                    notice.setId(null);
                }
            }else {//将非ppt文件信息插入数据库
                notice.setFilePath(loacalPath + filePath + fileName);
                noticeService.addNotice(notice);
            }
        }
        return msgJson;
    }

    /**
     * 功能描述:上编辑传文件消息
     * @auther: pxx
     * @param: [request]
     * @return: com.supadata.utils.MsgJson
     * @date: 2018/6/20 13:57
     */
    @RequestMapping("/eUpload")
    public @ResponseBody
    MsgJson editUploadNotice(@RequestParam(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request) {
        MsgJson msgJson = new MsgJson(0,"文件上传成功！");
        String user_id = request.getParameter("user_id");
        String title = request.getParameter("title");
        String notice_id = request.getParameter("notice_id");
        if (StringUtils.isEmpty(user_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("user_id为空！");
            return msgJson;
        }if (StringUtils.isEmpty(notice_id)) {
            msgJson.setCode(1);
            msgJson.setMsg("notice_id为空！");
            return msgJson;
        }
        if (file.getSize() <= 0) {
            msgJson.setCode(1);
            msgJson.setMsg("file为空！");
            return msgJson;
        }
        return msgJson;
    }

    /**
     * 功能描述: 文件下载
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/20 23:31
     */
    @RequestMapping("/fileDownLoad")
    public ResponseEntity<byte[]> fileDownLoad(HttpServletRequest request) throws Exception{
        String ip = request.getHeader("X-Real-Ip");

        String fileName = request.getParameter("name");

        String type = request.getParameter("type");

        logger.info("文件消息下载=" + DateUtil.getCurrentDateTime() + ",ip=" + ip + ",fileName=" + fileName);

        String suffix =  fileName.substring(fileName.lastIndexOf("."));//文件后缀名
        // 本地文件路径

        String filePath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
            filePath = config.getWINPATH();
        } else {
            filePath = config.getLINUXDOCPATH();
        }
        if (fileName.startsWith("PPT")){
            filePath = filePath + "ppt" + File.separator + fileName;
        }else{
            if (StringUtils.isNotEmpty(type)) {//添加一个监控目录的分支
                suffix = ".monitor";
            }
            filePath = filePath + FileType.getKey(suffix) + File.separator + fileName;
        }

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


}
