package com.guugoo.jiapeistudent.Data;

/**
 * Created by Administrator on 2016/8/7.
 */
public class ReturnData {

    private int Status;
    private String Message;
    private String Data;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
