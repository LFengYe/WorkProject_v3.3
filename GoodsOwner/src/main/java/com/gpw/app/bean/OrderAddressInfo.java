package com.gpw.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/16.
 * ---个人专属
 */

public class OrderAddressInfo implements Parcelable {
    private String Receipter;
    private String ReceiptTel;
    private String ReceiptAddress;
    private double Lat;
    private double Lng;
    private int state;
    private int action;
    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public String getReceiptAddress() {
        return ReceiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        ReceiptAddress = receiptAddress;
    }

    public String getReceiptTel() {
        return ReceiptTel;
    }

    public void setReceiptTel(String receiptTel) {
        ReceiptTel = receiptTel;
    }

    public String getReceipter() {
        return Receipter;
    }

    public void setReceipter(String receipter) {
        Receipter = receipter;
    }

    @Override
    public String toString() {

        return ReceiptAddress + "," +
                Receipter + "," +
                ReceiptTel + "," +
                Lng + "," +
                Lat + "," + money;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Receipter);
        dest.writeString(this.ReceiptTel);
        dest.writeString(this.ReceiptAddress);
        dest.writeDouble(this.Lat);
        dest.writeDouble(this.Lng);
        dest.writeInt(this.state);
        dest.writeInt(this.action);
        dest.writeDouble(this.money);
    }

    public OrderAddressInfo() {
    }

    protected OrderAddressInfo(Parcel in) {
        this.Receipter = in.readString();
        this.ReceiptTel = in.readString();
        this.ReceiptAddress = in.readString();
        this.Lat = in.readDouble();
        this.Lng = in.readDouble();
        this.state = in.readInt();
        this.action = in.readInt();
        this.money = in.readDouble();
    }

    public static final Parcelable.Creator<OrderAddressInfo> CREATOR = new Parcelable.Creator<OrderAddressInfo>() {
        @Override
        public OrderAddressInfo createFromParcel(Parcel source) {
            return new OrderAddressInfo(source);
        }

        @Override
        public OrderAddressInfo[] newArray(int size) {
            return new OrderAddressInfo[size];
        }
    };
}
