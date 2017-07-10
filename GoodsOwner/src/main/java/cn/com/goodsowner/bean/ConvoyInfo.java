package cn.com.goodsowner.bean;

/**
 * Created by gpy9983 on 2016/11/15.
 */

public class ConvoyInfo {

    /**
     * TransporterId : a1777987-fc90-4ce3-808d-79f13582b7d2
     * TransporterName : 车主
     * Tel : 13530177675
     * Score : 12
     * VehicleNo : 车牌号
     * CreateTime : 2016/11/8 0:00:00
     */

    private String TransporterId;
    private String TransporterName;
    private String Tel;
    private int Score;
    private String VehicleNo;
    private String CreateTime;

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

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String VehicleNo) {
        this.VehicleNo = VehicleNo;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
