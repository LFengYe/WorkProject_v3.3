package cn.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/9.
 */
public class Coach {
    private static final String TAG = "Coach";

    /**
     * TId : 教练编号
     * HeadPortrait : 头像
     * Name : 教练名称
     * Branch : 场地
     * ComprehensiveLevel : 教练综合星级
     * UniqueId : 该教练的预约单
     * BestCount:可预约数量
     */

    private int TId;
    private String HeadPortrait;
    private String Name;
    private String Branch;
    private int ComprehensiveLevel;
    private String UniqueId;
    private int BestCount;
    private String VehNof;
    private String Tel;
    private String Seniority;


    public int getBestCount() {
        return BestCount;
    }

    public void setBestCount(int bestCount) {
        BestCount = bestCount;
    }

    public int getTId() {
        return TId;
    }

    public void setTId(int TId) {
        this.TId = TId;
    }

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String Branch) {
        this.Branch = Branch;
    }

    public int getComprehensiveLevel() {
        return ComprehensiveLevel;
    }

    public void setComprehensiveLevel(int comprehensiveLevel) {
        ComprehensiveLevel = comprehensiveLevel;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getVehNof() {
        return VehNof;
    }

    public void setVehNof(String vehNof) {
        VehNof = vehNof;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getSeniority() {
        return Seniority;
    }

    public void setSeniority(String seniority) {
        Seniority = seniority;
    }
}
