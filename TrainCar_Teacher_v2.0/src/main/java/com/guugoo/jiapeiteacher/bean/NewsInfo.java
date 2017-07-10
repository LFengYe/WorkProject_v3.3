package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class NewsInfo {

    /**
     * MessageContent : 消息内容
     * CreateTime : 2016-8-8 12:00:00
     *  IsRead : true
     */

    private String MessageContent;
    private String CreateTime;
    private boolean IsRead;

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String MessageContent) {
        this.MessageContent = MessageContent;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    @Override
    public String toString() {
        return "NewsInfo{" +
                "MessageContent='" + MessageContent + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", IsRead=" + IsRead +
                '}';
    }
}
