package com.supadata.utils.enums;

/**
 * 创建日期: 2019/3/29.
 * @author: xm
 * 内    容:
 */
public enum EventType {
    IMAGE("image"),
    NOTICE("notice"),
    PPT("ppt"),
    VIDEO("video"),
    ONOFF("onoff"),
    COURSE("course"),
    REBOOT("reboot"),
    REMOTE_OBSERVE("remote_observe"),
    OFFLINE("offline"),
    ONLINE("online");

    public String getEvent() {
        return event;
    }

    private String event;

    EventType(String event) {
        this.event = event;
    }


}
