package com.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/13.
 */
public class MyInformation {
    private static final String TAG = "MyInformation";

    /**
     * HeadPortrait : 头像
     * PracticeCount : 我的学时总数
     * Coupon : 我的学时券数量
     *  Schedule : 当前进度
     * InvitationCode : 邀请码
     *  Customerservice : 客服电话
     */

    private String HeadPortrait;
    private String PracticeCount;
    private int Coupon;
    private String Schedule;
    private String InvitationCode;
    private String Customerservice;

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public String getPracticeCount() {
        return PracticeCount;
    }

    public void setPracticeCount(String PracticeCount) {
        this.PracticeCount = PracticeCount;
    }

    public int getCoupon() {
        return Coupon;
    }

    public void setCoupon(int Coupon) {
        this.Coupon = Coupon;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        InvitationCode = invitationCode;
    }

    public String getCustomerservice() {
        return Customerservice;
    }

    public void setCustomerservice(String customerservice) {
        Customerservice = customerservice;
    }
}
