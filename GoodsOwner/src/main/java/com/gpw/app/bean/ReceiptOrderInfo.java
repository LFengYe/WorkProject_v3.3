package com.gpw.app.bean;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * Created by gpy9983 on 2016/12/1.
 */

public class ReceiptOrderInfo {

    /**
     * OrderNo : TO20161111000002
     * PlanSendTime : 2016/11/11 16:35:22
     * Freight : 4000.0
     * Premium : 100.0
     * OrderStatus : 2
     * FinanceStatus : 1
     * LogisticStatus : 1
     * OrderType : 1
     * IsToPay : True
     * OrderAddress : [{"Address":"西丽","Receipter":"张三","Tel":"13530177675"},{"Address":"宝安","Receipter":"李四","Tel":"13530177675"}]
     */

    private String OrderNo;
    private String PlanSendTime;
    private double Freight;
    private double Premium;
    private int OrderStatus;
    private int FinanceStatus;
    private int LogisticStatus;
    private int OrderType;
    private String IsToPay;

    private JsonElement OrderAddress;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getPlanSendTime() {
        return PlanSendTime;
    }

    public void setPlanSendTime(String PlanSendTime) {
        this.PlanSendTime = PlanSendTime;
    }

    public double getFreight() {
        return Freight;
    }

    public void setFreight(double Freight) {
        this.Freight = Freight;
    }

    public double getPremium() {
        return Premium;
    }

    public void setPremium(double Premium) {
        this.Premium = Premium;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public int getFinanceStatus() {
        return FinanceStatus;
    }

    public void setFinanceStatus(int FinanceStatus) {
        this.FinanceStatus = FinanceStatus;
    }

    public int getLogisticStatus() {
        return LogisticStatus;
    }

    public void setLogisticStatus(int LogisticStatus) {
        this.LogisticStatus = LogisticStatus;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int OrderType) {
        this.OrderType = OrderType;
    }

    public String getIsToPay() {
        return IsToPay;
    }

    public void setIsToPay(String IsToPay) {
        this.IsToPay = IsToPay;
    }

    public JsonElement getJsonElement() {
        return OrderAddress;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.OrderAddress = jsonElement;
    }


}
