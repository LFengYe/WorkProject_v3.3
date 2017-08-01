package com.guugoo.jiapeistudent.Data;

/**
 * Created by LFeng on 2017/7/26.
 */

public class CouponDetail {
    private String CurrentSubject;
    private String CumulativeHours;

    public String getCurrentSubject() {
        return CurrentSubject;
    }

    public void setCurrentSubject(String currentSubject) {
        CurrentSubject = currentSubject;
    }

    public String getCumulativeHours() {
        return CumulativeHours;
    }

    public void setCumulativeHours(String cumulativeHours) {
        CumulativeHours = cumulativeHours;
    }
}
