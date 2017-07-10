package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class OwnInfo {

    /**
     * Name : 姓名
     * HeadImg : 头像
     * TeachingSum : 100
     * Score : 5
     * InvitationCode : 邀请码
     * CustomerService : 客服电话
     */

    private String Name;
    private String HeadImg;
    private String TeachingSum;
    private int Score;
    private String InvitationCode;
    private String CustomerService;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String HeadImg) {
        this.HeadImg = HeadImg;
    }

    public String getTeachingSum() {
        return TeachingSum;
    }

    public void setTeachingSum(String TeachingSum) {
        this.TeachingSum = TeachingSum;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setInvitationCode(String InvitationCode) {
        this.InvitationCode = InvitationCode;
    }

    public String getCustomerService() {
        return CustomerService;
    }

    public void setCustomerService(String CustomerService) {
        this.CustomerService = CustomerService;
    }

    @Override
    public String toString() {
        return "OwnInfo{" +
                "Name='" + Name + '\'' +
                ", HeadImg='" + HeadImg + '\'' +
                ", TeachingSum=" + TeachingSum +
                ", Score=" + Score +
                ", InvitationCode='" + InvitationCode + '\'' +
                ", CustomerService='" + CustomerService + '\'' +
                '}';
    }
}
