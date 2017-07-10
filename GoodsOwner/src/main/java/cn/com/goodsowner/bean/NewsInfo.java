package cn.com.goodsowner.bean;

/**
 * Created by Administrator on 2016/11/18.
 * ---个人专属
 */

public class NewsInfo {

    /**
     * MsgId : 5fbcfa49-d60b-409e-aa87-a2f43b32ec22
     * MsgType : 1
     * MsgTitle : 公告
     * MsgContent : 公告内容
     * CreateTime : 2016/11/11 0:00:00
     */

    private String MsgId;
    private String MsgType;
    private String MsgTitle;
    private String MsgContent;
    private String CreateTime;

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }

    public String getMsgTitle() {
        return MsgTitle;
    }

    public void setMsgTitle(String MsgTitle) {
        this.MsgTitle = MsgTitle;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(String MsgContent) {
        this.MsgContent = MsgContent;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
