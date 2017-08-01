package cn.com.goodsowner.bean;

/**
 * Created by Administrator on 2016/12/8.
 * ---个人专属
 */

public class OrderFeeInfo {

    /**
     * FeeType : 保险费
     * Amount : 100.0
     * OrderNo : TO20161110000006
     */

    private String FeeType;
    private double Amount;
    private String OrderNo;

    public String getFeeType() {
        return FeeType;
    }

    public void setFeeType(String FeeType) {
        this.FeeType = FeeType;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }
}
