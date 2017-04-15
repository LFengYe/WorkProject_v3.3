package com.gpw.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 * ---个人专属
 */

public class CarInfo {

    /**
     * ReturnPayRate : 30.0
     * VehicleTypeList : [{"TypeCode":"1","VehicleTypeName":"小面包车","Img":"/imgs/102.png","Volume":3,"LoadWeight":500,"StartingPrice":20,"FollowPrice":10,"Remark":""},{"TypeCode":"2","VehicleTypeName":"中面包车","Img":"/imgs/112.png","Volume":5,"LoadWeight":800,"StartingPrice":30,"FollowPrice":11,"Remark":""},{"TypeCode":"3","VehicleTypeName":"小型货车","Img":"/imgs/122.png","Volume":10,"LoadWeight":1500,"StartingPrice":40,"FollowPrice":15,"Remark":""},{"TypeCode":"4","VehicleTypeName":"中型货车","Img":"/imgs/132.png","Volume":15,"LoadWeight":3000,"StartingPrice":80,"FollowPrice":20,"Remark":""}]
     */

    private double ReturnPayRate;
    /**
     * TypeCode : 1
     * VehicleTypeName : 小面包车
     * Img : /imgs/102.png
     * Volume : 3.0
     * LoadWeight : 500.0
     * StartingPrice : 20.0
     * FollowPrice : 10.0
     * Remark :
     */

    private List<VehicleTypeListBean> VehicleTypeList;

    public double getReturnPayRate() {
        return ReturnPayRate;
    }

    public void setReturnPayRate(double ReturnPayRate) {
        this.ReturnPayRate = ReturnPayRate;
    }

    public List<VehicleTypeListBean> getVehicleTypeList() {
        return VehicleTypeList;
    }

    public void setVehicleTypeList(List<VehicleTypeListBean> VehicleTypeList) {
        this.VehicleTypeList = VehicleTypeList;
    }

    public static class VehicleTypeListBean {
        private String TypeCode;
        private String VehicleTypeName;
        private String Img;
        private double Volume;
        private double LoadWeight;
        private double StartingPrice;
        private double FollowPrice;
        private String Remark;

        public String getTypeCode() {
            return TypeCode;
        }

        public void setTypeCode(String TypeCode) {
            this.TypeCode = TypeCode;
        }

        public String getVehicleTypeName() {
            return VehicleTypeName;
        }

        public void setVehicleTypeName(String VehicleTypeName) {
            this.VehicleTypeName = VehicleTypeName;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String Img) {
            this.Img = Img;
        }

        public double getVolume() {
            return Volume;
        }

        public void setVolume(double Volume) {
            this.Volume = Volume;
        }

        public double getLoadWeight() {
            return LoadWeight;
        }

        public void setLoadWeight(double LoadWeight) {
            this.LoadWeight = LoadWeight;
        }

        public double getStartingPrice() {
            return StartingPrice;
        }

        public void setStartingPrice(double StartingPrice) {
            this.StartingPrice = StartingPrice;
        }

        public double getFollowPrice() {
            return FollowPrice;
        }

        public void setFollowPrice(double FollowPrice) {
            this.FollowPrice = FollowPrice;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
    }
}
