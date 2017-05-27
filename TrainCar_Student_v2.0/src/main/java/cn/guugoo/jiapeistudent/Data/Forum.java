package cn.guugoo.jiapeistudent.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class Forum implements Parcelable{


    /**
     * DeliverId : 1
     * StudentHeadPortrait : 学员头像
     * Nickname : 昵称
     * CreateTime : 2016-7-20 10:00:00
     * Content : 帖子内容
     * ZambiaNumber : 10
     * CommentNumber : 10
     * StudentId : S1
     * FirstTime : 查询第一页的时间
     * IsZambia : true
     */

    private int DeliverId;
    private String StudentHeadPortrait;
    private String Nickname;
    private String CreateTime;
    private String Content;
    private int ZambiaNumber;
    private int CommentNumber;
    private String StudentId;
    private String FirstTime;
    private boolean IsZambia;

    public int getDeliverId() {
        return DeliverId;
    }

    public void setDeliverId(int DeliverId) {
        this.DeliverId = DeliverId;
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

    public boolean getIsZambia() {
        return IsZambia;
    }

    public void setIsZambia(boolean IsZambia) {
        this.IsZambia = IsZambia;
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
