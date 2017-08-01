package cn.com.goodsowner.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/18.
 * ---个人专属
 */

public class UserInfo implements Parcelable {


    /**
     * UserId : b6953c0e-7d43-4caa-aa2c-3fe31cbce62a
     * UserName : ggg
     * Tel : 15271417050
     * HeadIco : /SenderHeadPortrait/b6953c0e-7d43-4caa-aa2c-3fe31cbce62a20170707132926.JPEG
     * Sex : 男
     * Address :
     * Balance : 9.9999977513E8
     * Frozen : 0.0
     * AuthenticateStatus : 0
     * Token : 82C006A6-A471-4DC2-973C-8A99B986A833
     * CompanyTel : 4008881234
     */

    private String UserId;
    private String UserName;
    private String Tel;
    private String HeadIco;
    private String Sex;
    private String Address;
    private double Balance;
    private double Frozen;
    private String AuthenticateStatus;
    private String Token;
    private String CompanyTel;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getHeadIco() {
        return HeadIco;
    }

    public void setHeadIco(String HeadIco) {
        this.HeadIco = HeadIco;
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

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public double getFrozen() {
        return Frozen;
    }

    public void setFrozen(double Frozen) {
        this.Frozen = Frozen;
    }

    public String getAuthenticateStatus() {
        return AuthenticateStatus;
    }

    public void setAuthenticateStatus(String AuthenticateStatus) {
        this.AuthenticateStatus = AuthenticateStatus;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getCompanyTel() {
        return CompanyTel;
    }

    public void setCompanyTel(String CompanyTel) {
        this.CompanyTel = CompanyTel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserId);
        dest.writeString(this.UserName);
        dest.writeString(this.Tel);
        dest.writeString(this.HeadIco);
        dest.writeString(this.Sex);
        dest.writeString(this.Address);
        dest.writeDouble(this.Balance);
        dest.writeDouble(this.Frozen);
        dest.writeString(this.AuthenticateStatus);
        dest.writeString(this.Token);
        dest.writeString(this.CompanyTel);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.UserId = in.readString();
        this.UserName = in.readString();
        this.Tel = in.readString();
        this.HeadIco = in.readString();
        this.Sex = in.readString();
        this.Address = in.readString();
        this.Balance = in.readDouble();
        this.Frozen = in.readDouble();
        this.AuthenticateStatus = in.readString();
        this.Token = in.readString();
        this.CompanyTel = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
