package com.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/4.
 * --加油
 */
public class ImageData {
    private String bookingId;
    private int state;
    private boolean press;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public boolean isPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
