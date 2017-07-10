package com.guugoo.jiapeiteacher.bean;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/11.
 * --加油
 */
public class ForumCommentInfo {

    /**
     * ZambiaName : 绝世
     */

    private ArrayList<ZambiaInfo> Zambia;
    /**
     * TheReviewers : 绝世
     * Replyobject : 13655547856
     * Content : 111
     * TheReviewersId : S7
     * ReplyobjectId : S4
     */

    private ArrayList<CommentInfo> Comment;

    public ArrayList<ZambiaInfo> getZambia() {
        return Zambia;
    }

    public void setZambia(ArrayList<ZambiaInfo> Zambia) {
        this.Zambia = Zambia;
    }

    public ArrayList<CommentInfo> getComment() {
        return Comment;
    }

    public void setComment(ArrayList<CommentInfo> Comment) {
        this.Comment = Comment;
    }

    public static class ZambiaInfo {
        private String ZambiaName;

        public String getZambiaName() {
            return ZambiaName;
        }

        public void setZambiaName(String ZambiaName) {
            this.ZambiaName = ZambiaName;
        }

        @Override
        public String toString() {
            return ZambiaName + '\''+",";
        }
    }

    public static class CommentInfo {
        private String TheReviewers;
        private String Replyobject;
        private String Content;
        private String TheReviewersId;
        private String ReplyobjectId;

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

        public String getTheReviewersId() {
            return TheReviewersId;
        }

        public void setTheReviewersId(String TheReviewersId) {
            this.TheReviewersId = TheReviewersId;
        }

        public String getReplyobjectId() {
            return ReplyobjectId;
        }

        public void setReplyobjectId(String ReplyobjectId) {
            this.ReplyobjectId = ReplyobjectId;
        }
    }
}
