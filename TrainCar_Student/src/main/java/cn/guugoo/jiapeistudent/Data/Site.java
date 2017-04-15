package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/9.
 */
public class Site {
    private static final String TAG = "Site";

    /**
     * Bid : 训练场编号
     * BranchSchoolName  : 场地名称
     *  BranchImg : 场地图片
     *  Distance : 场地距离
     */

    private int Bid;
    private String BranchSchoolName;
    private String BranchImg;
    private String Distance;
    private String RestCount;
    private String Address;

    public Site() {
    }

    public Site(int bid, String branchSchoolName, String branchImg, String distance) {
        Bid = bid;
        BranchSchoolName = branchSchoolName;
        BranchImg = branchImg;
        Distance = distance;
    }

    public int getBid() {
        return Bid;
    }

    public void setBid(int bid) {
        Bid = bid;
    }

    public String getBranchSchoolName() {
        return BranchSchoolName;
    }

    public void setBranchSchoolName(String branchSchoolName) {
        BranchSchoolName = branchSchoolName;
    }

    public String getBranchImg() {
        return BranchImg;
    }

    public void setBranchImg(String branchImg) {
        BranchImg = branchImg;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getRestCount() {
        return RestCount;
    }

    public void setRestCount(String restCount) {
        RestCount = restCount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
