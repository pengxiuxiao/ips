package com.supadata.service.impl;

import com.supadata.dao.CourseMapper;
import com.supadata.dao.SeatMapper;
import com.supadata.dao.StudentCardMapper;
import com.supadata.pojo.Course;
import com.supadata.pojo.Room;
import com.supadata.pojo.Seat;
import com.supadata.pojo.StudentCard;
import com.supadata.service.ICourseService;
import com.supadata.utils.DateUtil;
import com.supadata.utils.MsgJson;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: CourseServiceImpl
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/13 14:52
 * @Description:
 */
@Service
@Transactional
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private StudentCardMapper studentCardMapper;

    /**
     * 功能描述:查询现在或将要上的课程
     * @auther: pxx
     * @param: [time]
     * @return: java.util.List<com.supadata.pojo.Course>
     * @date: 2018/6/13 14:58
     */
    @Override
    public List<Course> queryComingCourse(String time) {
        return courseMapper.selectCourseAfterTime(time);
    }

    /**
     * 功能描述: 根据卡号查找课程（现在或将来的），优先查本教室的
     *
     * @param card_number
     * @param room_id
     * @param currentDateTime
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 17:26
     */
    @Override
    public Course queryStudentComingCourse(String card_number, Integer room_id, String currentDateTime) {
        //先查所在教室的课程
        Course cource = courseMapper.selectCourseByRoomIdAndCNo(card_number, room_id, currentDateTime);
        if (cource == null) {
            //所在教室无课程查询其他教室的
            cource = courseMapper.selectCourseByCNo(card_number, currentDateTime);
        }
        return cource;
    }

    /**
     * 功能描述: 查找发布的课程
     *
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/21 22:23
     * @param key
     */
    @Override
    public List<Course> queryAllCourse(String key, Integer type) {
        return courseMapper.selectAllCourse(key,type);
    }

    /**
     * 功能描述:删除课程
     *
     * @param id
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/21 22:40
     */
    @Override
    public int deleteCourse(Integer id) {
        return courseMapper.updateDeleteStatus(id);
    }

    /**
     * 功能描述:处理课程文件
     *
     * @param workbook
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 23:19
     */

    @Override
    public MsgJson handleCourseExcel(Workbook workbook, Room room) {
        // 读取第一个工作表sheet,获取课程信息
        Map<String, String> shaeetMap = readSheet1(workbook.getSheetAt(0));
        Course course = new Course();
        course.setcRoomId(room.getId());
        course.setcRoomName(room.getrName());
        course.setcName(shaeetMap.get("课程名称"));
        course.setUpdateTime(DateUtil.getCurDate());
        int res = courseMapper.insertSelective(course);

        Map<String, String> map = new HashedMap();
        List<StudentCard> studentCards = studentCardMapper.selectAllList(map);
        for (StudentCard studentCard : studentCards) {
            map.put(studentCard.getStudentName(), studentCard.getSecretNumber());
        }
        //读取第二个工作表sheet，获取座次信息
        Map<String, Object> reMap = readSheet2(workbook.getSheetAt(1), course, map);
        return (MsgJson) reMap.get("result");
    }

    /**
     * 功能描述:查询课程
     *
     * @param id
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/26 15:21
     */
    @Override
    public Course queryById(Integer id) {
        return courseMapper.selectByPrimaryKey(id);
    }

    /**
     * 功能描述:更新
     *
     * @param course
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/26 15:27
     */
    @Override
    public int editById(Course course) {
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    @Override
    public Course queryCourseByRoomId(Integer roomId) {
        return courseMapper.selectCourseByRoomId(roomId);
    }

    @Override
    public int addCourse(Course course) {
        return courseMapper.insertSelective(course);
    }

    @Override
    public int updateCourse(Course course) {
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    /**
     * 功能描述: 读取sheet1
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 17:59
     */
    private Map<String, String> readSheet1(Sheet sheet1) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 1; i++) {
            Row row = sheet1.getRow(i);
            Cell cell1 = row.getCell(0);
            Cell cell2 = row.getCell(1);
            String value1 = cell1.getStringCellValue().trim();
            String value2 = "";
            if (cell2.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                Date d = cell2.getDateCellValue();
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                value2 = formater.format(d);
            } else {
                value2 = cell2.getStringCellValue().trim();
            }
            map.put(value1, value2);
        }
        return map;
    }


    /**
     * 功能描述: 读取座次表(sheet2)
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 22:01
     */
    private Map<String,Object> readSheet2(Sheet sheet2, Course course, Map<String, String> map) {
        Map<String, Object> shaeetMap = new HashMap<>();
        shaeetMap.put("result", MsgJson.success("添加完成"));
        int sheet2firstRowNum = 1;
        // 获取sheet中最后一行行号
        int lastRowNum = sheet2.getLastRowNum()+1;
        int firstRowNum = sheet2.getFirstRowNum();
        System.out.println(firstRowNum);
        int lastCellNum = 0;
        // 取第一行标题
        Row firstRow = sheet2.getRow(0);

        // 判断列头
        String titleC[] = new String[firstRow.getLastCellNum()];
        String titleL[] = new String[lastRowNum];
        String columGdIndex = "";
        String lineGdIndex = "";
        int emptyRow = 0;//统计空的行头，遍历时要减掉sheet2.get
        int emptyColum = 0;//统计空的列头，遍历时要减掉
        for (int i = firstRow.getFirstCellNum(); i < firstRow.getLastCellNum(); i++) {//列
            Cell cell = firstRow.getCell(i);
            try {
                titleC[i] = cell.getStringCellValue().trim();
                if (i > 0 && StringUtils.isEmpty( titleC[i])){
                    emptyColum = emptyColum + 1;
                }
                if ("过道".equals(titleC[i])){
                    if (StringUtils.isEmpty(columGdIndex)){
                        columGdIndex = i + columGdIndex;
                    }else{
                        columGdIndex = columGdIndex + "," + i;
                    }
                }
            }catch (NullPointerException e){
                emptyColum = emptyColum + 1;
                e.printStackTrace();
            }
        }
        for (int j = 0; j < titleL.length; j++){//行
            Row row = sheet2.getRow(j);
            Cell cell = row.getCell(0);
            if (j > 0 && StringUtils.isEmpty(cell.getStringCellValue().trim())){
                emptyRow = emptyRow + 1;
                continue;
            }
            titleL[j] = cell.getStringCellValue().trim();
            if ("过道".equals(titleL[j])){
                if (StringUtils.isEmpty(lineGdIndex)){
                    lineGdIndex = j + lineGdIndex;
                }else{
                    lineGdIndex = lineGdIndex + "," + j;
                }
            }
        }
        lastRowNum = lastRowNum- emptyRow;//行
        lastCellNum = titleC.length - 1;//列
        shaeetMap.put("rGdIndex",lineGdIndex);//过道的行坐标
        shaeetMap.put("cGdIndex",columGdIndex);//过道的列坐标
        shaeetMap.put("rank", lastRowNum-1 + "*" + (lastCellNum - emptyColum));//行 * 列
        shaeetMap.put("rRankLine", lastRowNum-1);//行
        shaeetMap.put("rRankColum", (lastCellNum - emptyColum));//列
        // 遍历数据转为list
        int guodaoC = 0;
        int guodaoL = 0;
        List<Seat> sheet2List = new ArrayList<>();
        for (int y = sheet2firstRowNum; y < lastRowNum; y++) {
            Row row = sheet2.getRow(y);
            Cell temC = row.getCell(1);
            String temV = temC.getStringCellValue();
//                if (StringUtils.isEmpty(temV) || "过道".equals(temV)){
//                    guodaoC = guodaoC + 1;
//                }

            // 遍历所有数据
            for (int z = 1; z <= lastCellNum; z++) {
                Cell cell = row.getCell(z);
                String value = cell.getStringCellValue().trim();
//                    if (y == 1 && (StringUtils.isEmpty(value) || "过道".equals(value))){
//                        guodaoL = guodaoL + 1;
//                    }
                Seat seat = new Seat();
                seat.setsLine(y - guodaoC);
                seat.setsColumn(z - guodaoL);
                seat.setStuName(value);
                String cartNo =  map.get(value);
                if (StringUtils.isEmpty(cartNo) && StringUtils.isNotEmpty(value) && !"过道".equals(value)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    shaeetMap.put("result", MsgJson.fail(value + " 的卡信息未录入！"));
                    return shaeetMap;
                }
                seat.setCardNo(cartNo);
                seat.setrRank((String) shaeetMap.get("rank"));
                seat.setrRankColum((Integer) shaeetMap.get("rRankColum"));
                seat.setrRankLine((Integer) shaeetMap.get("rRankLine"));
                seat.setLineRoadIndex(StringUtils.isEmpty(lineGdIndex) ? "0" : lineGdIndex);
                seat.setColuRoadIndex(StringUtils.isEmpty(columGdIndex) ? "0" : columGdIndex);
                seat.setCourseId(course.getId());
                seat.setRoomId(course.getcRoomId());
                seat.setRoomName(course.getcRoomName());
                seat.setUpdateTime(com.supadata.utils.DateUtil.getCurDate());
                seatMapper.insertSelective(seat);
                sheet2List.add(seat);
            }
        }
        shaeetMap.put("seats",sheet2List);
        return shaeetMap;
    }

    /**
     * 功能描述:读取sheet3，获取学员卡号信息
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/25 22:49
     */
    private Map<String,String> readSheet3(Sheet sheet3) {
        Map<String, String> shaeetMap = new HashMap<>();
        int sheet1firstRowNum = 1;
        // 获取sheet中最后一行行号
        int lastRowNum = sheet3.getLastRowNum();

        // 遍历数据转为list
        for (int i = sheet1firstRowNum; i <= lastRowNum; i++) {
            StudentCard sc = new StudentCard();
            Row row = sheet3.getRow(i);
            Cell cell1 = row.getCell(0);
            Cell cell2 = row.getCell(1);
            cell2.setCellType(Cell.CELL_TYPE_STRING);
            String value1 = cell1.getStringCellValue().trim();
            String value2 = cell2.getStringCellValue().trim();
            sc.setStudentName(value1);
            sc.setCardNumber(value2);
            sc.setUpdateTime(com.supadata.utils.DateUtil.getCurDate());
            studentCardMapper.insertSelective(sc);
            shaeetMap.put(value1,value2);
        }
        return shaeetMap;
    }
}
