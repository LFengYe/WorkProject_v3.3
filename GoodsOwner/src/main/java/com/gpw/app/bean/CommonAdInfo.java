package com.gpw.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/14.
 * ---个人专属
 */

public class CommonAdInfo implements Parcelable {

    /**
     * AddressId : 1
     * Receipter : 张三
     * ReceiptTel : 13532148888
     * ReceiptAddress : 深圳市南山区大学城
     * Lat : 22.124545
     * Lng : 113.454514
     */

    private int AddressId;
    private String Receipter;
    private String ReceiptTel;
    private String ReceiptAddress;
    private double Lat;
    private double Lng;



    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int AddressId) {
        this.AddressId = AddressId;
    }

    public String getReceipter() {
        return Receipter;
    }

    public void setReceipter(String Receipter) {
        this.Receipter = Receipter;
    }

    public String getReceiptTel() {
        return ReceiptTel;
    }

    public void setReceiptTel(String ReceiptTel) {
        this.ReceiptTel = ReceiptTel;
    }

    public String getReceiptAddress() {
        return ReceiptAddress;
    }

    public void setReceiptAddress(String ReceiptAddress) {
        this.ReceiptAddress = ReceiptAddress;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double Lat) {
        this.Lat = Lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double Lng) {
        this.Lng = Lng;
    }

    @Override
    public String toString() {
        return "CommonAdInfo{" +
                "AddressId=" + AddressId +
                ", Receipter='" + Receipter + '\'' +
                ", ReceiptTel='" + ReceiptTel + '\'' +
                ", ReceiptAddress='" + ReceiptAddress + '\'' +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.AddressId);
        dest.writeString(this.Receipter);
        dest.writeString(this.ReceiptTel);
        dest.writeString(this.ReceiptAddress);
        dest.writeDouble(this.Lat);
        dest.writeDouble(this.Lng);
    }

    public CommonAdInfo() {
    }

    protected CommonAdInfo(Parcel in) {
        this.AddressId = in.readInt();
        this.Receipter = in.readString();
        this.ReceiptTel = in.readString();
        this.ReceiptAddress = in.readString();
        this.Lat = in.readDouble();
        this.Lng = in.readDouble();
    }

    public static final Creator<CommonAdInfo> CREATOR = new Creator<CommonAdInfo>() {
        @Override
        public CommonAdInfo createFromParcel(Parcel source) {
            return new CommonAdInfo(source);
        }

        @Override
        public CommonAdInfo[] newArray(int size) {
            return new CommonAdInfo[size];
        }
    };
}
