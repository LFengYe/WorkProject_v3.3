package com.gpw.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述：广告信息</br>
 *
 * @author Eden Cheng</br>
 * @version 2015年4月23日 上午11:32:53
 */
public class ADInfo implements Parcelable {


    /**
     * Id : 1
     * ImgPath : /Advertisings/0.jpg
     * Url :
     */

    private int Id;
    private String ImgPath;
    private String Url;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String ImgPath) {
        this.ImgPath = ImgPath;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }


    @Override
    public String toString() {
        return "ADInfo{" +
                "Id=" + Id +
                ", ImgPath='" + ImgPath + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeString(this.ImgPath);
        dest.writeString(this.Url);
    }

    public ADInfo() {
    }

    protected ADInfo(Parcel in) {
        this.Id = in.readInt();
        this.ImgPath = in.readString();
        this.Url = in.readString();
    }

    public static final Parcelable.Creator<ADInfo> CREATOR = new Parcelable.Creator<ADInfo>() {
        @Override
        public ADInfo createFromParcel(Parcel source) {
            return new ADInfo(source);
        }

        @Override
        public ADInfo[] newArray(int size) {
            return new ADInfo[size];
        }
    };
}
