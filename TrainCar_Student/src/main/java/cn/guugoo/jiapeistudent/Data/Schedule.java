package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/13.
 */
public class Schedule {
    private static final String TAG = "Schedule";

    /**
     * ScheduleName : 进度名
     *  CreateTime : 2016-8-6 12:00:00
     *  ScheduleNo : 1
     *  IsComplete : true
     */

    private String ScheduleName;
    private String CreateTime;
    private int ScheduleNo;
    private boolean IsComplete;

    public String getScheduleName() {
        return ScheduleName;
    }

    public void setScheduleName(String ScheduleName) {
        this.ScheduleName = ScheduleName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }



    public boolean isComplete() {
        return IsComplete;
    }

    public void setIsComplete(boolean complete) {
        IsComplete = complete;
    }

    public int getScheduleNo() {
        return ScheduleNo;
    }

    public void setScheduleNo(int scheduleNo) {
        ScheduleNo = scheduleNo;
    }
}
