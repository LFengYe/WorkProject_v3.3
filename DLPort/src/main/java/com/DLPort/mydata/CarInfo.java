package com.DLPort.mydata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LFeng on 16/5/18.
 */
public class CarInfo implements Parcelable {
    private int carId;
    private String carOwnerId;
    private String vehNof;
    private String carType;//车型
    private String upKeepTime;//保养日期
    private String insuranceTime;//保险日期
    private String gpsExpiredTime;//GPS到期日期
    private String gpsNumber;
    private String boxType;

    public CarInfo() {

    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarOwnerId() {
        return carOwnerId;
    }

    public void setCarOwnerId(String carOwnerId) {
        this.carOwnerId = carOwnerId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getGpsNumber() {
        return gpsNumber;
    }

    public void setGpsNumber(String gpsNumber) {
        this.gpsNumber = gpsNumber;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getVehNof() {
        return vehNof;
    }

    public void setVehNof(String vehNof) {
        this.vehNof = vehNof;
    }

    public String getUpKeepTime() {
        return upKeepTime;
    }

    public void setUpKeepTime(String upKeepTime) {
        this.upKeepTime = upKeepTime;
    }

    public String getInsuranceTime() {
        return insuranceTime;
    }

    public void setInsuranceTime(String insuranceTime) {
        this.insuranceTime = insuranceTime;
    }

    public String getGpsExpiredTime() {
        return gpsExpiredTime;
    }

    public void setGpsExpiredTime(String gpsExpiredTime) {
        this.gpsExpiredTime = gpsExpiredTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.carId);
        dest.writeString(this.carOwnerId);
        dest.writeString(this.vehNof);
        dest.writeString(this.carType);
        dest.writeString(this.upKeepTime);
        dest.writeString(this.insuranceTime);
        dest.writeString(this.gpsExpiredTime);
        dest.writeString(this.gpsNumber);
        dest.writeString(this.boxType);
    }

    protected CarInfo(Parcel in) {
        this.carId = in.readInt();
        this.carOwnerId = in.readString();
        this.vehNof = in.readString();
        this.carType = in.readString();
        this.upKeepTime = in.readString();
        this.insuranceTime = in.readString();
        this.gpsExpiredTime = in.readString();
        this.gpsNumber = in.readString();
        this.boxType = in.readString();
    }

    public static final Parcelable.Creator<CarInfo> CREATOR = new Parcelable.Creator<CarInfo>() {
        @Override
        public CarInfo createFromParcel(Parcel source) {
            return new CarInfo(source);
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };
}
