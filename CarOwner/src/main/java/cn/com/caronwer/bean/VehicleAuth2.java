package cn.com.caronwer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LFeng on 2017/7/12.
 */

public class VehicleAuth2 implements Parcelable {
    private String Sfz;
    private String Driver;
    private String CarFront;
    private String CarBack;
    private String CarLeft;
    private String CarRight;
    public void setSfz(String Sfz) {
        this.Sfz = Sfz;
    }
    public String getSfz() {
        return Sfz;
    }

    public void setDriver(String Driver) {
        this.Driver = Driver;
    }
    public String getDriver() {
        return Driver;
    }

    public void setCarFront(String CarFront) {
        this.CarFront = CarFront;
    }
    public String getCarFront() {
        return CarFront;
    }

    public void setCarBack(String CarBack) {
        this.CarBack = CarBack;
    }
    public String getCarBack() {
        return CarBack;
    }

    public void setCarLeft(String CarLeft) {
        this.CarLeft = CarLeft;
    }
    public String getCarLeft() {
        return CarLeft;
    }

    public void setCarRight(String CarRight) {
        this.CarRight = CarRight;
    }
    public String getCarRight() {
        return CarRight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Sfz);
        dest.writeString(this.Driver);
        dest.writeString(this.CarFront);
        dest.writeString(this.CarBack);
        dest.writeString(this.CarLeft);
        dest.writeString(this.CarRight);
    }

    public VehicleAuth2() {
    }

    protected VehicleAuth2(Parcel in) {
        this.Sfz = in.readString();
        this.Driver = in.readString();
        this.CarFront = in.readString();
        this.CarBack = in.readString();
        this.CarLeft = in.readString();
        this.CarRight = in.readString();
    }

    public static final Parcelable.Creator<VehicleAuth2> CREATOR = new Parcelable.Creator<VehicleAuth2>() {
        @Override
        public VehicleAuth2 createFromParcel(Parcel source) {
            return new VehicleAuth2(source);
        }

        @Override
        public VehicleAuth2[] newArray(int size) {
            return new VehicleAuth2[size];
        }
    };
}
