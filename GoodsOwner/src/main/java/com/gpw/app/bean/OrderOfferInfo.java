package com.gpw.app.bean;

/**
 * Created by gpy9983 on 2016/11/22.
 */

public class OrderOfferInfo {

    /**
     * TransporterId : a1777987-fc90-4ce3-808d-79f13582b7d2
     * TransporterName : 车主
     * Tel : 135*******5
     * VehicleNo : 粤B1***5
     * Offer : 200.0
     * Distance : 0.00km
     * OfferTime : 2016/11/10 9:57:17
     */

    private String TransporterId;
    private String TransporterName;
    private String Tel;
    private String VehicleNo;
    private double Offer;
    private String Distance;
    private String OfferTime;

    public String getTransporterId() {
        return TransporterId;
    }

    public void setTransporterId(String TransporterId) {
        this.TransporterId = TransporterId;
    }

    public String getTransporterName() {
        return TransporterName;
    }

    public void setTransporterName(String TransporterName) {
        this.TransporterName = TransporterName;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }

    public double getOffer() {
        return Offer;
    }

    public void setOffer(double Offer) {
        this.Offer = Offer;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String Distance) {
        this.Distance = Distance;
    }

    public String getOfferTime() {
        return OfferTime;
    }

    public void setOfferTime(String OfferTime) {
        this.OfferTime = OfferTime;
    }
}
