package com.gpw.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 * ---个人专属
 */

public class BalanceInfo {

    /**
     * SumIncome : 140.0
     * SumConsum : 2000.0
     * List : [{"Time":"2016/10/10 0:00:00","Way":"微信支付","Amount":100,"Type":"收支类型","SerialNO":"123","Remark":"充值"}]
     */

    private double SumIncome;
    private double SumConsum;
    /**
     * Time : 2016/10/10 0:00:00
     * Way : 微信支付
     * Amount : 100.0
     * Type : 收支类型
     * SerialNO : 123
     * Remark : 充值
     */

    private java.util.List<ListBean> List;

    public double getSumIncome() {
        return SumIncome;
    }

    public void setSumIncome(double SumIncome) {
        this.SumIncome = SumIncome;
    }

    public double getSumConsum() {
        return SumConsum;
    }

    public void setSumConsum(double SumConsum) {
        this.SumConsum = SumConsum;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        private String Time;
        private String Way;
        private double Amount;
        private int Type;
        private String SerialNO;
        private String Remark;

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getWay() {
            return Way;
        }

        public void setWay(String Way) {
            this.Way = Way;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getSerialNO() {
            return SerialNO;
        }

        public void setSerialNO(String SerialNO) {
            this.SerialNO = SerialNO;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
    }
}
