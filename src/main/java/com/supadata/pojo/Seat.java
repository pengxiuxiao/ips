package com.supadata.pojo;

import java.util.Date;

public class Seat {
    private Integer id;

    private Integer courseId;

    private String sName;

    private String stuName;

    private String cardNo;

    private String rRank;

    private String lineRoadIndex;

    private String coluRoadIndex;

    private Integer sLine;

    private Integer sColumn;

    private Integer roomId;

    private String roomName;

    private String cDesc;

    private Date updateTime;

    private String sRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName == null ? null : sName.trim();
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName == null ? null : stuName.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getrRank() {
        return rRank;
    }

    public void setrRank(String rRank) {
        this.rRank = rRank == null ? null : rRank.trim();
    }

    public Integer getsLine() {
        return sLine;
    }

    public void setsLine(Integer sLine) {
        this.sLine = sLine;
    }

    public Integer getsColumn() {
        return sColumn;
    }

    public void setsColumn(Integer sColumn) {
        this.sColumn = sColumn;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc == null ? null : cDesc.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getsRemark() {
        return sRemark;
    }

    public void setsRemark(String sRemark) {
        this.sRemark = sRemark == null ? null : sRemark.trim();
    }

    public String getLineRoadIndex() {
        return lineRoadIndex;
    }

    public void setLineRoadIndex(String lineRoadIndex) {
        this.lineRoadIndex = lineRoadIndex;
    }

    public String getColuRoadIndex() {
        return coluRoadIndex;
    }

    public void setColuRoadIndex(String coluRoadIndex) {
        this.coluRoadIndex = coluRoadIndex;
    }
}