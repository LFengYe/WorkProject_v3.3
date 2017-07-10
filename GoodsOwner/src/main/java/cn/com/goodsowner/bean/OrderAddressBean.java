package cn.com.goodsowner.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderAddressBean implements Parcelable {
    private String Address;
    private String Receipter;
    private String Tel;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getReceipter() {
        return Receipter;
    }

    public void setReceipter(String Receipter) {
        this.Receipter = Receipter;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    @Override
    public String toString() {
        return "OrderAddressBean{" +
                "Address='" + Address + '\'' +
                ", Receipter='" + Receipter + '\'' +
                ", Tel='" + Tel + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Address);
        dest.writeString(this.Receipter);
        dest.writeString(this.Tel);
    }

    public OrderAddressBean() {
    }

    protected OrderAddressBean(Parcel in) {
        this.Address = in.readString();
        this.Receipter = in.readString();
        this.Tel = in.readString();
    }

    public static final Parcelable.Creator<OrderAddressBean> CREATOR = new Parcelable.Creator<OrderAddressBean>() {
        @Override
        public OrderAddressBean createFromParcel(Parcel source) {
            return new OrderAddressBean(source);
        }

        @Override
        public OrderAddressBean[] newArray(int size) {
            return new OrderAddressBean[size];
        }
    };
}