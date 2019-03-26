package com.supadata.utils.enums;

/**
 * 功能描述:获取通知类型
 * @auther: pxx
 * @param:
 * @return:
 * @date: 2018/6/20 17:28
 */
public enum NoticeType {

    TYPE1("word", 1), TYPE2("image", 2),TYPE3("video", 3),TYPE4("ppt", 4);
    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private NoticeType(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (NoticeType c : NoticeType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    // 普通方法
    public static Integer getNoticeIndex(String name) {
        for (NoticeType c : NoticeType.values()) {
            if (c.getName().equals(name)) {
                return c.index;
            }
        }
        return 0;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
