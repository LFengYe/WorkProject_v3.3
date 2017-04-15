package cn.com.caronwer.bean;

import java.util.List;

/**
 * Created by  on .
 * --------------------------
 * 版   权 ：
 * <p>
 * 作   者 ：X230
 * 文件名 ：
 * <p>
 * 创建于：2016/11/26 18:09
 * 概  述:
 */

public class MeAllOrderInfo {


    /**
     * OrderNo : TO20161213000003
     * PlanSendTime : 2016/12/13 14:50:00
     * Freight : 135.79
     * IsRemove : False
     * IsMove : False
     * IsSurcharge : False
     * IsToPay : False
     * ToPayTel :
     * IsCollectionPayment : True
     * Payment : 200.0
     * VehicleType : 1
     * VehicleTypeName : 小面包车
     * CreateTime : 2016/12/13 14:50:58
     * Remark : 测试留言
     * OrderType : 1
     * BusinessType : 2
     * OrderStatus : 2
     * FinanceStatus : 2
     * LogisticStatus : 2
     * SenderId : 86a82419-3fb1-469a-9995-ba310b00fc6b
     * SenderName : 刘1
     * SenderTel : 18511111111
     * SendScore : 0
     * SendComment :
     * TransporterScore : 0
     * TransporterComment :
     * CancelFee : 0.0
     * IsFamiliar : true
     * OrderAddress : [{"AIndex":0,"Receipter":"刘1","ReceiptTel":"18511111111","ReceiptAddress":"天虹商场(西丽店)   (广东省深圳市南山区留仙大道1380号)","PaymentGoods":0,"Lat":22.587084,"Lng":113.961777,"ArriveTime":"","DischargeTime":"2016/12/13 14:53:45"},{"AIndex":1,"Receipter":"刘2","ReceiptTel":"18522222222","ReceiptAddress":"黄金台商业大厦   (广东省深圳市宝安区前进一路309-7号)","PaymentGoods":200,"Lat":22.584781,"Lng":113.900458,"ArriveTime":"","DischargeTime":""},{"AIndex":2,"Receipter":"刘3","ReceiptTel":"18533333333","ReceiptAddress":"天虹商场(合正汇一城店)   (广东省深圳市宝安区新湖路)","PaymentGoods":0,"Lat":22.581703,"Lng":113.870275,"ArriveTime":"","DischargeTime":""}]
     */

    private String OrderNo;
    private String PlanSendTime;
    private double Freight;
    private String IsRemove;
    private String IsMove;
    private String IsSurcharge;
    private String IsToPay;
    private String ToPayTel;
    private boolean IsCollectionPayment;
    private double Payment;
    private int VehicleType;
    private String VehicleTypeName;
    private String CreateTime;
    private String Remark;
    private int OrderType;
    private int BusinessType;
    private int OrderStatus;
    private int FinanceStatus;
    private String LogisticStatus;
    private String SenderId;
    private String SenderName;
    private String SenderTel;
    private int SendScore;
    private String SendComment;
    private int TransporterScore;
    private String TransporterComment;
    private double CancelFee;
    private String IsFamiliar;
    private List<OrderAddressBean> OrderAddress;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getPlanSendTime() {
        return PlanSendTime;
    }

    public void setPlanSendTime(String PlanSendTime) {
        this.PlanSendTime = PlanSendTime;
    }

    public double getFreight() {
        return Freight;
    }

    public void setFreight(double Freight) {
        this.Freight = Freight;
    }

    public String getIsRemove() {
        return IsRemove;
    }

    public void setIsRemove(String IsRemove) {
        this.IsRemove = IsRemove;
    }

    public String getIsMove() {
        return IsMove;
    }

    public void setIsMove(String IsMove) {
        this.IsMove = IsMove;
    }

    public String getIsSurcharge() {
        return IsSurcharge;
    }

    public void setIsSurcharge(String IsSurcharge) {
        this.IsSurcharge = IsSurcharge;
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

    public boolean getIsCollectionPayment() {
        return IsCollectionPayment;
    }

    public void setIsCollectionPayment(boolean IsCollectionPayment) {
        this.IsCollectionPayment = IsCollectionPayment;
    }

    public double getPayment() {
        return Payment;
    }

    public void setPayment(double Payment) {
        this.Payment = Payment;
    }

    public int getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(int VehicleType) {
        this.VehicleType = VehicleType;
    }

    public String getVehicleTypeName() {
        return VehicleTypeName;
    }

    public void setVehicleTypeName(String VehicleTypeName) {
        this.VehicleTypeName = VehicleTypeName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int OrderType) {
        this.OrderType = OrderType;
    }

    public int getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(int BusinessType) {
        this.BusinessType = BusinessType;
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

    public String getLogisticStatus() {
        return LogisticStatus;
    }

    public void setLogisticStatus(String LogisticStatus) {
        this.LogisticStatus = LogisticStatus;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String SenderId) {
        this.SenderId = SenderId;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String SenderName) {
        this.SenderName = SenderName;
    }

    public String getSenderTel() {
        return SenderTel;
    }

    public void setSenderTel(String SenderTel) {
        this.SenderTel = SenderTel;
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

    public double getCancelFee() {
        return CancelFee;
    }

    public void setCancelFee(double CancelFee) {
        this.CancelFee = CancelFee;
    }

    public String getIsFamiliar() {
        return IsFamiliar;
    }

    public void setIsFamiliar(String IsFamiliar) {
        this.IsFamiliar = IsFamiliar;
    }

    public List<OrderAddressBean> getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(List<OrderAddressBean> OrderAddress) {
        this.OrderAddress = OrderAddress;
    }


    /*
    public static class OrderAddressBean {

        private int AIndex;
        private String Receipter;
        private String ReceiptTel;
        private String ReceiptAddress;
        private double PaymentGoods;
        private double Lat;
        private double Lng;
        private String ArriveTime;
        private String DischargeTime;

        public int getAIndex() {
            return AIndex;
        }

        public void setAIndex(int AIndex) {
            this.AIndex = AIndex;
        }

        public String getReceipter() {
            return Receipter;
        }

        public void setReceipter(String Receipter) {
            this.Receipter = Receipter;
        }

        public String getReceiptTel() {
            return ReceiptTel;
        }

        public void setReceiptTel(String ReceiptTel) {
            this.ReceiptTel = ReceiptTel;
        }

        public String getReceiptAddress() {
            return ReceiptAddress;
        }

        public void setReceiptAddress(String ReceiptAddress) {
            this.ReceiptAddress = ReceiptAddress;
        }

        public double getPaymentGoods() {
            return PaymentGoods;
        }

        public void setPaymentGoods(double PaymentGoods) {
            this.PaymentGoods = PaymentGoods;
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
    }
    */
}
