package com.guugoo.jiapeiteacher.bean;

import java.util.ArrayList;

/**
 * Created by LFeng on 2017/5/30.
 */

public class Booking {
    private String bookingCode; //预约编号
    private String schoolCode; //驾校编号
    private String locationCode; //场地编号
    private String locationName; //场地名称
    private String teacherCode; //教练编号
    private String teacherName; //教练名称
    private String cardNo;
    private String courseDate;
    private String courseTime;
    private String subject;
    private int bookingSum;
    private int bookAmount;
    private String weekStr;
    private String dateStr;
    private int status;
    private String carType;
    private ArrayList<ReservationStudent> students;

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getBookingSum() {
        return bookingSum;
    }

    public void setBookingSum(int bookingSum) {
        this.bookingSum = bookingSum;
    }

    public int getBookAmount() {
        return bookAmount;
    }

    public void setBookAmount(int bookAmount) {
        this.bookAmount = bookAmount;
    }

    public String getWeekStr() {
        return weekStr;
    }

    public void setWeekStr(String weekStr) {
        this.weekStr = weekStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public ArrayList<ReservationStudent> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<ReservationStudent> students) {
        this.students = students;
    }
}
