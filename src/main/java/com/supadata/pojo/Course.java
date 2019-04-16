package com.supadata.pojo;

import java.util.Date;

public class Course {
    private Integer id;

    private String cName;

    private Integer cRoomId;

    private String cRoomName;

    private String cDesc;

    private String cTeacher;

    private Date cStartTime;

    private Date cEndTime;

    private Integer cPeople;

    private Integer cStatus;

    private Integer cWordSize;

    private Date updateTime;

    private String cRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName == null ? null : cName.trim();
    }

    public Integer getcRoomId() {
        return cRoomId;
    }

    public void setcRoomId(Integer cRoomId) {
        this.cRoomId = cRoomId;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc == null ? null : cDesc.trim();
    }

    public String getcTeacher() {
        return cTeacher;
    }

    public void setcTeacher(String cTeacher) {
        this.cTeacher = cTeacher == null ? null : cTeacher.trim();
    }

    public Date getcStartTime() {
        return cStartTime;
    }

    public void setcStartTime(Date cStartTime) {
        this.cStartTime = cStartTime;
    }

    public Date getcEndTime() {
        return cEndTime;
    }

    public void setcEndTime(Date cEndTime) {
        this.cEndTime = cEndTime;
    }

    public Integer getcPeople() {
        return cPeople;
    }

    public void setcPeople(Integer cPeople) {
        this.cPeople = cPeople;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark == null ? null : cRemark.trim();
    }

    public Integer getcStatus() {
        return cStatus;
    }

    public void setcStatus(Integer cStatus) {
        this.cStatus = cStatus;
    }

    public String getcRoomName() {
        return cRoomName;
    }

    public void setcRoomName(String cRoomName) {
        this.cRoomName = cRoomName;
    }

    public Integer getcWordSize() {
        return cWordSize;
    }

    public void setcWordSize(Integer cWordSize) {
        this.cWordSize = cWordSize;
    }
}