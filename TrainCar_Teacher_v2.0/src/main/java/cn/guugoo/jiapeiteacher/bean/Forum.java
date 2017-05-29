package cn.guugoo.jiapeiteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class Forum implements Parcelable {


    /**
     * DeliverId : 5
     * IsZambia : False
     * StudentHeadPortrait : http://101.201.74.192:8001/HeadPortraitImg/
     * Nickname : 13655547856
     * CreateTime : 2016/7/28 9:37:34
     * Content : 发帖
     * ZambiaNumber : 1
     * CommentNumber : 0
     * StudentId : S4
     * FirstTime : 2016/8/11 12:04:44
     */

    private int DeliverId;
    private boolean IsZambia;
    private String StudentHeadPortrait;
    private String Nickname;
    private String CreateTime;
    private String Content;
    private int ZambiaNumber;
    private int CommentNumber;
    private String StudentId;
    private String FirstTime;

    public int getDeliverId() {
        return DeliverId;
    }

    public void setDeliverId(int DeliverId) {
        this.DeliverId = DeliverId;
    }

    public boolean getIsZambia() {
        return IsZambia;
    }

    public void setIsZambia(boolean IsZambia) {
        this.IsZambia = IsZambia;
    }

    public String getStudentHeadPortrait() {
        return StudentHeadPortrait;
    }

    public void setStudentHeadPortrait(String StudentHeadPortrait) {
        this.StudentHeadPortrait = StudentHeadPortrait;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public int getZambiaNumber() {
        return ZambiaNumber;
    }

    public void setZambiaNumber(int ZambiaNumber) {
        this.ZambiaNumber = ZambiaNumber;
    }

    public int getCommentNumber() {
        return CommentNumber;
    }

    public void setCommentNumber(int CommentNumber) {
        this.CommentNumber = CommentNumber;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String StudentId) {
        this.StudentId = StudentId;
    }

    public String getFirstTime() {
        return FirstTime;
    }

    public void setFirstTime(String FirstTime) {
        this.FirstTime = FirstTime;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "DeliverId=" + DeliverId +
                ", IsZambia=" + IsZambia +
                ", StudentHeadPortrait='" + StudentHeadPortrait + '\'' +
                ", Nickname='" + Nickname + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", Content='" + Content + '\'' +
                ", ZambiaNumber=" + ZambiaNumber +
                ", CommentNumber=" + CommentNumber +
                ", StudentId='" + StudentId + '\'' +
                ", FirstTime='" + FirstTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.DeliverId);
        dest.writeByte(this.IsZambia ? (byte) 1 : (byte) 0);
        dest.writeString(this.StudentHeadPortrait);
        dest.writeString(this.Nickname);
        dest.writeString(this.CreateTime);
        dest.writeString(this.Content);
        dest.writeInt(this.ZambiaNumber);
        dest.writeInt(this.CommentNumber);
        dest.writeString(this.StudentId);
        dest.writeString(this.FirstTime);
    }

    public Forum() {
    }

    protected Forum(Parcel in) {
        this.DeliverId = in.readInt();
        this.IsZambia = in.readByte() != 0;
        this.StudentHeadPortrait = in.readString();
        this.Nickname = in.readString();
        this.CreateTime = in.readString();
        this.Content = in.readString();
        this.ZambiaNumber = in.readInt();
        this.CommentNumber = in.readInt();
        this.StudentId = in.readString();
        this.FirstTime = in.readString();
    }

    public static final Parcelable.Creator<Forum> CREATOR = new Parcelable.Creator<Forum>() {
        @Override
        public Forum createFromParcel(Parcel source) {
            return new Forum(source);
        }

        @Override
        public Forum[] newArray(int size) {
            return new Forum[size];
        }
    };
}
