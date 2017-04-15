package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/15.
 */
public class HuoInquire {

    private String Principal;
    private String CarNo;
    private String SuitCaseNo;
    private String Tel;
    private int orderStatusValue;
    private String OrderStatus;
    private int chargeStatusValue;
    private String ChargeStatus;
    private String OrderId;
    private String PutBoxID;
    private String PresentNumber;

    public HuoInquire(String Principal, String CarNo, String SuitCaseNo, String Tel,
                      String OrderStatus, String ChargeStatus, String OrderId,
                      int orderStatusValue, int chargeStatusValue,String PutBoxID,String PresentNumber) {
        this.CarNo = CarNo;
        this.Principal = Principal;
        this.SuitCaseNo = SuitCaseNo;
        this.Tel = Tel;
        this.OrderStatus = OrderStatus;
        this.ChargeStatus = ChargeStatus;
        this.OrderId = OrderId;
        this.orderStatusValue = orderStatusValue;
        this.chargeStatusValue = chargeStatusValue;
        this.PutBoxID = PutBoxID;
        this.PresentNumber =PresentNumber;
    }

    public int getChargeStatusValue() {
        return chargeStatusValue;
    }

    public void setChargeStatusValue(int chargeStatusValue) {
        this.chargeStatusValue = chargeStatusValue;
    }

    public int getOrderStatusValue() {
        return orderStatusValue;
    }

    public void setOrderStatusValue(int orderStatusValue) {
        this.orderStatusValue = orderStatusValue;
    }

    public String getPresentNumber() {
        return PresentNumber;
    }

    public void setPresentNumber(String presentNumber) {
        PresentNumber = presentNumber;
    }

    public String getPutBoxID() {
        return PutBoxID;
    }

    public void setPutBoxID(String putBoxID) {
        PutBoxID = putBoxID;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getChargeStatus() {
        return ChargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        ChargeStatus = chargeStatus;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getPrincipal() {
        return Principal;
    }

    public void setPrincipal(String principal) {
        Principal = principal;
    }

    public String getSuitCaseNo() {
        return SuitCaseNo;
    }

    public void setSuitCaseNo(String suitCaseNo) {
        SuitCaseNo = suitCaseNo;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }
}
