package com.supadata.pojo;

import java.util.Date;

public class StudentCard {
    private Integer id;

    private String studentName;

    private String cardNumber;//明码

    private String secretNumber;//暗码

    private Date updateTime;

    private String scRemark;

    private String courseName;

    private Integer courseId;

    public StudentCard() {
    }

    public StudentCard(String studentName, String cardNumber, String secretNumber, Date updateTime) {
        this.studentName = studentName;
        this.cardNumber = cardNumber;
        this.secretNumber = secretNumber;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName == null ? null : studentName.trim();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber == null ? null : cardNumber.trim();
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

    public String getSecretNumber() {
        return secretNumber;
    }

    public void setSecretNumber(String secretNumber) {
        this.secretNumber = secretNumber;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}