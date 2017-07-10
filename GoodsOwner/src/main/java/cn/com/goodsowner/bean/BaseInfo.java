package cn.com.goodsowner.bean;

import com.google.gson.JsonElement;

/**
 * Created by Administrator on 2016/11/14.
 * ---个人专属
 */

public class BaseInfo {
    private int Status;
    private String Message;
    private JsonElement Data;


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public JsonElement getData() {
        return Data;
    }

    public void setData(JsonElement data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "BaseInfo{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
