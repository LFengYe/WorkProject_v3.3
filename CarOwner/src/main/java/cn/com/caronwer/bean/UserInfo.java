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

    public UserInfo(String userId, String userName, String tel, String headIco, String sex, String address, double balance, double frozen, String authenticateStatus, String isWork, String vehicleNo, int vehicleType, String vehicleTypeName, int score, String token) {
        UserId = userId;
        UserName = userName;
        Tel = tel;
        HeadIco = headIco;
        Sex = sex;
        Address = address;
        Balance = balance;
        Frozen = frozen;
        AuthenticateStatus = authenticateStatus;
        IsWork = isWork;
        VehicleNo = vehicleNo;
        VehicleType = vehicleType;
        VehicleTypeName = vehicleTypeName;
        Score = score;
        Token = token;
    }

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

    private double Balance;
    private double Frozen;
    private String AuthenticateStatus;
    private String IsWork;
    private String VehicleNo;
    private int VehicleType;
    private String VehicleTypeName;
    private int Score;
    private String Token;





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
    public int describeContents() {
        return 0;
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
        dest.writeString(this.IsWork);
        dest.writeString(this.VehicleNo);
        dest.writeInt(this.VehicleType);
        dest.writeString(this.VehicleTypeName);
        dest.writeInt(this.Score);
        dest.writeString(this.Token);



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
        this.IsWork = in.readString();
        this.VehicleNo = in.readString();
        this.VehicleType = in.readInt();
        this.VehicleTypeName = in.readString();
        this.Score = in.readInt();
        this.Token = in.readString();



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
}
