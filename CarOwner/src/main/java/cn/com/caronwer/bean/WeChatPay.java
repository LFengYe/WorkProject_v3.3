package cn.com.caronwer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LFeng on 2017/7/21.
 */

public class WeChatPay implements Parcelable {
    private String timeStamp;
    private String nonceStr;
    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageValue;
    private String sign;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timeStamp);
        dest.writeString(this.nonceStr);
        dest.writeString(this.appId);
        dest.writeString(this.partnerId);
        dest.writeString(this.prepayId);
        dest.writeString(this.packageValue);
        dest.writeString(this.sign);
    }

    public WeChatPay() {
    }

    protected WeChatPay(Parcel in) {
        this.timeStamp = in.readString();
        this.nonceStr = in.readString();
        this.appId = in.readString();
        this.partnerId = in.readString();
        this.prepayId = in.readString();
        this.packageValue = in.readString();
        this.sign = in.readString();
    }

    public static final Parcelable.Creator<WeChatPay> CREATOR = new Parcelable.Creator<WeChatPay>() {
        @Override
        public WeChatPay createFromParcel(Parcel source) {
            return new WeChatPay(source);
        }

        @Override
        public WeChatPay[] newArray(int size) {
            return new WeChatPay[size];
        }
    };
}
