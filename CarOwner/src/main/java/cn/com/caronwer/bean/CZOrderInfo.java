package cn.com.caronwer.bean;

import java.util.List;

/**
 * --------------------------------------------------
 * 版       权 ：易成勇
 * <p>
 * 作       者： 易成勇
 * <p>
 * 文件名：CZOrderInfo
 * <p>
 * 创 建 日 期 ： 2016/11/24 0024  6:00
 * <p>
 * 描      述 ：
 * <p>
 * <p>
 * 修 订 历 史:
 * <p>
 * --------------------------------------------------
 */

public class CZOrderInfo {

    /**
     * Status : 1
     * Message : 获取信息成功
     * Data : [{"OrderNo":"TO20161103000002","PlanSendTime":"","Freight":200,"IsRemove":"","IsMove":"","IsSurcharge":"","IsToPay":"False","ToPayTel":"","IsCollectionPayment":"","Payment":"","VehicleType":"","VehicleTypeName":"","CreateTime":"2016/11/3 17:28:18","OrderType":"","BusinessType":"","OrderStatus":"1","FinanceStatus":"2","LogisticStatus":"","SenderId":"9d2c8eba-f9a6-44ab-88e7-a10543afc4eb","SenderName":"","SenderTel":"","OrderAddress":[{"AIndex":"0","Receipter":"","ReceiptTel":"","ReceiptAddress":"211111111","PaymentGoods":"10.00","Lat":"121.454500","Lng":"124.000000","ArriveTime":"","DischargeTime":""},{"AIndex":"0","Receipter":"","ReceiptTel":"","ReceiptAddress":"211111111","PaymentGoods":"50.00","Lat":"121.454500","Lng":"124.000000","ArriveTime":"","DischargeTime":""},{"AIndex":"1","Receipter":"","ReceiptTel":"","ReceiptAddress":"W22222222","PaymentGoods":"","Lat":"153.452450","Lng":"123.454500","ArriveTime":"","DischargeTime":""},{"AIndex":"2","Receipter":"","ReceiptTel":"","ReceiptAddress":"233333333","PaymentGoods":"30.00","Lat":"412.544500","Lng":"12.545000","ArriveTime":"","DischargeTime":""},{"AIndex":"3","Receipter":"","ReceiptTel":"","ReceiptAddress":"24444444","PaymentGoods":"40.00","Lat":"154.454520","Lng":"32.454500","ArriveTime":"","DischargeTime":""}]}]
     */

    private int Status;
    private String Message;
    private List<DataBean> Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * OrderNo : TO20161103000002
         * PlanSendTime :
         * Freight : 200
         * IsRemove :
         * IsMove :
         * IsSurcharge :
         * IsToPay : False
         * ToPayTel :
         * IsCollectionPayment :
         * Payment :
         * VehicleType :
         * VehicleTypeName :
         * CreateTime : 2016/11/3 17:28:18
         * OrderType :
         * BusinessType :
         * OrderStatus : 1
         * FinanceStatus : 2
         * LogisticStatus :
         * SenderId : 9d2c8eba-f9a6-44ab-88e7-a10543afc4eb
         * SenderName :
         * SenderTel :
         * OrderAddress : [{"AIndex":"0","Receipter":"","ReceiptTel":"","ReceiptAddress":"211111111","PaymentGoods":"10.00","Lat":"121.454500","Lng":"124.000000","ArriveTime":"","DischargeTime":""},{"AIndex":"0","Receipter":"","ReceiptTel":"","ReceiptAddress":"211111111","PaymentGoods":"50.00","Lat":"121.454500","Lng":"124.000000","ArriveTime":"","DischargeTime":""},{"AIndex":"1","Receipter":"","ReceiptTel":"","ReceiptAddress":"W22222222","PaymentGoods":"","Lat":"153.452450","Lng":"123.454500","ArriveTime":"","DischargeTime":""},{"AIndex":"2","Receipter":"","ReceiptTel":"","ReceiptAddress":"233333333","PaymentGoods":"30.00","Lat":"412.544500","Lng":"12.545000","ArriveTime":"","DischargeTime":""},{"AIndex":"3","Receipter":"","ReceiptTel":"","ReceiptAddress":"24444444","PaymentGoods":"40.00","Lat":"154.454520","Lng":"32.454500","ArriveTime":"","DischargeTime":""}]
         */

        private String OrderNo;
        private String PlanSendTime;
        private int Freight;
        private String IsRemove;
        private String IsMove;
        private String IsSurcharge;
        private String IsToPay;
        private String ToPayTel;
        private String IsCollectionPayment;
        private String Payment;
        private String VehicleType;
        private String VehicleTypeName;
        private String CreateTime;
        private String OrderType;
        private String BusinessType;
        private String OrderStatus;
        private String FinanceStatus;
        private String LogisticStatus;
        private String SenderId;
        private String SenderName;
        private String SenderTel;
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

        public int getFreight() {
            return Freight;
        }

        public void setFreight(int Freight) {
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

        public String getIsCollectionPayment() {
            return IsCollectionPayment;
        }

        public void setIsCollectionPayment(String IsCollectionPayment) {
            this.IsCollectionPayment = IsCollectionPayment;
        }

        public String getPayment() {
            return Payment;
        }

        public void setPayment(String Payment) {
            this.Payment = Payment;
        }

        public String getVehicleType() {
            return VehicleType;
        }

        public void setVehicleType(String VehicleType) {
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

        public String getOrderType() {
            return OrderType;
        }

        public void setOrderType(String OrderType) {
            this.OrderType = OrderType;
        }

        public String getBusinessType() {
            return BusinessType;
        }

        public void setBusinessType(String BusinessType) {
            this.BusinessType = BusinessType;
        }

        public String getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(String OrderStatus) {
            this.OrderStatus = OrderStatus;
        }

        public String getFinanceStatus() {
            return FinanceStatus;
        }

        public void setFinanceStatus(String FinanceStatus) {
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

        public List<OrderAddressBean> getOrderAddress() {
            return OrderAddress;
        }

        public void setOrderAddress(List<OrderAddressBean> OrderAddress) {
            this.OrderAddress = OrderAddress;
        }

        /*
        public static class OrderAddressBean {

            private String AIndex;
            private String Receipter;
            private String ReceiptTel;
            private String ReceiptAddress;
            private String PaymentGoods;
            private String Lat;
            private String Lng;
            private String ArriveTime;
            private String DischargeTime;

            public String getAIndex() {
                return AIndex;
            }

            public void setAIndex(String AIndex) {
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

            public String getPaymentGoods() {
                return PaymentGoods;
            }

            public void setPaymentGoods(String PaymentGoods) {
                this.PaymentGoods = PaymentGoods;
            }

            public String getLat() {
                return Lat;
            }

            public void setLat(String Lat) {
                this.Lat = Lat;
            }

            public String getLng() {
                return Lng;
            }

            public void setLng(String Lng) {
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
}
