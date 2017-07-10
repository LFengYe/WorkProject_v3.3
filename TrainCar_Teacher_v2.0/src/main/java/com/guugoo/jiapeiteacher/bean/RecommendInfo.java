package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/8.
 * --加油
 */
public class RecommendInfo {


    /**
     * HeadPortrait : 头像
     * Nmae : 姓名
     * Tel : 电话
     * Type : 类型
     * InvitePeopleBonusAmount : 1
     * InvitePeopleIsCash : true
     * InvitePeopleRewarType : 2
     */

    private String HeadPortrait;
    private String Nmae;
    private String Tel;
    private String Type;
    private int InvitePeopleBonusAmount;
    private boolean InvitePeopleIsCash;
    private int InvitePeopleRewarType;

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public String getNmae() {
        return Nmae;
    }

    public void setNmae(String Nmae) {
        this.Nmae = Nmae;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getInvitePeopleBonusAmount() {
        return InvitePeopleBonusAmount;
    }

    public void setInvitePeopleBonusAmount(int InvitePeopleBonusAmount) {
        this.InvitePeopleBonusAmount = InvitePeopleBonusAmount;
    }

    public boolean isInvitePeopleIsCash() {
        return InvitePeopleIsCash;
    }

    public void setInvitePeopleIsCash(boolean InvitePeopleIsCash) {
        this.InvitePeopleIsCash = InvitePeopleIsCash;
    }

    public int getInvitePeopleRewarType() {
        return InvitePeopleRewarType;
    }

    public void setInvitePeopleRewarType(int InvitePeopleRewarType) {
        this.InvitePeopleRewarType = InvitePeopleRewarType;
    }
}
