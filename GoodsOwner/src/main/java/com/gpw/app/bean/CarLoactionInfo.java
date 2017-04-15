package com.gpw.app.bean;

/**
 * Created by Administrator on 2016/11/24.
 * ---个人专属
 */

public class CarLoactionInfo {

    /**
     * VehicleNo : 粤B12345
     * GpsNo : Gps编号
     * Lat : 44.424541
     * Lng : 116.54124
     * Speed : 54.21
     * Angle : 120
     * Location : 1
     */

    private String VehicleNo;
    private String GpsNo;
    private double Lat;
    private double Lng;
    private double Speed;
    private int Angle;
    private int Location;

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }

    public String getGpsNo() {
        return GpsNo;
    }

    public void setGpsNo(String GpsNo) {
        this.GpsNo = GpsNo;
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

    public double getSpeed() {
        return Speed;
    }

    public void setSpeed(double Speed) {
        this.Speed = Speed;
    }

    public int getAngle() {
        return Angle;
    }

    public void setAngle(int Angle) {
        this.Angle = Angle;
    }

    public int getLocation() {
        return Location;
    }

    public void setLocation(int Location) {
        this.Location = Location;
    }
}
