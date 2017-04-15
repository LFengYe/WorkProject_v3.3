package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/12.
 */
public class HuoOrder {


    private String CreateTimeY;
    private String CreateTimeX;
    private String CargoId;
    private String ContainerType;
    private String Amount;
    private int amountValue;
    private int remainValue;
    private String BussinessType;
    private String Status;
    private int statusValue;
    private String Price;
    private String StartAddress;
    private String Destination;
    private int ChargeType;
    private int isFinish;

    public HuoOrder( String CreateTimeY, String CreateTimeX, String  CargoId, String ContainerType,
                     String Amount,  String BussinessType, String Status, int statusValue,
                     String Price, String StartAddress, String Destination,int ChargeType,
                     int isFinish, int remainValue, int amountValue){

        this.CreateTimeY =CreateTimeY;
        this.CreateTimeX =CreateTimeX;
        this.CargoId =CargoId;
        this.ContainerType = ContainerType;
        this.Amount =Amount;

        this.BussinessType =BussinessType;
        this.statusValue = statusValue;
        this.Status =Status;
        this.Price =Price ;
        this.StartAddress =StartAddress ;
        this.Destination =Destination;
        this.ChargeType = ChargeType;
        this.isFinish = isFinish;
        this.remainValue = remainValue;
        this.amountValue = amountValue;
    }

    public int getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(int amountValue) {
        this.amountValue = amountValue;
    }

    public int getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(int remainValue) {
        this.remainValue = remainValue;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(int statusValue) {
        this.statusValue = statusValue;
    }

    public int getChargeType() {
        return ChargeType;
    }

    public void setChargeType(int chargeType) {
        ChargeType = chargeType;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setBussinessType(String bussinessType) {
        BussinessType = bussinessType;
    }

    public void setCargoId(String cargoId) {
        CargoId = cargoId;
    }

    public void setContainerType(String containerType) {
        ContainerType = containerType;
    }

    public void setCreateTimeX(String createTimeX) {
        CreateTimeX = createTimeX;
    }

    public void setCreateTimeY(String createTimeY) {
        CreateTimeY = createTimeY;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public void setPrice(String price) {
        Price = price;
    }



    public void setStartAddress(String startAddress) {
        StartAddress = startAddress;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCreateTimeY() {
        return CreateTimeY;
    }

    public String getAmount() {
        return Amount;
    }

    public String getBussinessType() {
        return BussinessType;
    }

    public String getCargoId() {
        return CargoId;
    }

    public String getContainerType() {
        return ContainerType;
    }

    public String getCreateTimeX() {
        return CreateTimeX;
    }

    public String getDestination() {
        return Destination;
    }

    public String getPrice() {
        return Price;
    }

    public String getStartAddress() {
        return StartAddress;
    }

    public String getStatus() {
        return Status;
    }




}
