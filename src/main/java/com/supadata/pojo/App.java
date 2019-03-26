package com.supadata.pojo;

import java.util.Date;

public class App {
    private Integer id;

    private String aVersion;

    private String aType;

    private String aDesc;

    private String aUrl;

    private Date updateTime;

    private String seRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getaVersion() {
        return aVersion;
    }

    public void setaVersion(String aVersion) {
        this.aVersion = aVersion == null ? null : aVersion.trim();
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType == null ? null : aType.trim();
    }

    public String getaDesc() {
        return aDesc;
    }

    public void setaDesc(String aDesc) {
        this.aDesc = aDesc == null ? null : aDesc.trim();
    }

    public String getaUrl() {
        return aUrl;
    }

    public void setaUrl(String aUrl) {
        this.aUrl = aUrl == null ? null : aUrl.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSeRemark() {
        return seRemark;
    }

    public void setSeRemark(String seRemark) {
        this.seRemark = seRemark == null ? null : seRemark.trim();
    }
}