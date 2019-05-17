package com.supadata.pojo;

import java.util.Date;

public class MImg {
    private Integer id;

    private Integer pId;

    private String mUrl;

    private Date updateTime;

    private String mRemark;

    public MImg() {
    }

    public MImg(Integer pId, String mUrl, Date updateTime) {
        this.pId = pId;
        this.mUrl = mUrl;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl == null ? null : mUrl.trim();
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