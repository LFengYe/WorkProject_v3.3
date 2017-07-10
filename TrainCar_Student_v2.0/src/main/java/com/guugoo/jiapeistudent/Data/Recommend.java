package com.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/13.
 */
public class Recommend {
    private static final String TAG = "Recommend";


    /**
     * HeadPortrait : 头像
     * Nmae : 姓名
     * Tel : 电话
     * Type : 类型
     *  InvitePeopleBonusAmount : 学员奖励的数量
     *  InvitePeopleIsCash : 是否已经奖励
     *  InvitePeopleRewarType : 奖励的类型（1表示现金奖励，2表示学时券奖励）
     */

    private String HeadPortrait;
    private String Nmae;
    private String Tel;
    private String Type;
    private float InvitePeopleBonusAmount;
    private boolean InvitePeopleIsCash;
    private int InvitePeopleRewarType;

    public Recommend() {
    }

    public Recommend(String headPortrait, String nmae, String tel, String type, int invitePeopleBonusAmount, boolean invitePeopleIsCash, int invitePeopleRewarType) {
        HeadPortrait = headPortrait;
        Nmae = nmae;
        Tel = tel;
        Type = type;
        InvitePeopleBonusAmount = invitePeopleBonusAmount;
        InvitePeopleIsCash = invitePeopleIsCash;
        InvitePeopleRewarType = invitePeopleRewarType;
    }

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

    public float getInvitePeopleBonusAmount() {
        return InvitePeopleBonusAmount;
    }

    public void setInvitePeopleBonusAmount(float invitePeopleBonusAmount) {
        InvitePeopleBonusAmount = invitePeopleBonusAmount;
    }

    public boolean isInvitePeopleIsCash() {
        return InvitePeopleIsCash;
    }

    public void setInvitePeopleIsCash(boolean invitePeopleIsCash) {
        InvitePeopleIsCash = invitePeopleIsCash;
    }

    public int getInvitePeopleRewarType() {
        return InvitePeopleRewarType;
    }

    public void setInvitePeopleRewarType(int invitePeopleRewarType) {
        InvitePeopleRewarType = invitePeopleRewarType;
    }
}
