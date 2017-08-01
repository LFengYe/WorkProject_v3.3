package cn.com.goodsowner.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 * ---个人专属
 */

public class OrderDetailInfo {

    /**
     * OrderNo : TO20161122000014
     * OrderStatus : 2
     * FinanceStatus : 1
     * LogisticStatus : 1
     * Weight : 0.0
     * Volume : 0.0
     * Freight : 344.71
     * Premium : 20.0
     * SureNo :
     * TransporterId : 9f3e366b-18c3-47cb-b99c-f79549695191
     * TransporterName : 绝世
     * TransporterTel : 13781936563
     * VehicleTypeName :
     * VehicleNo : 5555555
     * GrabTime : 2016/11/22 18:01:04
     * Remove : True
     * Move : True
     * Surcharge : True
     * IsToPay : True
     * ToPayTel : 15271417050
     * IsCollectionPayment : False
     * Payment : 0.0
     * SendScore : 0
     * SendComment :
     * TransporterScore : 0
     * TransporterComment :
     * Remark :
     * OrderAddress : [{"AIndex":0,"Address":"pepperclub  (广东省深圳市福田区民田路138号)","Receipter":"fff","Tel":"88888","Lat":114.060752,"Lng":22.541294,"ArriveTime":"","DischargeTime":"","Amount":20,"PayWay":0,"PayTime":"","PayStatus":0},{"AIndex":0,"Address":"pepperclub  (广东省深圳市福田区民田路138号)","Receipter":"fff","Tel":"88888","Lat":114.060752,"Lng":22.541294,"ArriveTime":"","DischargeTime":"","Amount":344.71,"PayWay":0,"PayTime":"","PayStatus":0},{"AIndex":1,"Address":"深圳大学  (广东省深圳市南山区深大北路)","Receipter":"fff","Tel":"4555","Lat":113.943047,"Lng":22.540685,"ArriveTime":"","DischargeTime":"","Amount":0,"PayWay":0,"PayTime":"","PayStatus":0}]
     */

    private String OrderNo;
    private int OrderStatus;
    private int FinanceStatus;
    private int LogisticStatus;
    private double Weight;
    private double Volume;
    private double Freight;
    private double Premium;
    private String SureNo;
    private String TransporterId;
    private String TransporterName;
    private String TransporterTel;
    private String VehicleTypeName;
    private String VehicleNo;
    private String GrabTime;
    private String Remove;
    private String Move;
    private String Surcharge;
    private String IsToPay;
    private String ToPayTel;
    private String IsCollectionPayment;
    private double Payment;
    private int SendScore;
    private String SendComment;
    private int TransporterScore;
    private String TransporterComment;
    private String Remark;
    /**
     * AIndex : 0
     * Address : pepperclub  (广东省深圳市福田区民田路138号)
     * Receipter : fff
     * Tel : 88888
     * Lat : 114.060752
     * Lng : 22.541294
     * ArriveTime :
     * DischargeTime :
     * Amount : 20.0
     * PayWay : 0
     * PayTime :
     * PayStatus : 0
     */

    private List<OrderAddressBean> OrderAddress;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public int getFinanceStatus() {
        return FinanceStatus;
    }

    public void setFinanceStatus(int FinanceStatus) {
        this.FinanceStatus = FinanceStatus;
    }

    public int getLogisticStatus() {
        return LogisticStatus;
    }

    public void setLogisticStatus(int LogisticStatus) {
        this.LogisticStatus = LogisticStatus;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double Weight) {
        this.Weight = Weight;
    }

    public double getVolume() {
        return Volume;
    }

    public void setVolume(double Volume) {
        this.Volume = Volume;
    }

    public double getFreight() {
        return Freight;
    }

    public void setFreight(double Freight) {
        this.Freight = Freight;
    }

    public double getPremium() {
        return Premium;
    }

    public void setPremium(double Premium) {
        this.Premium = Premium;
    }

    public String getSureNo() {
        return SureNo;
    }

    public void setSureNo(String SureNo) {
        this.SureNo = SureNo;
    }

    public String getTransporterId() {
        return TransporterId;
    }

    public void setTransporterId(String TransporterId) {
        this.TransporterId = TransporterId;
    }

    public String getTransporterName() {
        return TransporterName;
    }

    public void setTransporterName(String TransporterName) {
        this.TransporterName = TransporterName;
    }

    public String getTransporterTel() {
        return TransporterTel;
    }

    public void setTransporterTel(String TransporterTel) {
        this.TransporterTel = TransporterTel;
    }

    public String getVehicleTypeName() {
        return VehicleTypeName;
    }

    public void setVehicleTypeName(String VehicleTypeName) {
        this.VehicleTypeName = VehicleTypeName;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }

    public String getGrabTime() {
        return GrabTime;
    }

    public void setGrabTime(String GrabTime) {
        this.GrabTime = GrabTime;
    }

    public String getRemove() {
        return Remove;
    }

    public void setRemove(String Remove) {
        this.Remove = Remove;
    }

    public String getMove() {
        return Move;
    }

    public void setMove(String Move) {
        this.Move = Move;
    }

    public String getSurcharge() {
        return Surcharge;
    }

    public void setSurcharge(String Surcharge) {
        this.Surcharge = Surcharge;
    }

    public String getIsToPay() {
        return IsToPay;
    }

    public void setIsToPay(String IsToPay) {
        this.IsToPay = IsToPay;
    }

    public String getToPayTel() {
        return ToPayTel;
    }

    public void setToPayTel(String ToPayTel) {
        this.ToPayTel = ToPayTel;
    }

    public String getIsCollectionPayment() {
        return IsCollectionPayment;
    }

    public void setIsCollectionPayment(String IsCollectionPayment) {
        this.IsCollectionPayment = IsCollectionPayment;
    }

    public double getPayment() {
        return Payment;
    }

    public void setPayment(double Payment) {
        this.Payment = Payment;
    }

    public int getSendScore() {
        return SendScore;
    }

    public void setSendScore(int SendScore) {
        this.SendScore = SendScore;
    }

    public String getSendComment() {
        return SendComment;
    }

    public void setSendComment(String SendComment) {
        this.SendComment = SendComment;
    }

    public int getTransporterScore() {
        return TransporterScore;
    }

    public void setTransporterScore(int TransporterScore) {
        this.TransporterScore = TransporterScore;
    }

    public String getTransporterComment() {
        return TransporterComment;
    }

    public void setTransporterComment(String TransporterComment) {
        this.TransporterComment = TransporterComment;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public List<OrderAddressBean> getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(List<OrderAddressBean> OrderAddress) {
        this.OrderAddress = OrderAddress;
    }

    public static class OrderAddressBean {
        private int AIndex;
        private String Address;
        private String Receipter;
        private String Tel;
        private double Lat;
        private double Lng;
        private String ArriveTime;
        private String DischargeTime;
        private double Amount;
        private int PayWay;
        private String PayTime;
        private int PayStatus;

        public int getAIndex() {
            return AIndex;
        }

        public void setAIndex(int AIndex) {
            this.AIndex = AIndex;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getReceipter() {
            return Receipter;
        }

        public void setReceipter(String Receipter) {
            this.Receipter = Receipter;
        }

        public String getTel() {
            return Tel;
        }

        public void setTel(String Tel) {
            this.Tel = Tel;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double Lat) {
            this.Lat = Lat;
        }

        public double getLng() {
            return Lng;
        }

        public void setLng(double Lng) {
            this.Lng = Lng;
        }

        public String getArriveTime() {
            return ArriveTime;
        }

        public void setArriveTime(String ArriveTime) {
            this.ArriveTime = ArriveTime;
        }

        public String getDischargeTime() {
            return DischargeTime;
        }

        public void setDischargeTime(String DischargeTime) {
            this.DischargeTime = DischargeTime;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public int getPayWay() {
            return PayWay;
        }

        public void setPayWay(int PayWay) {
            this.PayWay = PayWay;
        }

        public String getPayTime() {
            return PayTime;
        }

        public void setPayTime(String PayTime) {
            this.PayTime = PayTime;
        }

        public int getPayStatus() {
            return PayStatus;
        }

        public void setPayStatus(int PayStatus) {
            this.PayStatus = PayStatus;
        }
    }
}
