package com.DLPort.mydata;

/**
 * Created by fuyzh on 16/6/17.
 */
public class PickUpCashRecord {
    private int pickUpId;
    private float pickUpAmount;
    private String cardNumber;
    private String name;
    private String tel;
    private String bankName;
    private String createTime;
    private String state;
    private String userId;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPickUpAmount() {
        return pickUpAmount;
    }

    public void setPickUpAmount(float pickUpAmount) {
        this.pickUpAmount = pickUpAmount;
    }

    public int getPickUpId() {
        return pickUpId;
    }

    public void setPickUpId(int pickUpId) {
        this.pickUpId = pickUpId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
