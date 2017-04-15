package cn.com.caronwer.bean;

import java.util.List;

/**
 * --------------------------------------------------
 * 版       权 ：易成勇
 * <p>
 * 作       者： 易成勇
 * <p>
 * 文件名：QiangOrderInfo
 * <p>
 * 创 建 日 期 ： 2016/11/24 0024  6:40
 * <p>
 * 描      述 ：
 * <p>
 * <p>
 * 修 订 历 史:
 * <p>
 * --------------------------------------------------
 */

public class QiangOrderInfo {


    /**
     * ordersdetails : {"OrderNo":"TO20161124000016","SenderId":"4c459715-1870-45b1-bfcb-6357064b6af2","SenderTel":"13781936563","SendAddress":"盛地大厦  (南阳路101","Weight":0,"Volume":0,"Quantity":0,"Freight":5,"Premium":100,"SureNo":"","IsRemove":false,"IsMove":false,"IsSurcharge":false,"IsMyFleet":false,"IsToPay":false,"ToPayTel":"","IsCollectionPayment":false,"Payment":0,"OrderStatus":1,"FinanceStatus":2,"LogisticStatus":0,"TransporterId":"9f3e366b-18c3-47cb-b99c-f79549695191","TransporterTel":"13781936563","VehicleNo":"5555555","VehicleType":0,"VehicleTypeName":"","GrabTime":"0001-01-01T00:00:00","CreateTime":"2016-11-25T15:28:18","PlanSendTime":"2016-11-24T14:28:00","OrderType":1,"BusinessType":1,"SendScore":0,"SendComment":"","TransporterScore":0,"TransporterComment":"","IsInvoice":false,"StartLat":34.8,"StartLng":113.64,"StartToEndDistance":"2.684","VehToStartDistance":0.6234313,"Remark":""}
     * AddressList : [{"AIndex":0,"OrderNo":"TO20161124000016","Receipter":"","ReceiptTel":"","ReceiptAddress":"金水区东风路  (河南省郑州市)","PaymentGoods":0,"Lat":34.8,"Lng":113.64,"ArriveTime":"0001-01-01T00:00:00","DischargeTime":"0001-01-01T00:00:00"},{"AIndex":1,"OrderNo":"TO20161124000016","Receipter":"","ReceiptTel":"","ReceiptAddress":"河南省实验中学  (文化路街道文化路60号)","PaymentGoods":0,"Lat":34.8,"Lng":113.67,"ArriveTime":"0001-01-01T00:00:00","DischargeTime":"0001-01-01T00:00:00"},{"AIndex":2,"OrderNo":"TO20161124000016","Receipter":"","ReceiptTel":"","ReceiptAddress":"储运贸易公司家属院  (郑州市金水区)","PaymentGoods":105,"Lat":34.79,"Lng":113.64,"ArriveTime":"0001-01-01T00:00:00","DischargeTime":"0001-01-01T00:00:00"}]
     */

    private OrdersdetailsBean ordersdetails;
    private List<AddressListBean> AddressList;

    public OrdersdetailsBean getOrdersdetails() {
        return ordersdetails;
    }

    public void setOrdersdetails(OrdersdetailsBean ordersdetails) {
        this.ordersdetails = ordersdetails;
    }

    public List<AddressListBean> getAddressList() {
        return AddressList;
    }

    public void setAddressList(List<AddressListBean> AddressList) {
        this.AddressList = AddressList;
    }

    public static class OrdersdetailsBean {
        /**
         * OrderNo : TO20161124000016
         * SenderId : 4c459715-1870-45b1-bfcb-6357064b6af2
         * SenderTel : 13781936563
         * SendAddress : 盛地大厦  (南阳路101
         * Weight : 0.0
         * Volume : 0.0
         * Quantity : 0
         * Freight : 5.0
         * Premium : 100.0
         * SureNo :
         * IsRemove : false
         * IsMove : false
         * IsSurcharge : false
         * IsMyFleet : false
         * IsToPay : false
         * ToPayTel :
         * IsCollectionPayment : false
         * Payment : 0.0
         * OrderStatus : 1
         * FinanceStatus : 2
         * LogisticStatus : 0
         * TransporterId : 9f3e366b-18c3-47cb-b99c-f79549695191
         * TransporterTel : 13781936563
         * VehicleNo : 5555555
         * VehicleType : 0
         * VehicleTypeName :
         * GrabTime : 0001-01-01T00:00:00
         * CreateTime : 2016-11-25T15:28:18
         * PlanSendTime : 2016-11-24T14:28:00
         * OrderType : 1
         * BusinessType : 1
         * SendScore : 0
         * SendComment :
         * TransporterScore : 0
         * TransporterComment :
         * IsInvoice : false
         * StartLat : 34.8
         * StartLng : 113.64
         * StartToEndDistance : 2.684
         * VehToStartDistance : 0.6234313
         * Remark :
         */

        private String OrderNo;
        private String SenderId;
        private String SenderTel;
        private String SendAddress;
        private double Weight;
        private double Volume;
        private int Quantity;
        private double Freight;
        private double Premium;
        private String SureNo;
        private boolean IsRemove;
        private boolean IsMove;
        private boolean IsSurcharge;
        private boolean IsMyFleet;
        private boolean IsToPay;
        private String ToPayTel;
        private boolean IsCollectionPayment;
        private double Payment;
        private int OrderStatus;
        private int FinanceStatus;
        private int LogisticStatus;
        private String TransporterId;
        private String TransporterTel;
        private String VehicleNo;
        private int VehicleType;
        private String VehicleTypeName;
        private String GrabTime;
        private String CreateTime;
        private String PlanSendTime;
        private int OrderType;
        private int BusinessType;
        private int SendScore;
        private String SendComment;
        private int TransporterScore;
        private String TransporterComment;
        private boolean IsInvoice;
        private double StartLat;
        private double StartLng;
        private String StartToEndDistance;
        private double VehToStartDistance;
        private String Remark;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getSenderId() {
            return SenderId;
        }

        public void setSenderId(String SenderId) {
            this.SenderId = SenderId;
        }

        public String getSenderTel() {
            return SenderTel;
        }

        public void setSenderTel(String SenderTel) {
            this.SenderTel = SenderTel;
        }

        public String getSendAddress() {
            return SendAddress;
        }

        public void setSendAddress(String SendAddress) {
            this.SendAddress = SendAddress;
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

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int Quantity) {
            this.Quantity = Quantity;
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

        public boolean isIsRemove() {
            return IsRemove;
        }

        public void setIsRemove(boolean IsRemove) {
            this.IsRemove = IsRemove;
        }

        public boolean isIsMove() {
            return IsMove;
        }

        public void setIsMove(boolean IsMove) {
            this.IsMove = IsMove;
        }

        public boolean isIsSurcharge() {
            return IsSurcharge;
        }

        public void setIsSurcharge(boolean IsSurcharge) {
            this.IsSurcharge = IsSurcharge;
        }

        public boolean isIsMyFleet() {
            return IsMyFleet;
        }

        public void setIsMyFleet(boolean IsMyFleet) {
            this.IsMyFleet = IsMyFleet;
        }

        public boolean isIsToPay() {
            return IsToPay;
        }

        public void setIsToPay(boolean IsToPay) {
            this.IsToPay = IsToPay;
        }

        public String getToPayTel() {
            return ToPayTel;
        }

        public void setToPayTel(String ToPayTel) {
            this.ToPayTel = ToPayTel;
        }

        public boolean isIsCollectionPayment() {
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

        public String getTransporterId() {
            return TransporterId;
        }

        public void setTransporterId(String TransporterId) {
            this.TransporterId = TransporterId;
        }

        public String getTransporterTel() {
            return TransporterTel;
        }

        public void setTransporterTel(String TransporterTel) {
            this.TransporterTel = TransporterTel;
        }

        public String getVehicleNo() {
            return VehicleNo;
        }

        public void setVehicleNo(String VehicleNo) {
            this.VehicleNo = VehicleNo;
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

        public String getGrabTime() {
            return GrabTime;
        }

        public void setGrabTime(String GrabTime) {
            this.GrabTime = GrabTime;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getPlanSendTime() {
            return PlanSendTime;
        }

        public void setPlanSendTime(String PlanSendTime) {
            this.PlanSendTime = PlanSendTime;
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

        public boolean isIsInvoice() {
            return IsInvoice;
        }

        public void setIsInvoice(boolean IsInvoice) {
            this.IsInvoice = IsInvoice;
        }

        public double getStartLat() {
            return StartLat;
        }

        public void setStartLat(double StartLat) {
            this.StartLat = StartLat;
        }

        public double getStartLng() {
            return StartLng;
        }

        public void setStartLng(double StartLng) {
            this.StartLng = StartLng;
        }

        public String getStartToEndDistance() {
            return StartToEndDistance;
        }

        public void setStartToEndDistance(String StartToEndDistance) {
            this.StartToEndDistance = StartToEndDistance;
        }

        public double getVehToStartDistance() {
            return VehToStartDistance;
        }

        public void setVehToStartDistance(double VehToStartDistance) {
            this.VehToStartDistance = VehToStartDistance;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
    }

    public static class AddressListBean {
        /**
         * AIndex : 0
         * OrderNo : TO20161124000016
         * Receipter :
         * ReceiptTel :
         * ReceiptAddress : 金水区东风路  (河南省郑州市)
         * PaymentGoods : 0.0
         * Lat : 34.8
         * Lng : 113.64
         * ArriveTime : 0001-01-01T00:00:00
         * DischargeTime : 0001-01-01T00:00:00
         */

        private int AIndex;
        private String OrderNo;
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

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
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
}
