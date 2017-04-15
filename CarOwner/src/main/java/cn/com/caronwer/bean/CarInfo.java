package cn.com.caronwer.bean;

/**
 * Created by Administrator on 2016/11/17.
 * ---个人专属
 */

public class CarInfo {

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
