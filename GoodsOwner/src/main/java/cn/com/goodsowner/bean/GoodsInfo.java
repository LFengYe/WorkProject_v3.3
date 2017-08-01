package cn.com.goodsowner.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by gpw on 2017/7/12.
 */

public class GoodsInfo implements Parcelable {
    private String type;
    private String name;
    private String pack;
    private String volume;
    private String kg;
    private String time;
    private boolean isMove;
    private String remark;
    private ArrayList<Addre> mAddres;

    public static class Addre implements Parcelable {
        private String ad_place;
        private String ad_city;
        private String ad_name;
        private String ad_tel;


        public Addre() {
        }

        public String getAd_place() {
            return ad_place;
        }

        public void setAd_place(String ad_place) {
            this.ad_place = ad_place;
        }

        public String getAd_city() {
            return ad_city;
        }

        public void setAd_city(String ad_city) {
            this.ad_city = ad_city;
        }

        public String getAd_name() {
            return ad_name;
        }

        public void setAd_name(String ad_name) {
            this.ad_name = ad_name;
        }

        public String getAd_tel() {
            return ad_tel;
        }

        public void setAd_tel(String ad_tel) {
            this.ad_tel = ad_tel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ad_place);
            dest.writeString(this.ad_city);
            dest.writeString(this.ad_name);
            dest.writeString(this.ad_tel);
        }

        protected Addre(Parcel in) {
            this.ad_place = in.readString();
            this.ad_city = in.readString();
            this.ad_name = in.readString();
            this.ad_tel = in.readString();
        }

        public static final Creator<Addre> CREATOR = new Creator<Addre>() {
            @Override
            public Addre createFromParcel(Parcel source) {
                return new Addre(source);
            }

            @Override
            public Addre[] newArray(int size) {
                return new Addre[size];
            }
        };
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ArrayList<Addre> getAddres() {
        return mAddres;
    }

    public void setAddres(ArrayList<Addre> addres) {
        mAddres = addres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.pack);
        dest.writeString(this.volume);
        dest.writeString(this.kg);
        dest.writeString(this.time);
        dest.writeByte(this.isMove ? (byte) 1 : (byte) 0);
        dest.writeString(this.remark);
        dest.writeList(this.mAddres);
    }

    public GoodsInfo() {
    }

    protected GoodsInfo(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
        this.pack = in.readString();
        this.volume = in.readString();
        this.kg = in.readString();
        this.time = in.readString();
        this.isMove = in.readByte() != 0;
        this.remark = in.readString();
        this.mAddres = new ArrayList<Addre>();
        in.readList(this.mAddres, Addre.class.getClassLoader());
    }

    public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
        @Override
        public GoodsInfo createFromParcel(Parcel source) {
            return new GoodsInfo(source);
        }

        @Override
        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };
}
