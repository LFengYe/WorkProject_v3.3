package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ReserveTime {
    private static final String TAG = "ReserveTime";


    /**
     * Type : SJ
     * ColumnTitle :
     * CrossTitle :
     * data :
     */

    private String Type;
    private String TimeCoordinate;
    private String BookingList;


    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getBookingList() {
        return BookingList;
    }

    public void setBookingList(String bookingList) {
        BookingList = bookingList;
    }

    public String getTimeCoordinate() {
        return TimeCoordinate;
    }

    public void setTimeCoordinate(String timeCoordinate) {
        TimeCoordinate = timeCoordinate;
    }
}
