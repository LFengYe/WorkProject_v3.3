package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/13.
 * --加油
 */
public class WorkBeachInfo {



    private String TimeCoordinate;
    private String BookingList;

    public String getTimeCoordinate() {
        return TimeCoordinate;
    }

    public void setTimeCoordinate(String timeCoordinate) {
        TimeCoordinate = timeCoordinate;
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
                "TimeCoordinate='" + TimeCoordinate + '\'' +
                ", BookingList='" + BookingList + '\'' +
                '}';
    }
}
