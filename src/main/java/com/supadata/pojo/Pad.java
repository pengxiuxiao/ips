package com.supadata.pojo;

import java.util.Date;

public class Pad {
    private Integer id;

    private String pIp;

    private String code;

    private String clientId;

    private String roomName;

    private Integer roomId;

    private String pLocation;

    private String pStatus;

    private Date updateTime;

    private String startTime;

    private String endTime;

    private String pRemark;

    private String isBlack;

    private String pModule;

    private String pState;

    private String pClickCard;

    private Integer pAudio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getpIp() {
        return pIp;
    }

    public void setpIp(String pIp) {
        this.pIp = pIp == null ? null : pIp.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getpLocation() {
        return pLocation;
    }

    public void setpLocation(String pLocation) {
        this.pLocation = pLocation == null ? null : pLocation.trim();
    }

    public String getpStatus() {
        return pStatus;
    }

    public void setpStatus(String pStatus) {
        this.pStatus = pStatus == null ? null : pStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(String isBlack) {
        this.isBlack = isBlack;
    }

    public String getpRemark() {
        return pRemark;
    }

    public void setpRemark(String pRemark) {
        this.pRemark = pRemark == null ? null : pRemark.trim();
    }

    public String getpModule() {
        return pModule;
    }

    public void setpModule(String pModule) {
        this.pModule = pModule;
    }

    public String getpClickCard() {
        return pClickCard;
    }

    public void setpClickCard(String pClickCard) {
        this.pClickCard = pClickCard;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getpState() {
        return pState;
    }

    public void setpState(String pState) {
        this.pState = pState;
    }

    public Integer getpAudio() {
        return pAudio;
    }

    public void setpAudio(Integer pAudio) {
        this.pAudio = pAudio;
    }
}