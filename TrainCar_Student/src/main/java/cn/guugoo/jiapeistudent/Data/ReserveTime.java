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
    private String ColumnTitle;
    private String CrossTitle;
    private String BookingList;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getColumnTitle() {
        return ColumnTitle;
    }

    public void setColumnTitle(String ColumnTitle) {
        this.ColumnTitle = ColumnTitle;
    }

    public String getCrossTitle() {
        return CrossTitle;
    }

    public void setCrossTitle(String CrossTitle) {
        this.CrossTitle = CrossTitle;
    }

    public String getBookingList() {
        return BookingList;
    }

    public void setBookingList(String bookingList) {
        BookingList = bookingList;
    }
}
