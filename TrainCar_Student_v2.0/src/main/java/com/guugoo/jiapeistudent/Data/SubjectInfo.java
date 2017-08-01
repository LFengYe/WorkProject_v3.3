package com.guugoo.jiapeistudent.Data;

/**
 * Created by LFeng on 2017/8/1.
 */

public class SubjectInfo {
    private int SubjectId;
    private String SubjectName;

    public int getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(int subjectId) {
        SubjectId = subjectId;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    @Override
    public String toString() {
        return this.SubjectName;
    }
}
