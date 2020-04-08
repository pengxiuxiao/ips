package com.supadata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.supadata.constant.Config;
import com.supadata.pojo.Course;
import com.supadata.pojo.StudentCard;
import com.supadata.service.ICourseService;
import com.supadata.service.IStudentCardService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.ExcelUtil;
import com.supadata.utils.MsgJson;
import com.supadata.utils.enums.FileType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @Autowired
    private Config config;

    /**
     * 功能描述:添加卡片信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping("/add")
    public MsgJson AddCard(String user_id, String number, String course_id, String name,
                           @RequestParam(value = "file", required = false) MultipartFile file){
        if (StringUtils.isEmpty(user_id) || StringUtils.isEmpty(number) || StringUtils.isEmpty(name)) {
            return MsgJson.fail("参数包含空值！");
        }
        //存储文件
        String url = "";
        if (file.getSize() > 0) {
            String fileName = file.getOriginalFilename();
            url = storageFile(number + fileName.substring(fileName.lastIndexOf(".")), file);
        }

        String secretNo = com.supadata.utils.StringUtil.HexToLongString(com.supadata.utils.StringUtil.overturnHexString(number));
        StudentCard card = studentCardService.selectByNumber(secretNo);
        if (card == null) {
            StudentCard sc = new StudentCard(name, number, secretNo, DateUtil.getCurDate());
            sc.setScRemark(url);
            Course course = courseService.queryById(Integer.parseInt(course_id));
            if (course != null) {
                sc.setCourseId(Integer.parseInt(course_id));
                sc.setCourseName(course.getcName());
            }
            int res = 0;
            try {
                res = studentCardService.insertSelective(sc);
                if (res > 0) {
                    logger.info("添加卡片：id=" + sc.getId() + ",name=" + name + ",number=" + number);
                    return MsgJson.success( "卡片添加成功！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return MsgJson.fail("卡号重复!");
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
    public MsgJson editCard(String user_id, Integer id,  String name,
                            @RequestParam(value = "file", required = false) MultipartFile file){
        if (StringUtils.isEmpty(user_id) || id == null || StringUtils.isEmpty(name)) {
            return MsgJson.fail("参数包含空值！");
        }
        StudentCard sc = studentCardService.selectByPrimaryKey(id);
        if (sc == null) {
            return MsgJson.success( "卡片id有误！");
        }
        //存储文件
        if (file.getSize() > 0) {
            String fileName = file.getOriginalFilename();
            String url = storageFile(sc.getCardNumber() + fileName.substring(fileName.lastIndexOf(".")), file);
            sc.setScRemark(url);
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
    public MsgJson listCards(String user_id, Integer page, Integer limit, String key){
        if (StringUtils.isEmpty(user_id)) {
            return MsgJson.fail("参数包含空值！");
        }
        Map<String, String> map = new HashedMap();
        if (StringUtils.isNotEmpty(key)) {
            try {
                String secretNo = com.supadata.utils.StringUtil.HexToLongString(com.supadata.utils.StringUtil.overturnHexString(key));
                map.put("number", secretNo);
            } catch (Exception e) {
                map.put("name", key);
            }
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
    public void exportCards(String user_id,String key,  HttpServletResponse response){
        if (StringUtils.isEmpty(user_id)) {
            return;
        }
        Map<String, String> map = new HashedMap();
        if (StringUtils.isNotEmpty(key)) {
            try {
                String secretNo = com.supadata.utils.StringUtil.HexToLongString(com.supadata.utils.StringUtil.overturnHexString(key));
                map.put("number", secretNo);
            } catch (Exception e) {
                map.put("name", key);
            }
        }
        List<StudentCard> cards = studentCardService.selectAllList(map);
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

    /**
     * 功能描述:存储文件
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 22:49
     */
    private String storageFile(String fileName, MultipartFile file) {
        String loacalPath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
            loacalPath = config.getWINPATH();
        } else {
            loacalPath = config.getLINUXDOCPATH();
        }
        String filePath =  "upic" + File.separator;
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
        return config.getSERVICEURL() + "card/fileDownLoad?name=" + fileName;
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

        logger.info("头像消息下载=" + DateUtil.getCurrentDateTime() + ",ip=" + ip + ",fileName=" + fileName);

        String filePath = "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0) {//windows环境
            filePath = config.getWINPATH();
        } else {
            filePath = config.getLINUXDOCPATH();
        }
        filePath = filePath + "upic" + File.separator + fileName;

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
     * 功能描述:同步接收卡片列表信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/4/18 15:49
     */
    @RequestMapping(value = "/synchci",method = RequestMethod.POST)
    public MsgJson synchronizeCardsInfo(HttpServletRequest request){
        String datas = getParm(request);

        if (StringUtils.isEmpty(datas)) {
            return MsgJson.fail("参数包含空值！");
        }
        System.out.println(datas);
        JSONObject fromObject = JSONObject.fromObject(datas);
        JSONArray jsonArray = fromObject.getJSONArray("datas");

        for (int i = 0; i < jsonArray.size();i ++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            if (StringUtils.isEmpty(name)) {
                return MsgJson.fail("name为空！");
            }

            String number = jsonObject.getString("number");
            if (StringUtils.isEmpty(number)) {
                return MsgJson.fail("number为空！");
            }

            String startDate = jsonObject.getString("startDate");
            if (StringUtils.isEmpty(startDate)) {
                return MsgJson.fail("startDate为空！");
            }

            String endDate = jsonObject.getString("endDate");
            if (StringUtils.isEmpty(endDate)) {
                return MsgJson.fail("endDate为空！");
            }
            System.out.println("name=" + name + ",number="+number+ ",startDate="+startDate+ ",endDate="+endDate);
        }
        return MsgJson.success( "接收成功！");

    }


    /**
     * 获取POST请求中Body参数
     * @param request
     * @return 字符串
     */
    public String getParm(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }
}
