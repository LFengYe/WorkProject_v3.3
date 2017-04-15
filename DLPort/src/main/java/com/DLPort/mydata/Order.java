package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/4/18.
 */
public class Order {
    public static final int TYPE_OLD =0;
    public static final int TYPE_NEW =1;
    private String LoadTimeY;
    private String LoadTimeX;
    private String ContainerType;
    private String Remain;
    private String BussinessType;
    private String Price;
    private String CargoId;
    private String Destination;
    private String StartAddress;
    private String ShipCompany;
    private String InPortTime;
    private int ChargeType;
    private int IsFinish;
    private int status;

    public Order( String LoadTimeY,String LoadTimeX, String ContainerType, String Remain, String BussinessType,
                  String Price,  String Destination, String StartAddress,String CargoId,int IsFinish,
                  String ShipCompany, String InPortTime, int ChargeType, int status){
        this.LoadTimeY=LoadTimeY;
        this.LoadTimeX =LoadTimeX;
        this.ContainerType =ContainerType;
        this.Remain = Remain;
        this.BussinessType =BussinessType;
        this.Price =Price;

        this.Destination =Destination;
        this.StartAddress =StartAddress;
        this.CargoId = CargoId;
        this.IsFinish = IsFinish;
        this.ShipCompany = ShipCompany ;
        this.InPortTime = InPortTime ;
        this.ChargeType = ChargeType ;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setChargeType(int chargeType) {
        ChargeType = chargeType;
    }

    public void setInPortTime(String inPortTime) {
        InPortTime = inPortTime;
    }

    public void setShipCompany(String shipCompany) {
        ShipCompany = shipCompany;
    }

    public int getChargeType() {
        return ChargeType;
    }

    public String getInPortTime() {
        return InPortTime;
    }

    public String getShipCompany() {
        return ShipCompany;
    }

    public void setIsFinish(int isFinish) {
        IsFinish = isFinish;
    }

    public int getIsFinish() {

        return IsFinish;
    }


    public String getLoadTimeX() {
        return LoadTimeX;
    }

    public String getLoadTimeY() {
        return LoadTimeY;
    }

    public void setLoadTimeX(String loadTimeX) {
        LoadTimeX = loadTimeX;
    }

    public void setLoadTimeY(String loadTimeY) {
        LoadTimeY = loadTimeY;
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

    public void setDestination(String destination) {
        Destination = destination;
    }



    public void setPrice(String price) {
        Price = price;
    }

    public void setRemain(String remain) {
        Remain = remain;
    }

    public void setStartAddress(String startAddress) {
        StartAddress = startAddress;
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

    public String getDestination() {
        return Destination;
    }



    public String getPrice() {
        return Price;
    }

    public String getRemain() {
        return Remain;
    }

    public String getStartAddress() {
        return StartAddress;
    }
}
