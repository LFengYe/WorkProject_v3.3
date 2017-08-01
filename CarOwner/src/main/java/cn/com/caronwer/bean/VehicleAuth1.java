package cn.com.caronwer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LFeng on 2017/7/12.
 */

public class VehicleAuth1 implements Parcelable {

    private String UserName;
    private String Sex;
    private String IDNumber;
    private String Phone;
    private String DriverId;
    private String VehicleNo;
    private String TravelCard;
    private String GpsNo;
    private String VehType;
    private String Phone1;
    private float Width;
    private float Height;
    private float Length;
    private float Tons;
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    public String getUserName() {
        return UserName;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }
    public String getSex() {
        return Sex;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }
    public String getIDNumber() {
        return IDNumber;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
    public String getPhone() {
        return Phone;
    }

    public void setDriverId(String DriverId) {
        this.DriverId = DriverId;
    }
    public String getDriverId() {
        return DriverId;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }
    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setTravelCard(String TravelCard) {
        this.TravelCard = TravelCard;
    }
    public String getTravelCard() {
        return TravelCard;
    }

    public void setGpsNo(String GpsNo) {
        this.GpsNo = GpsNo;
    }
    public String getGpsNo() {
        return GpsNo;
    }

    public void setVehType(String VehType) {
        this.VehType = VehType;
    }
    public String getVehType() {
        return VehType;
    }

    public void setPhone1(String Phone1) {
        this.Phone1 = Phone1;
    }
    public String getPhone1() {
        return Phone1;
    }

    public void setWidth(float Width) {
        this.Width = Width;
    }
    public float getWidth() {
        return Width;
    }

    public void setHeight(float Height) {
        this.Height = Height;
    }
    public float getHeight() {
        return Height;
    }

    public void setLength(float Length) {
        this.Length = Length;
    }
    public float getLength() {
        return Length;
    }

    public void setTons(float Tons) {
        this.Tons = Tons;
    }
    public float getTons() {
        return Tons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserName);
        dest.writeString(this.Sex);
        dest.writeString(this.IDNumber);
        dest.writeString(this.Phone);
        dest.writeString(this.DriverId);
        dest.writeString(this.VehicleNo);
        dest.writeString(this.TravelCard);
        dest.writeString(this.GpsNo);
        dest.writeString(this.VehType);
        dest.writeString(this.Phone1);
        dest.writeFloat(this.Width);
        dest.writeFloat(this.Height);
        dest.writeFloat(this.Length);
        dest.writeFloat(this.Tons);
    }

    public VehicleAuth1() {
    }

    protected VehicleAuth1(Parcel in) {
        this.UserName = in.readString();
        this.Sex = in.readString();
        this.IDNumber = in.readString();
        this.Phone = in.readString();
        this.DriverId = in.readString();
        this.VehicleNo = in.readString();
        this.TravelCard = in.readString();
        this.GpsNo = in.readString();
        this.VehType = in.readString();
        this.Phone1 = in.readString();
        this.Width = in.readFloat();
        this.Height = in.readFloat();
        this.Length = in.readFloat();
        this.Tons = in.readFloat();
    }

    public static final Parcelable.Creator<VehicleAuth1> CREATOR = new Parcelable.Creator<VehicleAuth1>() {
        @Override
        public VehicleAuth1 createFromParcel(Parcel source) {
            return new VehicleAuth1(source);
        }

        @Override
        public VehicleAuth1[] newArray(int size) {
            return new VehicleAuth1[size];
        }
    };
}
