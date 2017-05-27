package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/14.
 */
public class TimeData {
    private static final String TAG = "TimeData";

    private int Type;
    private int X;
    private int Y;
    private float price;
    private String year;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
