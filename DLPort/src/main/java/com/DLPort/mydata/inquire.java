package com.DLPort.mydata;

import java.util.IdentityHashMap;

/**
 * Created by Administrator on 2016/5/14.
 */
public class inquire {
    private String Id;
    private String ShipCompany;
    private String ShipLine;
    private String ShipName;
    private String ShipOrder;
    private String DestinationPort;
    private String InPortTime;
    public inquire( String Id,String ShipCompany, String ShipLine, String ShipName, String ShipOrder,
                     String DestinationPort, String InPortTime){
        this.Id =Id;
        this.DestinationPort=DestinationPort;
        this.ShipCompany =ShipCompany;
        this.ShipLine = ShipLine ;
        this.ShipName = ShipName ;
        this.ShipOrder = ShipOrder ;
        this.InPortTime = InPortTime ;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setDestinationPort(String destinationPort) {
        DestinationPort = destinationPort;
    }

    public void setInPortTime(String inPortTime) {
        InPortTime = inPortTime;
    }

    public void setShipCompany(String shipCompany) {
        ShipCompany = shipCompany;
    }

    public void setShipLine(String shipLine) {
        ShipLine = shipLine;
    }

    public void setShipName(String shipName) {
        ShipName = shipName;
    }

    public void setShipOrder(String shipOrder) {
        ShipOrder = shipOrder;
    }

    public String getDestinationPort() {
        return DestinationPort;
    }

    public String getInPortTime() {
        return InPortTime;
    }

    public String getShipCompany() {
        return ShipCompany;
    }

    public String getShipLine() {
        return ShipLine;
    }

    public String getShipName() {
        return ShipName;
    }

    public String getShipOrder() {
        return ShipOrder;
    }
}
