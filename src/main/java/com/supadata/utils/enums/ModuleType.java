package com.supadata.utils.enums;

/**
 * 创建日期: 2019/3/29.
 * @author: xm
 * 内    容:
 */
public enum ModuleType {

    NOTICE("1","文字"),
    IMAGE("2","图片"),
    VIDEO("3","视频"),
    PPT("4","PPT"),
    COURSE("5","课程");

    // 成员变量
    private String index;
    private String name;

    // 普通方法
    public static String getName(String index) {
        for (ModuleType c : ModuleType.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }
    // 普通方法
    public static String getIndex(String name) {
        for (ModuleType c : ModuleType.values()) {
            if (c.getName().equals(name)) {
                return c.index;
            }
        }
        return null;
    }

    // 构造方法
    private ModuleType(String index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
