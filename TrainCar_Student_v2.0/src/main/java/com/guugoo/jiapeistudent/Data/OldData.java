package com.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/13.
 */
public class OldData {
    private static final String TAG = "OldData";

    /**
     * Name : 姓名
     * CardNo : 身份证号码
     * Tel : 联系电话
     * Sex : 男
     * Address : 地址
     */

    private String Name;
    private String CardNo;
    private String Tel;
    private String Sex;
    private String Address;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }
}
