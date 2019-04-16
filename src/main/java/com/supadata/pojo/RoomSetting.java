package com.supadata.pojo;

import java.util.Date;

public class RoomSetting {
    private Integer id;

    private Integer roomId;

    private Integer setId;

    private Date updateTime;

    private String rsRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getSetId() {
        return setId;
    }

    public void setSetId(Integer setId) {
        this.setId = setId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRsRemark() {
        return rsRemark;
    }

    public void setRsRemark(String rsRemark) {
        this.rsRemark = rsRemark == null ? null : rsRemark.trim();
    }
}