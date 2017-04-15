package com.gpw.app.bean;

/**
 * Created by gpy9983 on 2016/11/25.
 */

public class PayAmount {

    /**
     * OrderNo : TO20161110000005
     * PayAmount : 300.0
     */

    private String OrderNo;
    private double PayAmount;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public double getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(double PayAmount) {
        this.PayAmount = PayAmount;
    }
}
