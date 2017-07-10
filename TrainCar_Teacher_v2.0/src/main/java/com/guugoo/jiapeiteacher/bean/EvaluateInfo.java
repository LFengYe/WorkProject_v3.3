package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/11.
 * --加油
 */
public class EvaluateInfo {


    /**
     * StudentName : 姓名
     * StudentHeadImg : http://101.201.74.192:8001/HeadPortraitImg/145415412.jpg
     * Attitude : 3
     * Technology : 3
     * Appearance : 3
     * CarCondition : 3
     * Comment : 评论内容
     * Count : 1
     * CreateTime : 2016/7/28 0:00:00
     */

    private String StudentName;
    private String StudentHeadImg;
    private int Attitude;
    private int Technology;
    private int Appearance;
    private int CarCondition;
    private String Comment;
    private int Count;
    private String CreateTime;

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String StudentName) {
        this.StudentName = StudentName;
    }

    public String getStudentHeadImg() {
        return StudentHeadImg;
    }

    public void setStudentHeadImg(String StudentHeadImg) {
        this.StudentHeadImg = StudentHeadImg;
    }

    public int getAttitude() {
        return Attitude;
    }

    public void setAttitude(int Attitude) {
        this.Attitude = Attitude;
    }

    public int getTechnology() {
        return Technology;
    }

    public void setTechnology(int Technology) {
        this.Technology = Technology;
    }

    public int getAppearance() {
        return Appearance;
    }

    public void setAppearance(int Appearance) {
        this.Appearance = Appearance;
    }

    public int getCarCondition() {
        return CarCondition;
    }

    public void setCarCondition(int CarCondition) {
        this.CarCondition = CarCondition;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
