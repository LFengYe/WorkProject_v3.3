package com.DLPort.mydata;

import java.io.Serializable;

/**
 * Created by fuyzh on 16/7/9.
 */
public class CarLocation implements Serializable {
    private int carId;
    private String carOwnerId;
    private String carNumber;
    private String carType;
    private String tunnage;//承载吨数
    private String carMessage;
    private String upkeepTime;//
    private String insuranceTime;
    private String gpsExpire;
    private boolean isMortgage;
    private String boxType;
    private String gpsNo;
    private float longitude;
    private float latitude;
    private String status;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarMessage() {
        return carMessage;
    }

    public void setCarMessage(String carMessage) {
        this.carMessage = carMessage;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
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

    public String getGpsExpire() {
        return gpsExpire;
    }

    public void setGpsExpire(String gpsExpire) {
        this.gpsExpire = gpsExpire;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }

    public String getInsuranceTime() {
        return insuranceTime;
    }

    public void setInsuranceTime(String insuranceTime) {
        this.insuranceTime = insuranceTime;
    }

    public boolean getIsMortgage() {
        return isMortgage;
    }

    public void setIsMortgage(boolean isMortgage) {
        this.isMortgage = isMortgage;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTunnage() {
        return tunnage;
    }

    public void setTunnage(String tunnage) {
        this.tunnage = tunnage;
    }

    public String getUpkeepTime() {
        return upkeepTime;
    }

    public void setUpkeepTime(String upkeepTime) {
        this.upkeepTime = upkeepTime;
    }
}
