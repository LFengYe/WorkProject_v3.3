package cn.com.caronwer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderAddressBean implements Parcelable {
        /**
         * AIndex : 0
         * Receipter : 123456
         * ReceiptTel : 123456789
         * ReceiptAddress : 湖北汽车工业学院   (湖北省十堰市张湾区G316(车城西路))
         * PaymentGoods : 0.0
         * Lat : 32.656148
         * Lng : 110.747101
         * ArriveTime :
         * DischargeTime :
         */

        private int AIndex;
        private String Receipter;
        private String ReceiptTel;
        private String ReceiptAddress;
        private double PaymentGoods;
        private double Lat;
        private double Lng;
        private String ArriveTime;
        private String DischargeTime;

        protected OrderAddressBean(Parcel in) {
            AIndex = in.readInt();
            Receipter = in.readString();
            ReceiptTel = in.readString();
            ReceiptAddress = in.readString();
            PaymentGoods = in.readDouble();
            Lat = in.readDouble();
            Lng = in.readDouble();
            ArriveTime = in.readString();
            DischargeTime = in.readString();
        }

        public static final Creator<OrderAddressBean> CREATOR = new Creator<OrderAddressBean>() {
            @Override
            public OrderAddressBean createFromParcel(Parcel in) {
                return new OrderAddressBean(in);
            }

            @Override
            public OrderAddressBean[] newArray(int size) {
                return new OrderAddressBean[size];
            }
        };

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

        public OrderAddressBean(int AIndex, String receipter, String receiptTel, String receiptAddress, double paymentGoods, double lat, double lng, String arriveTime, String dischargeTime) {
            this.AIndex = AIndex;
            Receipter = receipter;
            ReceiptTel = receiptTel;
            ReceiptAddress = receiptAddress;
            PaymentGoods = paymentGoods;
            Lat = lat;
            Lng = lng;
            ArriveTime = arriveTime;
            DischargeTime = dischargeTime;
        }

        public String getArriveTime() {
            return ArriveTime;
        }

        @Override
        public String toString() {
            return "OrderAddressBean{" +
                    "AIndex=" + AIndex +
                    ", Receipter='" + Receipter + '\'' +
                    ", ReceiptTel='" + ReceiptTel + '\'' +
                    ", ReceiptAddress='" + ReceiptAddress + '\'' +
                    ", PaymentGoods=" + PaymentGoods +
                    ", Lat=" + Lat +
                    ", Lng=" + Lng +
                    ", ArriveTime='" + ArriveTime + '\'' +
                    ", DischargeTime='" + DischargeTime + '\'' +
                    '}';
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







        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(this.Receipter);
            dest.writeString(this.ReceiptTel);
            dest.writeString(this.ReceiptAddress);
            dest.writeString(this.ArriveTime);
            dest.writeString(this.DischargeTime);
            dest.writeDouble(this.PaymentGoods);
            dest.writeDouble(this.Lat);
            dest.writeDouble(this.Lng);
            dest.writeInt(this.AIndex);
        }
    }