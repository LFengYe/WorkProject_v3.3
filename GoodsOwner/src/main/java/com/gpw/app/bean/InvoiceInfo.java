package com.gpw.app.bean;

/**
 * Created by gpy9983 on 2016/11/17.
 */

public class InvoiceInfo {

    /**
     * OrderNo : TO20161110000005
     * OrderAmount : 100.0
     * CreateTime : 2016/11/10 13:13:30
     * StartAddress :
     * EndAddress :
     */

    private String OrderNo;
    private double OrderAmount;
    private String CreateTime;
    private String StartAddress;
    private String EndAddress;
    private boolean ischeck = false;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public double getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(double OrderAmount) {
        this.OrderAmount = OrderAmount;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getStartAddress() {
        return StartAddress;
    }

    public void setStartAddress(String StartAddress) {
        this.StartAddress = StartAddress;
    }

    public String getEndAddress() {
        return EndAddress;
    }

    public void setEndAddress(String EndAddress) {
        this.EndAddress = EndAddress;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
}
