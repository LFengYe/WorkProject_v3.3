package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ForumComment {
    private static final String TAG = "ForumComment";

    /**
     * TheReviewers : 评论者
     * Replyobject : 回复对象
     * Content : 内容
     *  TheReviewersId : s1
     *  ReplyobjectId : t2
     */

    private String TheReviewers;
    private String Replyobject;
    private String Content;
    private String TheReviewersId;
    private String ReplyobjectId;

    public ForumComment() {

    }

    public ForumComment(String theReviewers, String replyobject, String content, String theReviewersId, String replyobjectId) {
        TheReviewers = theReviewers;
        Replyobject = replyobject;
        Content = content;
        TheReviewersId = theReviewersId;
        ReplyobjectId = replyobjectId;
    }

    public ForumComment(String theReviewers,String content ) {
        Content = content;
        TheReviewers = theReviewers;
    }

    public String getReplyobjectId() {
        return ReplyobjectId;
    }

    public void setReplyobjectId(String replyobjectId) {
        ReplyobjectId = replyobjectId;
    }

    public String getTheReviewersId() {
        return TheReviewersId;
    }

    public void setTheReviewersId(String theReviewersId) {
        TheReviewersId = theReviewersId;
    }

    public String getTheReviewers() {
        return TheReviewers;
    }

    public void setTheReviewers(String TheReviewers) {
        this.TheReviewers = TheReviewers;
    }

    public String getReplyobject() {
        return Replyobject;
    }

    public void setReplyobject(String Replyobject) {
        this.Replyobject = Replyobject;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
}
