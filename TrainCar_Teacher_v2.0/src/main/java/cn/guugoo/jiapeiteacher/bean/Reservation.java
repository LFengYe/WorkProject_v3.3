package cn.guugoo.jiapeiteacher.bean;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class Reservation {


    /**
     * Name : 看看
     * Amount : 120.0
     * HeadImg : http://101.201.74.192:8001//HeadPortraitImg/S_1_46_20160901093239.JPEG
     * DriveType :
     * Status : 3
     * BookingAccount : 2
     * BookingTime : 2016年09月01日11:25-12:25
     * BookingId : 95986f44-1e7b-4974-8329-2086e3f8d419
     * IsComment : 0
     */

//    private String Name;
//    private double Amount;
//    private String HeadImg;
    private String DriveType;
    private int Status;
    private String BookingAccount;
    private String BookingTime;
    private String BookingId;
    private ArrayList<ReservationStudent> StudentList;
/*
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String HeadImg) {
        this.HeadImg = HeadImg;
    }
    */

    public ArrayList<ReservationStudent> getStudentList() {
        return StudentList;
    }

    public void setStudentList(ArrayList<ReservationStudent> studentList) {
        StudentList = studentList;
    }

    public String getDriveType() {
        return DriveType;
    }

    public void setDriveType(String DriverType) {
        this.DriveType = DriverType;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getBookingAccount() {
        return BookingAccount;
    }

    public void setBookingAccount(String BookingAccount) {
        this.BookingAccount = BookingAccount;
    }

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String BookingTime) {
        this.BookingTime = BookingTime;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String BookingId) {
        this.BookingId = BookingId;
    }

    /*
    public int getIsComment() {
        return IsComment;
    }

    public void setIsComment(int IsComment) {
        this.IsComment = IsComment;
    }
    */
}
