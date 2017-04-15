package com.DLPort.mydata;

/**
 * Created by fuyzh on 16/5/20.
 */
public class Inform {
    private static final String[] statueName = {"未读", "已读", "已处理"};
    private static final String[] flageName = {"申请取消订单", "订单到达", "", "货主同意取消订单", "货主不同意取消订单"};
    private int id;
    private String carOwnerId;
    private String carGoOwnerId;
    private String msgContent;
    private int msgStatue;
    private String createTime;
    private String orderId;
    private int flage;
    private String carOwnerName;
    private String carGoOwnerName;
    private String carOwnerTel;
    private String carGoOwnerTel;
    private String vehNof;
    private String presentNo;

    public String getCarGoOwnerId() {
        return carGoOwnerId;
    }

    public void setCarGoOwnerId(String carGoOwnerId) {
        this.carGoOwnerId = carGoOwnerId;
    }

    public String getStatueName() {
        if (msgStatue >= 0 && msgStatue <= 2)
            return statueName[msgStatue];
        return null;
    }

    public String getFlageName() {
        if (flage >= 0 && flage <= 4)
            return flageName[flage];
        return null;
    }

    public String getCarGoOwnerName() {
        return carGoOwnerName;
    }

    public void setCarGoOwnerName(String carGoOwnerName) {
        this.carGoOwnerName = carGoOwnerName;
    }

    public String getCarGoOwnerTel() {
        return carGoOwnerTel;
    }

    public void setCarGoOwnerTel(String carGoOwnerTel) {
        this.carGoOwnerTel = carGoOwnerTel;
    }

    public String getCarOwnerId() {
        return carOwnerId;
    }

    public void setCarOwnerId(String carOwnerId) {
        this.carOwnerId = carOwnerId;
    }

    public String getCarOwnerName() {
        return carOwnerName;
    }

    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }

    public String getCarOwnerTel() {
        return carOwnerTel;
    }

    public void setCarOwnerTel(String carOwnerTel) {
        this.carOwnerTel = carOwnerTel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getFlage() {
        return flage;
    }

    public void setFlage(int flage) {
        this.flage = flage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgStatue() {
        return msgStatue;
    }

    public void setMsgStatue(int msgStatue) {
        this.msgStatue = msgStatue;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPresentNo() {
        return presentNo;
    }

    public void setPresentNo(String presentNo) {
        this.presentNo = presentNo;
    }

    public String getVehNof() {
        return vehNof;
    }

    public void setVehNof(String vehNof) {
        this.vehNof = vehNof;
    }
}
