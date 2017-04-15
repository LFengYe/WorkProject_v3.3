package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/14.
 */
public class Message {
    private static final String TAG = "Message";


    /**
     * MessageContent : 消息内容
     * CreateTime : 2016-8-8 12:00:00
     */

    private String MessageContent;
    private String CreateTime;

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
}
