package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/8.
 */
public class CarOrder {


    private String CreateTimeY;
    private String CreateTimeX;
    private String CarNo;
    private String TripLong;
    private int orderStatusValue;
    private String OrderStatus;
    private String Price;
    private String StartAddress;
    private String Destination;
    private String OrderId;
    private String Trl;

    public CarOrder(String createTimeY,String createTimeX,String carNo,String tripLong,
                    int orderStatusValue, String orderStatus,String price,String startAddress,
                    String destination, String orderId,String Trl){
        this.CreateTimeY=createTimeY;
        this.CreateTimeX =createTimeX;
        this.CarNo = carNo;
        this.TripLong = tripLong;
        this.orderStatusValue = orderStatusValue;
        this.OrderStatus = orderStatus;
        this.Price = price;
        this.StartAddress =startAddress;
        this.Destination = destination;
        this.OrderId = orderId;
    }

    public int getOrderStatusValue() {
        return orderStatusValue;
    }

    public void setOrderStatusValue(int orderStatusValue) {
        this.orderStatusValue = orderStatusValue;
    }

    public void setTrl(String trl) {
        Trl = trl;
    }

    public String getTrl() {
        return Trl;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public void setCreateTimeX(String createTimeX) {
        CreateTimeX = createTimeX;
    }

    public void setCreateTimeY(String createTimeY) {
        CreateTimeY = createTimeY;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setStartAddress(String startAddress) {
        StartAddress = startAddress;
    }
    public void setTripLong(String tripLong) {
        TripLong = tripLong;
    }


    public String getCarNo() {

        return CarNo;
    }

    public String getCreateTimeX() {
        return CreateTimeX;
    }

    public String getCreateTimeY() {
        return CreateTimeY;
    }

    public String getDestination() {
        return Destination;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getPrice() {
        return Price;
    }

    public String getStartAddress() {
        return StartAddress;
    }



    public String getTripLong() {

        return TripLong;
    }
}
