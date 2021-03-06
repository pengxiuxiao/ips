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

    private String cTitle;

    private Integer rRankColum;

    private Integer rRankLine;

    private String sCall;

    private String sFlag;

    private String sMsg;

    private String uPic;



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

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public Integer getrRankColum() {
        return rRankColum;
    }

    public void setrRankColum(Integer rRankColum) {
        this.rRankColum = rRankColum;
    }

    public Integer getrRankLine() {
        return rRankLine;
    }

    public void setrRankLine(Integer rRankLine) {
        this.rRankLine = rRankLine;
    }

    public String getsCall() {
        return sCall;
    }

    public void setsCall(String sCall) {
        this.sCall = sCall;
    }

    public String getsFlag() {
        return sFlag;
    }

    public void setsFlag(String sFlag) {
        this.sFlag = sFlag;
    }

    public String getsMsg() {
        return sMsg;
    }

    public void setsMsg(String sMsg) {
        this.sMsg = sMsg;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic;
    }
}