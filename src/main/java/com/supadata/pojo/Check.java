package com.supadata.pojo;

import java.util.Date;

public class Check {
    private Integer id;

    private String chModule;

    private String chUrl;

    private String chUser;

    private String chStatus;

    private Date updateTime;

    private String scRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChModule() {
        return chModule;
    }

    public void setChModule(String chModule) {
        this.chModule = chModule == null ? null : chModule.trim();
    }

    public String getChUrl() {
        return chUrl;
    }

    public void setChUrl(String chUrl) {
        this.chUrl = chUrl == null ? null : chUrl.trim();
    }

    public String getChUser() {
        return chUser;
    }

    public void setChUser(String chUser) {
        this.chUser = chUser == null ? null : chUser.trim();
    }

    public String getChStatus() {
        return chStatus;
    }

    public void setChStatus(String chStatus) {
        this.chStatus = chStatus == null ? null : chStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getScRemark() {
        return scRemark;
    }

    public void setScRemark(String scRemark) {
        this.scRemark = scRemark == null ? null : scRemark.trim();
    }
}