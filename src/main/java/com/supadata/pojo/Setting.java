package com.supadata.pojo;

import java.util.Date;

public class Setting {
    private Integer id;

    private Date sStartTime;

    private Date sEndTime;

    private String displayDate;

    private String displayCard;

    private String displayDaojishi;

    private String daojishi;

    private String wordFont;

    private String wordSize;

    private String wordColor;

    private String rotationTime;

    private String sModule;

    private Date updateTime;

    private String seRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getsStartTime() {
        return sStartTime;
    }

    public void setsStartTime(Date sStartTime) {
        this.sStartTime = sStartTime;
    }

    public Date getsEndTime() {
        return sEndTime;
    }

    public void setsEndTime(Date sEndTime) {
        this.sEndTime = sEndTime;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate == null ? null : displayDate.trim();
    }

    public String getDisplayCard() {
        return displayCard;
    }

    public void setDisplayCard(String displayCard) {
        this.displayCard = displayCard == null ? null : displayCard.trim();
    }

    public String getDisplayDaojishi() {
        return displayDaojishi;
    }

    public void setDisplayDaojishi(String displayDaojishi) {
        this.displayDaojishi = displayDaojishi == null ? null : displayDaojishi.trim();
    }

    public String getDaojishi() {
        return daojishi;
    }

    public void setDaojishi(String daojishi) {
        this.daojishi = daojishi == null ? null : daojishi.trim();
    }

    public String getWordFont() {
        return wordFont;
    }

    public void setWordFont(String wordFont) {
        this.wordFont = wordFont == null ? null : wordFont.trim();
    }

    public String getWordSize() {
        return wordSize;
    }

    public void setWordSize(String wordSize) {
        this.wordSize = wordSize == null ? null : wordSize.trim();
    }

    public String getWordColor() {
        return wordColor;
    }

    public void setWordColor(String wordColor) {
        this.wordColor = wordColor == null ? null : wordColor.trim();
    }

    public String getRotationTime() {
        return rotationTime;
    }

    public void setRotationTime(String rotationTime) {
        this.rotationTime = rotationTime == null ? null : rotationTime.trim();
    }

    public String getsModule() {
        return sModule;
    }

    public void setsModule(String sModule) {
        this.sModule = sModule == null ? null : sModule.trim();
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