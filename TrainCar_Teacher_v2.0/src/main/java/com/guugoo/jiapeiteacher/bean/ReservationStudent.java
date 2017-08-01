package com.guugoo.jiapeiteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LFeng on 2017/5/27.
 */

public class ReservationStudent implements Parcelable {

    private String Name;
    private String StudentId;
    private String StudentTel;
    private String CardNo;
    private int IsComment;
    private int Issign;
    private int Status;

    public String getStudentTel() {
        return StudentTel;
    }

    public void setStudentTel(String studentTel) {
        StudentTel = studentTel;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public int getIsComment() {
        return IsComment;
    }

    public void setIsComment(int isComment) {
        IsComment = isComment;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getIssign() {
        return Issign;
    }

    public void setIssign(int issign) {
        Issign = issign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.StudentId);
        dest.writeString(this.StudentTel);
        dest.writeString(this.CardNo);
        dest.writeInt(this.IsComment);
        dest.writeInt(this.Status);
        dest.writeInt(this.Issign);
    }

    public ReservationStudent() {
    }

    protected ReservationStudent(Parcel in) {
        this.Name = in.readString();
        this.StudentId = in.readString();
        this.StudentTel = in.readString();
        this.CardNo = in.readString();
        this.IsComment = in.readInt();
        this.Status = in.readInt();
        this.Issign = in.readInt();
    }

    public static final Parcelable.Creator<ReservationStudent> CREATOR = new Parcelable.Creator<ReservationStudent>() {
        @Override
        public ReservationStudent createFromParcel(Parcel source) {
            return new ReservationStudent(source);
        }

        @Override
        public ReservationStudent[] newArray(int size) {
            return new ReservationStudent[size];
        }
    };
}
