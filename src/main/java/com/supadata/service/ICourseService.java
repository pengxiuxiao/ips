package com.supadata.service;

import com.supadata.pojo.Course;
import com.supadata.pojo.Room;
import com.supadata.utils.MsgJson;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @ClassName: ICourseService
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/13 14:50
 * @Description:
 */
public interface ICourseService {

    /**
     * 功能描述: 查询课表（现在+将来的）
     * @auther: pxx
     * @param:  当前时间
     * @return:
     * @date: 2018/6/13 17:25
     */
    List<Course> queryComingCourse(String time);

    /**
     * 功能描述:根据卡号查找课程（现在或将来的），优先查本教室的
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/13 17:26
     */
    Course queryStudentComingCourse(String card_number, Integer room_id, String currentDateTime);

    /**
     * 功能描述: 查找发布的课程
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/21 22:23
     * @param key
     */
    List<Course> queryAllCourse(String key,Integer type);

    /**
     * 功能描述:删除课程
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2018/6/21 22:40
     */
    int deleteCourse(Integer id);

    /**
     * 功能描述:处理课程文件
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/25 23:19
     */
    MsgJson handleCourseExcel(Workbook workbook, Room room);

    /**
     * 功能描述:查询课程
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/26 15:21
     */
    Course queryById(Integer id);

    /**
     * 功能描述:更新
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2018/6/26 15:27
     */
    int editById(Course course);

    /**
     * 功能描述:
     * @auther: pxx
     * @param: 
     * @return: 
     * @date: 2019/4/17 17:42
     */
    Course queryCourseByRoomId(Integer roomId);

    /**
     * 添加课程
     * @param course
     * @return
     */
    int addCourse(Course course);

    /**
     * 功能描述: 更新课程
     * @auther: pxx
     * @param:
     * @return:
     * @date: 2019/9/6 10:59
     */
    int updateCourse(Course course);
}
