package com.supadata.pojo;

/**
 * 创建日期: 2019/3/11.
 * 创 建 人: xm
 * 内    容: 登录后系统返回参数
 *
 * @author XM-PC
 */
public class SystemInfo {

    /* 当前系统时间 */
    private long updateTime;

    private String code;

    private String roomId;

    private String roomName;

    /* 关机时间 hh:mm */
    private String offTime;

    /* 开机时间 hh:mm */
    private String onTime;

    /**
     * 初始化后将要执行的事件
     * image
     * notice
     * video
     * course
     */
    private String event;

    public SystemInfo() {
    }

    public SystemInfo(long updateTime, String code, String roomId, String roomName, String offTime, String onTime, String event) {
        this.updateTime = updateTime;
        this.code = code;
        this.roomId = roomId;
        this.roomName = roomName;
        this.offTime = offTime;
        this.onTime = onTime;
        this.event = event;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
