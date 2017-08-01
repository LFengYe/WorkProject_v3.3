package cn.com.caronwer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LFeng on 2017/7/11.
 */

public class VehicleType implements Parcelable {
    private float ReturnPayRate;
    private int TypeCode;
    private String VehicleTypeName;
    private String Img;
    private float Volume;
    private float LoadWeight;
    private float StartingPrice;
    private float FollowPrice;
    private String Remark;

    public float getReturnPayRate() {
        return ReturnPayRate;
    }

    public void setReturnPayRate(float returnPayRate) {
        ReturnPayRate = returnPayRate;
    }

    public int getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(int typeCode) {
        TypeCode = typeCode;
    }

    public String getVehicleTypeName() {
        return VehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        VehicleTypeName = vehicleTypeName;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public float getVolume() {
        return Volume;
    }

    public void setVolume(float volume) {
        Volume = volume;
    }

    public float getLoadWeight() {
        return LoadWeight;
    }

    public void setLoadWeight(float loadWeight) {
        LoadWeight = loadWeight;
    }

    public float getStartingPrice() {
        return StartingPrice;
    }

    public void setStartingPrice(float startingPrice) {
        StartingPrice = startingPrice;
    }

    public float getFollowPrice() {
        return FollowPrice;
    }

    public void setFollowPrice(float followPrice) {
        FollowPrice = followPrice;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String toString() {
        return this.VehicleTypeName;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.ReturnPayRate);
        dest.writeInt(this.TypeCode);
        dest.writeString(this.VehicleTypeName);
        dest.writeString(this.Img);
        dest.writeFloat(this.Volume);
        dest.writeFloat(this.LoadWeight);
        dest.writeFloat(this.StartingPrice);
        dest.writeFloat(this.FollowPrice);
        dest.writeString(this.Remark);
    }

    public VehicleType() {
    }

    protected VehicleType(Parcel in) {
        this.ReturnPayRate = in.readFloat();
        this.TypeCode = in.readInt();
        this.VehicleTypeName = in.readString();
        this.Img = in.readString();
        this.Volume = in.readFloat();
        this.LoadWeight = in.readFloat();
        this.StartingPrice = in.readFloat();
        this.FollowPrice = in.readFloat();
        this.Remark = in.readString();
    }

    public static final Parcelable.Creator<VehicleType> CREATOR = new Parcelable.Creator<VehicleType>() {
        @Override
        public VehicleType createFromParcel(Parcel source) {
            return new VehicleType(source);
        }

        @Override
        public VehicleType[] newArray(int size) {
            return new VehicleType[size];
        }
    };
}
