package com.supadata.pojo;

import java.util.Date;

public class Message {
    private Integer id;

    private String mModule;

    private String mUrl;

    private String mUser;

    private String mStatus;

    private Date updateTime;

    private String mRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getmModule() {
        return mModule;
    }

    public void setmModule(String mModule) {
        this.mModule = mModule == null ? null : mModule.trim();
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl == null ? null : mUrl.trim();
    }

    public String getmUser() {
        return mUser;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser == null ? null : mUser.trim();
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus == null ? null : mStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getmRemark() {
        return mRemark;
    }

    public void setmRemark(String mRemark) {
        this.mRemark = mRemark == null ? null : mRemark.trim();
    }
}