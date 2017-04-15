package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Touzi {
    private String ManageMoneyName;
    private String ManageMoneyIntro;
    private int Id;
    public Touzi( String ManageMoneyName,String ManageMoneyIntro,int Id){
        this.ManageMoneyIntro =ManageMoneyIntro;
        this.ManageMoneyName = ManageMoneyName;
        this.Id = Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setManageMoneyIntro(String manageMoneyIntro) {
        ManageMoneyIntro = manageMoneyIntro;
    }

    public void setManageMoneyName(String manageMoneyName) {
        ManageMoneyName = manageMoneyName;
    }

    public String getManageMoneyIntro() {
        return ManageMoneyIntro;
    }

    public String getManageMoneyName() {

        return ManageMoneyName;

    }
}
