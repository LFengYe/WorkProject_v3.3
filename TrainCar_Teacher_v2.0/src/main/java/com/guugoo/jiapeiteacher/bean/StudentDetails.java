package com.guugoo.jiapeiteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/13.
 * --加油
 */
public class StudentDetails implements Parcelable {


    /**
     * Name : 123
     * Head : S_1_6_20160816165010.JPEG
     * Sex : 男
     * Tel : 12345678
     * CardId : 456
     * DriverType :
     * BookingAccount : 科目二
     * BookingTime : 2016年08月17日14:00-16:00
     * WhenLongTime : 120
     * ClassHour : 0时0分钟
     * CalculationTime : 0
     * EvaluateList : [{"LianjuTime":"2016年08月18日17:30-18:30","Subject":"2","Comment":"","SubjectItem":""}]
     */

    private String Name;
    private String Head;
    private String Sex;
    private String Tel;
    private int Status;
    private String CardId;
    private String DriverType;
    private String BookingAccount;
    private String BookingTime;
    private String ClassHour;
    private int WhenLong;
    private int CalculationTime;
    private int StudentId;
    private String BookingDay;
    private String TimeSlot;
    private String CurrentComment;
    private String CurrentSubjectItem;
    private int IsComment;
    private int StudyMinutes;
    private int SchoolId;

    public String getCurrentComment() {
        return CurrentComment;
    }

    public void setCurrentComment(String currentComment) {
        CurrentComment = currentComment;
    }

    public String getCurrentSubjectItem() {
        return CurrentSubjectItem;
    }

    public void setCurrentSubjectItem(String currentSubjectItem) {
        CurrentSubjectItem = currentSubjectItem;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getBookingDay() {
        return BookingDay;
    }

    public void setBookingDay(String bookingDay) {
        BookingDay = bookingDay;
    }

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public int getIsComment() {
        return IsComment;
    }

    public void setIsComment(int isComment) {
        IsComment = isComment;
    }

    /**
     * LianjuTime : 2016年08月18日17:30-18:30
     * Subject : 2
     * Comment :
     * SubjectItem :
     */

    private ArrayList<EvaluateListBean> EvaluateList;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String Head) {
        this.Head = Head;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String CardId) {
        this.CardId = CardId;
    }

    public String getDriverType() {
        return DriverType;
    }

    public void setDriverType(String DriverType) {
        this.DriverType = DriverType;
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

    public int getWhenLong() {
        return WhenLong;
    }

    public void setWhenLong(int WhenLong) {
        this.WhenLong = WhenLong;
    }

    public String getClassHour() {
        return ClassHour;
    }

    public void setClassHour(String ClassHour) {
        this.ClassHour = ClassHour;
    }

    public int getCalculationTime() {
        return CalculationTime;
    }

    public void setCalculationTime(int CalculationTime) {
        this.CalculationTime = CalculationTime;
    }

    public int getStudyMinutes() {
        return StudyMinutes;
    }

    public void setStudyMinutes(int studyMinutes) {
        StudyMinutes = studyMinutes;
    }

    public ArrayList<EvaluateListBean> getEvaluateList() {
        return EvaluateList;
    }

    public void setEvaluateList(ArrayList<EvaluateListBean> EvaluateList) {
        this.EvaluateList = EvaluateList;
    }

    public static class EvaluateListBean {
        private String LianjuTime;
        private String Subject;
        private String Comment;
        private String SubjectItem;

        public String getLianjuTime() {
            return LianjuTime;
        }

        public void setLianjuTime(String LianjuTime) {
            this.LianjuTime = LianjuTime;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String Subject) {
            this.Subject = Subject;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }

        public String getSubjectItem() {
            return SubjectItem;
        }

        public void setSubjectItem(String SubjectItem) {
            this.SubjectItem = SubjectItem;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.Head);
        dest.writeString(this.Sex);
        dest.writeString(this.Tel);
        dest.writeInt(this.Status);
        dest.writeString(this.CardId);
        dest.writeString(this.DriverType);
        dest.writeString(this.BookingAccount);
        dest.writeString(this.BookingTime);
        dest.writeString(this.ClassHour);
        dest.writeInt(this.WhenLong);
        dest.writeInt(this.CalculationTime);
        dest.writeInt(this.StudentId);
        dest.writeString(this.BookingDay);
        dest.writeString(this.TimeSlot);
        dest.writeInt(this.IsComment);
        dest.writeInt(this.StudyMinutes);
        dest.writeInt(this.SchoolId);
        dest.writeList(this.EvaluateList);
    }

    public StudentDetails() {
    }

    protected StudentDetails(Parcel in) {
        this.Name = in.readString();
        this.Head = in.readString();
        this.Sex = in.readString();
        this.Tel = in.readString();
        this.Status = in.readInt();
        this.CardId = in.readString();
        this.DriverType = in.readString();
        this.BookingAccount = in.readString();
        this.BookingTime = in.readString();
        this.ClassHour = in.readString();
        this.WhenLong = in.readInt();
        this.CalculationTime = in.readInt();
        this.StudentId = in.readInt();
        this.BookingDay = in.readString();
        this.TimeSlot = in.readString();
        this.IsComment = in.readInt();
        this.StudyMinutes = in.readInt();
        this.SchoolId = in.readInt();
        this.EvaluateList = new ArrayList<EvaluateListBean>();
        in.readList(this.EvaluateList, EvaluateListBean.class.getClassLoader());
    }

    public static final Creator<StudentDetails> CREATOR = new Creator<StudentDetails>() {
        @Override
        public StudentDetails createFromParcel(Parcel source) {
            return new StudentDetails(source);
        }

        @Override
        public StudentDetails[] newArray(int size) {
            return new StudentDetails[size];
        }
    };
}
