package com.supadata.pojo;

import java.util.Date;

public class Room {
    private Integer id;

    private String rName;

    private String rLocation;

    private Integer capacity;

    private String rRank;

    private String rIp;

    private String rModule;

    private String rModuleMsg;

    private Date updateTime;

    private String rRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName == null ? null : rName.trim();
    }

    public String getrLocation() {
        return rLocation;
    }

    public void setrLocation(String rLocation) {
        this.rLocation = rLocation == null ? null : rLocation.trim();
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getrRank() {
        return rRank;
    }

    public void setrRank(String rRank) {
        this.rRank = rRank == null ? null : rRank.trim();
    }

    public String getrIp() {
        return rIp;
    }

    public void setrIp(String rIp) {
        this.rIp = rIp == null ? null : rIp.trim();
    }

    public String getrModule() {
        return rModule;
    }

    public void setrModule(String rModule) {
        this.rModule = rModule;
        switch (rModule) {
            case "1":
                this.rModuleMsg = "文字";
                return;
            case "2":
                this.rModuleMsg = "图片";
                return;
            case "3":
                this.rModuleMsg = "PPT";
                return;
            case "4":
                this.rModuleMsg = "视频";
                return;
            case "5":
                this.rModuleMsg = "课程1";
                return;
        }
    }

    public String getrModuleMsg() {
        return rModuleMsg;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getrRemark() {
        return rRemark;
    }

    public void setrRemark(String rRemark) {
        this.rRemark = rRemark == null ? null : rRemark.trim();
    }

}