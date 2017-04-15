package com.DLPort.mydata;

/**
 * Created by fuyzh on 16/5/21.
 */
public class ActivityMessage {
    private String msgId;
    private String msgTypeA;
    private String msgTypeB;
    private String msgContent;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTypeA() {
        return msgTypeA;
    }

    public void setMsgTypeA(String msgTypeA) {
        this.msgTypeA = msgTypeA;
    }

    public String getMsgTypeB() {
        return msgTypeB;
    }

    public void setMsgTypeB(String msgTypeB) {
        this.msgTypeB = msgTypeB;
    }
}
