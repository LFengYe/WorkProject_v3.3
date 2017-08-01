package cn.com.caronwer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/18.
 * ---个人专属
 */

public class UserInfo implements Parcelable {

    /**
     * UserId : 41b1502f-d151-45ef-a37f-b97dc80c1332
     * UserName :
     * Tel : 15635849658
     * HeadIco :
     * Sex :
     * Address :
     */

    private String UserId;
    private String UserName;
    private String Tel;
    private String HeadIco;
    private String Sex;
    private String Address;
    private double Balance;
    private double Frozen;
    private int AuthenticateStatus;
    private String IsWork;
    private String VehicleNo;
    private int VehicleType;
    private String VehicleTypeName;
    private int Score;
    private String Token;
    private int AuthenticateStep;
    private String CompanyTel;

    /**
     * Balance : 0.0
     * Frozen : 0.0
     * AuthenticateStatus : 1
     * IsWork : False
     * VehicleNo : 145263

     * VehicleType : 1
     * VehicleTypeName : 小面包车
     * Score : 0
     * Token : 01806E2D-E55E-4172-8139-D41DE60B5955
     */

    public int getAuthenticateStep() {
        return AuthenticateStep;
    }

    public void setAuthenticateStep(int authenticateStep) {
        AuthenticateStep = authenticateStep;
    }

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

    @Override
    public String toString() {
        return "UserInfo{" +
                "UserId='" + UserId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Tel='" + Tel + '\'' +
                ", HeadIco='" + HeadIco + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Address='" + Address + '\'' +
                ", Balance=" + Balance +
                ", Frozen=" + Frozen +
                ", AuthenticateStatus='" + AuthenticateStatus + '\'' +
                ", IsWork='" + IsWork + '\'' +
                ", VehicleNo='" + VehicleNo + '\'' +
                ", VehicleType=" + VehicleType +
                ", VehicleTypeName='" + VehicleTypeName + '\'' +
                ", Score=" + Score +
                ", Token='" + Token + '\'' +
                '}';
    }

    public UserInfo() {
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

    public int getAuthenticateStatus() {
        return AuthenticateStatus;
    }

    public void setAuthenticateStatus(int AuthenticateStatus) {
        this.AuthenticateStatus = AuthenticateStatus;
    }

    public String getIsWork() {
        return IsWork;
    }

    public void setIsWork(String IsWork) {
        this.IsWork = IsWork;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }

    public int getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(int VehicleType) {
        this.VehicleType = VehicleType;
    }

    public String getVehicleTypeName() {
        return VehicleTypeName;
    }

    public void setVehicleTypeName(String VehicleTypeName) {
        this.VehicleTypeName = VehicleTypeName;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
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

    public void setCompanyTel(String companyTel) {
        CompanyTel = companyTel;
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
        dest.writeInt(this.AuthenticateStatus);
        dest.writeString(this.IsWork);
        dest.writeString(this.VehicleNo);
        dest.writeInt(this.VehicleType);
        dest.writeString(this.VehicleTypeName);
        dest.writeInt(this.Score);
        dest.writeString(this.Token);
        dest.writeInt(this.AuthenticateStep);
        dest.writeString(this.CompanyTel);
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
        this.AuthenticateStatus = in.readInt();
        this.IsWork = in.readString();
        this.VehicleNo = in.readString();
        this.VehicleType = in.readInt();
        this.VehicleTypeName = in.readString();
        this.Score = in.readInt();
        this.Token = in.readString();
        this.AuthenticateStep = in.readInt();
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
