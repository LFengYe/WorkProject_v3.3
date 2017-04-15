package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/9.
 */
public class Reserve {
    private static final String TAG = "Reserve";

    /**
     *  BookingsId : 预约编号
     *  BookingsTime : 预约练车时间
     *  Amount : 本次练车所需金额
     *  Teacher : 教练名称
     *  Status : 当前状态
     *  Branch : 训练场地
     *  FeeItem :1:预约练车费用，2:取消预约单违约金
     *  RefId : 取消预约数据ID
     */

    private String BookingsId;
    private String BookingsTime;

    private float Amount;
    private String Teacher;
    private int Status;
    private String Branch;
    private int ActMinute;
    private float MinuteFee;
    private int Subject;
    private int FeeItem;
    private int TeacherId;
    private String BookingDay;
    private String TimeSlot;
    private String RefId;


    public String getBookingsId() {
        return BookingsId;
    }

    public void setBookingsId(String bookingsId) {
        BookingsId = bookingsId;
    }

    public String getBookingsTime() {
        return BookingsTime;
    }

    public void setBookingsTime(String BookingsTime) {
        this.BookingsTime = BookingsTime;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public int getActMinute() {
        return ActMinute;
    }

    public void setActMinute(int actMinute) {
        ActMinute = actMinute;
    }

    public float getMinuteFee() {
        return MinuteFee;
    }

    public void setMinuteFee(float minuteFee) {
        MinuteFee = minuteFee;
    }

    public int getSubject() {
        return Subject;
    }

    public void setSubject(int subject) {
        Subject = subject;
    }

    public int getFeeItem() {
        return FeeItem;
    }

    public void setFeeItem(int feeItem) {
        FeeItem = feeItem;
    }

    public int getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(int teacherId) {
        TeacherId = teacherId;
    }

    public String getBookingDay() {
        return BookingDay;
    }

    public void setBookingDay(String bookingDay) {
        BookingDay = bookingDay;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getRefId() {
        return RefId;
    }

    public void setRefId(String refId) {
        RefId = refId;
    }
}
