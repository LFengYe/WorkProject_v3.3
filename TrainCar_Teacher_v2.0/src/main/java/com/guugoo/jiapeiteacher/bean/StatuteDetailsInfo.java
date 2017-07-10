package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class StatuteDetailsInfo {


    /**
     * Id : 1
     * Title : 法规标题
     * NewContent : 法规内容
     */

    private int Id;
    private String Title;
    private String NewContent;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getNewContent() {
        return NewContent;
    }

    public void setNewContent(String NewContent) {
        this.NewContent = NewContent;
    }
}
