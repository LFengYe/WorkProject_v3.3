package com.guugoo.jiapeiteacher.bean;

import com.google.gson.JsonElement;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class TotalPaperData {

    /**
     * status : -1
     * message : 登录出错
     * data :
     */

    private int Status;
    private String Message;
    private JsonElement Data;
    private int TotalPage;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public JsonElement getData() {
        return Data;
    }

    public void setData(JsonElement data) {
        this.Data = data;
    }

    public int getTotalPage() {
        return TotalPage;
    }

    public void setTotalPage(int totalPage) {
        TotalPage = totalPage;
    }

    @Override
    public String toString() {
        return "TotalPaperData{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                ", TotalPage=" + TotalPage +
                '}';
    }
}
