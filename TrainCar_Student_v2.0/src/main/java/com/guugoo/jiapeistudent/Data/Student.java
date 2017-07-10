package com.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Student {
    private static final String TAG = "Student";


    /**
     * Id : 学员主键
     * Name : 学员姓名
     * Tel : 学员电话（登录名）
     * CurrentSubject : 当前科目
     * SchoolId : 驾校编号
     * Hours :我的学时券数量,
     * IsSign : 是否已报名
     * token :后续操作的验证值,
     * HeadPortrait : 头像
     * Nickname : 昵称
     * CardNo : 身份证号码
     * Sex : 男
     * Address : 地址
     * PracticeCount : 学时
     * Schedule : 进度
     * InvitationCode : 邀请码
     */

    private int Id;
    private String Name;
    private String Tel;
    private int CurrentSubject;
    private int SchoolId;
    private int Hours;
    private boolean IsSign;
    private String bookingId;
    private String token;
    private String HeadPortrait;
    private String Nickname;
    private String CardNo;
    private String Sex;
    private String Address;
    private String PracticeCount;
    private String Schedule;
    private String InvitationCode;


    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public int getCurrentSubject() {
        return CurrentSubject;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setCurrentSubject(int currentSubject) {
        CurrentSubject = currentSubject;
    }

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int SchoolId) {
        this.SchoolId = SchoolId;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int Hours) {
        this.Hours = Hours;
    }

    public boolean isSign() {
        return IsSign;
    }

    public void setSign(boolean sign) {
        IsSign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPracticeCount() {
        return PracticeCount;
    }

    public void setPracticeCount(String PracticeCount) {
        this.PracticeCount = PracticeCount;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String Schedule) {
        this.Schedule = Schedule;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setInvitationCode(String InvitationCode) {
        this.InvitationCode = InvitationCode;
    }
}
