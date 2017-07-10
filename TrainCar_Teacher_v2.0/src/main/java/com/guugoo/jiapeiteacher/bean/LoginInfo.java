package com.guugoo.jiapeiteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gpw on 2016/8/9.
 * --加油
 */
public class LoginInfo implements Parcelable {


    /**
     * Id : 1
     * Name : 教练
     * Tel : 123456789
     * SchoolId : 1
     * Icon : http://101.201.74.192:8001/HeadPortraitImg/T广东_深圳_1_1_20160815163759.JPEG
     * NicKname : ggllyyasd
     * ComprehensiveLevel : 4
     * CardNo : 236987456325485426
     * InvitationCode : T21524141
     * token : 683012ea5dd34d62aaa758ade67635d6
     */

    private int Id;
    private String Name;
    private String Tel;
    private int SchoolId;
    private String Icon;
    private String NicKname;
    private int ComprehensiveLevel;
    private String CardNo;
    private String InvitationCode;
    private String token;

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

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int SchoolId) {
        this.SchoolId = SchoolId;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public String getNicKname() {
        return NicKname;
    }

    public void setNicKname(String NicKname) {
        this.NicKname = NicKname;
    }

    public int getComprehensiveLevel() {
        return ComprehensiveLevel;
    }

    public void setComprehensiveLevel(int ComprehensiveLevel) {
        this.ComprehensiveLevel = ComprehensiveLevel;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setInvitationCode(String InvitationCode) {
        this.InvitationCode = InvitationCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Tel='" + Tel + '\'' +
                ", SchoolId=" + SchoolId +
                ", Icon='" + Icon + '\'' +
                ", NicKname='" + NicKname + '\'' +
                ", ComprehensiveLevel=" + ComprehensiveLevel +
                ", CardNo='" + CardNo + '\'' +
                ", InvitationCode='" + InvitationCode + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeString(this.Name);
        dest.writeString(this.Tel);
        dest.writeInt(this.SchoolId);
        dest.writeString(this.Icon);
        dest.writeString(this.NicKname);
        dest.writeInt(this.ComprehensiveLevel);
        dest.writeString(this.CardNo);
        dest.writeString(this.InvitationCode);
        dest.writeString(this.token);
    }

    public LoginInfo() {
    }

    protected LoginInfo(Parcel in) {
        this.Id = in.readInt();
        this.Name = in.readString();
        this.Tel = in.readString();
        this.SchoolId = in.readInt();
        this.Icon = in.readString();
        this.NicKname = in.readString();
        this.ComprehensiveLevel = in.readInt();
        this.CardNo = in.readString();
        this.InvitationCode = in.readString();
        this.token = in.readString();
    }

    public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel source) {
            return new LoginInfo(source);
        }

        @Override
        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };
}
