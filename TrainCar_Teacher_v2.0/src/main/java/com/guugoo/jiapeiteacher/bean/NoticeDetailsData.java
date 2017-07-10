package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class NoticeDetailsData {

    /**
     * NoticeId : 1
     * Title : 公告标题
     * Content : 公告内容
     */

    private int NoticeId;
    private String Title;
    private String Content;

    public int getNoticeId() {
        return NoticeId;
    }

    public void setNoticeId(int NoticeId) {
        this.NoticeId = NoticeId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    @Override
    public String toString() {
        return "NoticeDetailsData{" +
                "NoticeId=" + NoticeId +
                ", Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }
}
