package cn.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/13.
 * --加油
 */
public class WorkBeachInfo {



    private String CrossTitle;
    private String ColumnTitle;
    private String BookingList;

    public String getColumnTitle() {
        return ColumnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        ColumnTitle = columnTitle;
    }

    public String getCrossTitle() {
        return CrossTitle;
    }

    public void setCrossTitle(String crossTitle) {
        CrossTitle = crossTitle;
    }

    public String getBookingList() {
        return BookingList;
    }

    public void setBookingList(String bookingList) {
        BookingList = bookingList;
    }


    @Override
    public String toString() {
        return "WorkBeachInfo{" +
                "CrossTitle='" + CrossTitle + '\'' +
                ", ColumnTitle='" + ColumnTitle + '\'' +
                ", BookingList='" + BookingList + '\'' +
                '}';
    }
}
