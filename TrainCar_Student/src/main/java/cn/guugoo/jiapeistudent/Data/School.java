package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/15.
 */
public class School {
    private static final String TAG = "School";

    /**
     * SchoolName : 驾校名称
     * Address : 总校址
     *  SchoolInfo : 学校简介
     *  Url : 官网地址
     *  Tel : 联系电话
     *  SchoolManager : 校长
     *  ExpandHistory : 发展历史
     *  SchoolHonor : 学校荣誉
     *  SchoolCulture : 企业文化
     *  Id : 1
     *  Province : 广东省
     *  City : 深圳市
     */

    private String SchoolName;
    private String Address;
    private String SchoolInfo;
    private String Url;
    private String Tel;
    private String SchoolManager;
    private String ExpandHistory;
    private String SchoolHonor;
    private String SchoolCulture;
    private String Id;
    private String Province;
    private String City;

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String SchoolName) {
        this.SchoolName = SchoolName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getSchoolInfo() {
        return SchoolInfo;
    }

    public void setSchoolInfo(String schoolInfo) {
        SchoolInfo = schoolInfo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getSchoolManager() {
        return SchoolManager;
    }

    public void setSchoolManager(String schoolManager) {
        SchoolManager = schoolManager;
    }

    public String getExpandHistory() {
        return ExpandHistory;
    }

    public void setExpandHistory(String expandHistory) {
        ExpandHistory = expandHistory;
    }

    public String getSchoolHonor() {
        return SchoolHonor;
    }

    public void setSchoolHonor(String schoolHonor) {
        SchoolHonor = schoolHonor;
    }

    public String getSchoolCulture() {
        return SchoolCulture;
    }

    public void setSchoolCulture(String schoolCulture) {
        SchoolCulture = schoolCulture;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
