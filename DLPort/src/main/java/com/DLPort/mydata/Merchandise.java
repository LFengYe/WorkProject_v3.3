package com.DLPort.mydata;

import java.io.Serializable;

/**
 * Created by fuyzh on 16/5/18.
 */
public class Merchandise implements Serializable {
    private String merchandiseId;
    private String merchandiseName;
    private String merchandiseImage;
    private int merchandisePrice;
    private String merchandiseDescribe;
    private String merchandiseCreateTime;

    public String getMerchandiseId() {
        return merchandiseId;
    }

    public void setMerchandiseId(String merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public String getMerchandiseImage() {
        return merchandiseImage;
    }

    public void setMerchandiseImage(String merchandiseImage) {
        this.merchandiseImage = merchandiseImage;
    }

    public int getMerchandisePrice() {
        return merchandisePrice;
    }

    public void setMerchandisePrice(int merchandisePrice) {
        this.merchandisePrice = merchandisePrice;
    }

    public String getMerchandiseDescribe() {
        return merchandiseDescribe;
    }

    public void setMerchandiseDescribe(String merchandiseDescribe) {
        this.merchandiseDescribe = merchandiseDescribe;
    }

    public String getMerchandiseCreateTime() {
        return merchandiseCreateTime;
    }

    public void setMerchandiseCreateTime(String merchandiseCreateTime) {
        this.merchandiseCreateTime = merchandiseCreateTime;
    }
}
