package cn.com.caronwer.bean;

/**
 * Created by gpy9983 on 2016/11/20.
 */

public class OrderInfo {

    /**
     * SendUserId : 9D2C8EBA-F9A6-44AB-88E7-A10543AFC4EB
     * VehideTypeId : 3
     * IsRemove : true
     * IsMove : false
     * IsSurcharge : true
     * IsToPayFreight : true
     * ToPayFreightTel : 1353017675
     * IsCollectionPayment : true
     * Payment : 5000.0
     * IsMyFleet : true
     * OrderAddress : 深圳北,悟空,1357878888,24.124512,116.02451,0|大学城,张三,13588888888,23.124512,116.12451,1000.00|西丽,张四,13588888888,23.224512,116.32451,1000.00|宝安交通局,王五,1357777888,23.324512,116.32451,1000.00|翻身地铁站,赵六,13588999988,23.324512,116.42451,1000.00|前海湾,洛溪,1358666888,23.524512,116.72451,1000.00
     * Remark : 司机司机快点接单
     * Premiums : 100
     * OrderType : 1
     * PlanSendTime : 2016-11-10 12:00
     */

    private String SendUserId;
    private int VehideTypeId;
    private boolean IsRemove;
    private boolean IsMove;
    private boolean IsSurcharge;
    private boolean IsToPayFreight;
    private String ToPayFreightTel;
    private boolean IsCollectionPayment;
    private double Payment;
    private boolean IsMyFleet;
    private String OrderAddress;
    private String Remark;
    private int Premiums;
    private int OrderType;
    private String PlanSendTime;
    private double Volume;
    private double Weight;
    private int Quantity;

    public String getSendUserId() {
        return SendUserId;
    }

    public void setSendUserId(String SendUserId) {
        this.SendUserId = SendUserId;
    }

    public int getVehideTypeId() {
        return VehideTypeId;
    }

    public void setVehideTypeId(int VehideTypeId) {
        this.VehideTypeId = VehideTypeId;
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

    public boolean isIsToPayFreight() {
        return IsToPayFreight;
    }

    public void setIsToPayFreight(boolean IsToPayFreight) {
        this.IsToPayFreight = IsToPayFreight;
    }

    public String getToPayFreightTel() {
        return ToPayFreightTel;
    }

    public void setToPayFreightTel(String ToPayFreightTel) {
        this.ToPayFreightTel = ToPayFreightTel;
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

    public boolean isIsMyFleet() {
        return IsMyFleet;
    }

    public void setIsMyFleet(boolean IsMyFleet) {
        this.IsMyFleet = IsMyFleet;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String OrderAddress) {
        this.OrderAddress = OrderAddress;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getPremiums() {
        return Premiums;
    }

    public void setPremiums(int Premiums) {
        this.Premiums = Premiums;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int OrderType) {
        this.OrderType = OrderType;
    }

    public String getPlanSendTime() {
        return PlanSendTime;
    }

    public void setPlanSendTime(String PlanSendTime) {
        this.PlanSendTime = PlanSendTime;
    }

    public boolean isRemove() {
        return IsRemove;
    }

    public void setRemove(boolean remove) {
        IsRemove = remove;
    }

    public boolean isMove() {
        return IsMove;
    }

    public void setMove(boolean move) {
        IsMove = move;
    }

    public boolean isSurcharge() {
        return IsSurcharge;
    }

    public void setSurcharge(boolean surcharge) {
        IsSurcharge = surcharge;
    }

    public boolean isCollectionPayment() {
        return IsCollectionPayment;
    }

    public void setCollectionPayment(boolean collectionPayment) {
        IsCollectionPayment = collectionPayment;
    }

    public boolean isToPayFreight() {
        return IsToPayFreight;
    }

    public void setToPayFreight(boolean toPayFreight) {
        IsToPayFreight = toPayFreight;
    }

    public boolean isMyFleet() {
        return IsMyFleet;
    }

    public void setMyFleet(boolean myFleet) {
        IsMyFleet = myFleet;
    }

    public double getVolume() {
        return Volume;
    }

    public void setVolume(double volume) {
        Volume = volume;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
