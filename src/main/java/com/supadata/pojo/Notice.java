package com.supadata.pojo;

import java.util.Date;

public class Notice {
    private Integer id;

    private String nTitle;

    private String nContent;

    private String nType;

    private String nStatus;

    private String publishRoom;

    private String publishRoomId;

    private Date nTime;

    private String nUser;

    private String nUrl;

    private String filePath;

    private Integer nWordSize;

    private Integer fileSize;

    private Date updateTime;

    private String nRemark;

    public Notice() {
    }

    public Notice(String nTitle, String nContent, String nType, String publishRoom, String publishRoomId, Integer nWordSize, Date updateTime) {
        this.nTitle = nTitle;
        this.nContent = nContent;
        this.nType = nType;
        this.publishRoom = publishRoom;
        this.publishRoomId = publishRoomId;
        this.nWordSize = nWordSize;
        this.updateTime = updateTime;
    }

    public Notice(String nTitle, String nContent, String nType, String publishRoom, String publishRoomId, Date updateTime) {
        this.nTitle = nTitle;
        this.nContent = nContent;
        this.nType = nType;
        this.publishRoom = publishRoom;
        this.publishRoomId = publishRoomId;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle == null ? null : nTitle.trim();
    }

    public String getnContent() {
        return nContent;
    }

    public void setnContent(String nContent) {
        this.nContent = nContent == null ? null : nContent.trim();
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType == null ? null : nType.trim();
    }

    public String getnStatus() {
        return nStatus;
    }

    public void setnStatus(String nStatus) {
        this.nStatus = nStatus == null ? null : nStatus.trim();
    }

    public String getPublishRoom() {
        return publishRoom;
    }

    public void setPublishRoom(String publishRoom) {
        this.publishRoom = publishRoom == null ? null : publishRoom.trim();
    }

    public Date getnTime() {
        return nTime;
    }

    public void setnTime(Date nTime) {
        this.nTime = nTime;
    }

    public String getnUser() {
        return nUser;
    }

    public void setnUser(String nUser) {
        this.nUser = nUser == null ? null : nUser.trim();
    }

    public String getnUrl() {
        return nUrl;
    }

    public void setnUrl(String nUrl) {
        this.nUrl = nUrl == null ? null : nUrl.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getnRemark() {
        return nRemark;
    }

    public void setnRemark(String nRemark) {
        this.nRemark = nRemark == null ? null : nRemark.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPublishRoomId() {
        return publishRoomId;
    }

    public void setPublishRoomId(String publishRoomId) {
        this.publishRoomId = publishRoomId;
    }

    public Integer getnWordSize() {
        return nWordSize;
    }

    public void setnWordSize(Integer nWordSize) {
        this.nWordSize = nWordSize;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
}