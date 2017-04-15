package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/4/28.
 */
public class People {
    private String UserId;
    private String Companyname;
    private String Principal;
    private String Telephoen;
    private String LoginName;
    private String Integral;
    private String Address;

    public String getCompanyname() {
        return Companyname;

    }

    public String getAddress() {
        return Address;
    }

    public String getIntegral() {
        return Integral;
    }

    public String getLoginName() {
        return LoginName;
    }

    public String getPrincipal() {
        return Principal;
    }

    public String getTelephoen() {
        return Telephoen;
    }

    public String getUserId() {
        return UserId;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCompanyname(String companyname) {
        Companyname = companyname;
    }

    public void setIntegral(String integral) {
        Integral = integral;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public void setPrincipal(String principal) {
        Principal = principal;
    }

    public void setTelephoen(String telephoen) {
        Telephoen = telephoen;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
