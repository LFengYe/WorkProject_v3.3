package com.guugoo.jiapeistudent.Data;

/**
 * Created by LFeng on 2017/7/28.
 */

public class BranchInfo {
    private int SchoolId;
    private String BranchSchoolName;
    private int Id;
    private String Address;

    public int getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public String getBranchSchoolName() {
        return BranchSchoolName;
    }

    public void setBranchSchoolName(String branchSchoolName) {
        BranchSchoolName = branchSchoolName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public String toString() {
        return this.BranchSchoolName;
    }
}
