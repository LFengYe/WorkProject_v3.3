package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ReserveDetails {
    private static final String TAG = "ReserveDetails";

    /**
     * BookingTime : 预订时间
     *  Branch : 场地名称
     *  TeacherNmae : 教练名称
     *  TeacherTel : 教练电话
     * VehNof : 车牌号
     *  Subject : 科目
     *  Amount : 100
     *  Comment : 教练评语
     *  status : 状态
     *  TimeSlot :   预约练车时间
     * PayTime : 支付的时间
     * PayType : 支付方式
     */

    private String BookingTime;
    private String Branch;
    private String TeacherNmae;
    private String TeacherTel;
    private String VehNof;
    private int Subject;
    private float Amount;
    private String Comment;
    private int status;
    private String TimeSlot;
    private String PayTime;
    private int PayType;
    private String SubjectItem;
    private String Lon;
    private String Lat;

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String BookingTime) {
        this.BookingTime = BookingTime;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getTeacherNmae() {
        return TeacherNmae;
    }

    public void setTeacherNmae(String teacherNmae) {
        TeacherNmae = teacherNmae;
    }

    public String getVehNof() {
        return VehNof;
    }

    public void setVehNof(String vehNof) {
        VehNof = vehNof;
    }

    public String getTeacherTel() {
        return TeacherTel;
    }

    public void setTeacherTel(String teacherTel) {
        TeacherTel = teacherTel;
    }

    public int getSubject() {
        return Subject;
    }

    public void setSubject(int subject) {
        Subject = subject;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPayTime() {
        return PayTime;
    }

    public void setPayTime(String payTime) {
        PayTime = payTime;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public int getPayType() {
        return PayType;
    }

    public void setPayType(int payType) {
        PayType = payType;
    }

    public String getSubjectItem() {
        return SubjectItem;
    }

    public void setSubjectItem(String subjectItem) {
        SubjectItem = subjectItem;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }
}
