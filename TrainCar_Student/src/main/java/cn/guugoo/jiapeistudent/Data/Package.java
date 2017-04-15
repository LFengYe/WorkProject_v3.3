package cn.guugoo.jiapeistudent.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/8/7.
 */
public class Package implements Parcelable {

    /**
     *  PackageName：套餐名称
     *  Introduction：套餐简介
     *  Price:该套餐的报名价格
     *  Id:套餐编号
     *  FreeHours:该套餐免费的学时，
     */

    private String PackageName;
    private String Introduction;
    private String Price;
    private String Id;
    private int FreeHours;

    public Package() {

    }



    public Package(String packageName, String introduction, String price, String id, int freeHours) {
        PackageName = packageName;
        Introduction = introduction;
        Price = price;
        Id = id;
        FreeHours = freeHours;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getFreeHours() {
        return FreeHours;
    }

    public void setFreeHours(int freeHours) {
        FreeHours = freeHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PackageName);
        dest.writeString(this.Introduction);
        dest.writeString(this.Price);
        dest.writeString(this.Id);
        dest.writeInt(this.FreeHours);
    }


    protected Package(Parcel in) {
        this.PackageName = in.readString();
        this.Introduction = in.readString();
        this.Price = in.readString();
        this.Id = in.readString();
        this.FreeHours = in.readInt();
    }

    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel source) {
            return new Package(source);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}
