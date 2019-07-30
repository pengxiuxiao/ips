package com.supadata.constant;

/**
 * @ClassName: IntervalType
 * @Description: 根据视频大小设置线程休眠的时间
 * @Author: pxx
 * @Date: 2019/7/30 10:21
 * @Description:
 */
public class IntervalType {

    private static int size1 = 100;
    private static int size2 = 200;
    private static int size3 = 300;
    private static int size4 = 400;


    /**
     * 功能描述:根据视频大小设置线程休眠的时间
     * @auther: pxx
     * @param: 视频大小
     * @return: 休眠时间
     * @date: 2019/7/30 10:44
     */
    public static Integer getInterval(Integer fileSize) {

        if (fileSize <= size1) {
            return 15000;
        } else if (fileSize <= size2) {
            return 30000;
        } else if (fileSize <= size3) {
            return 45000;
        } else {
            return 60000;
        }
    }
}
