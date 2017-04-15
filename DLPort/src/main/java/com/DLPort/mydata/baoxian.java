package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/17.
 */
public class baoxian {

    private String Id;
    private String InsuranceName;
    private String InsuranceType;
    private String Discount;
    private String Description;
    private String Money;
    private String CreateTime;
    public baoxian(   String Id, String InsuranceName, String InsuranceType, String Discount,
         String Description, String Money, String CreateTime){
        this.Id=Id;
        this.InsuranceName = InsuranceName;
        this.InsuranceType = InsuranceType;
        this.Discount =Discount;
        this.CreateTime = CreateTime ;
        this.Money = Money;
        this.Description =Description;

    }


    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setInsuranceName(String insuranceName) {
        InsuranceName = insuranceName;
    }

    public void setInsuranceType(String insuranceType) {
        InsuranceType = insuranceType;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public String getDescription() {
        return Description;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getId() {
        return Id;
    }

    public String getInsuranceName() {
        return InsuranceName;
    }

    public String getInsuranceType() {
        return InsuranceType;
    }

    public String getMoney() {
        return Money;
    }
}
