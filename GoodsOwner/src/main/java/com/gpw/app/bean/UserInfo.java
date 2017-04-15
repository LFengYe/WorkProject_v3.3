package com.gpw.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/18.
 * ---个人专属
 */

public class UserInfo implements Parcelable {

    /**
     * UserId : 41b1502f-d151-45ef-a37f-b97dc80c1332
     * UserName :
     * Tel : 15635849658
     * HeadIco :
     * Sex :
     * Address :
     */

    private String UserId;
    private String UserName;
    private String Tel;
    private String HeadIco;
    private String Sex;
    private String Address;
    private double Balance;
    private double Frozen;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getHeadIco() {
        return HeadIco;
    }

    public void setHeadIco(String HeadIco) {
        this.HeadIco = HeadIco;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public double getFrozen() {
        return Frozen;
    }

    public void setFrozen(double frozen) {
        Frozen = frozen;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "UserId='" + UserId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Tel='" + Tel + '\'' +
                ", HeadIco='" + HeadIco + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserId);
        dest.writeString(this.UserName);
        dest.writeString(this.Tel);
        dest.writeString(this.HeadIco);
        dest.writeString(this.Sex);
        dest.writeString(this.Address);
        dest.writeDouble(this.Balance);
        dest.writeDouble(this.Frozen);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.UserId = in.readString();
        this.UserName = in.readString();
        this.Tel = in.readString();
        this.HeadIco = in.readString();
        this.Sex = in.readString();
        this.Address = in.readString();
        this.Balance = in.readDouble();
        this.Frozen = in.readDouble();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
