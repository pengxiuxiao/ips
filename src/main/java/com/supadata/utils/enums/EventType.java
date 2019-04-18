package com.supadata.utils.enums;

/**
 * 创建日期: 2019/3/29.
 * @author: xm
 * 内    容:
 */
public enum EventType {

    NOTICE("1","notice"),
    IMAGE("2","image"),
    VIDEO("3","video"),
    PPT("4","ppt"),
    COURSE("5","course"),
    ONOFF("6","onoff"),
    REBOOT("7","reboot"),
    REMOTE_OBSERVE("8","remote_observe"),
    OFFLINE("9","offline"),
    SETTING("10","setting"),
    ONLINE("11","online");

    // 成员变量
    private String index;
    private String name;

    // 普通方法
    public static String getName(String index) {
        for (EventType c : EventType.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }
    // 普通方法
    public static String getIndex(String name) {
        for (EventType c : EventType.values()) {
            if (c.getName().equals(name)) {
                return c.index;
            }
        }
        return null;
    }

    // 构造方法
    private EventType(String index, String name) {
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
