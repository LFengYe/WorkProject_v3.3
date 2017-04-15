package cn.com.caronwer.bean;

import java.util.List;

/**
 * Created by  on .
 * --------------------------
 * 版   权 ：
 * <p>
 * 作   者 ：X230
 * 文件名 ：
 * <p>
 * 创建于：2016/11/26 16:27
 * 概  述:
 */

public class AlreadyExistingOrderInfo {

    /**
     * Status : 1
     * Message : 获取成功
     * Data : [{"OrderNo":"OrderNo","PlanSendTime":"2016-11-17 00:00:00"}]
     */

    private int Status;
    private String Message;
    /**
     * OrderNo : OrderNo
     * PlanSendTime : 2016-11-17 00:00:00
     */

    private List<DataBean> Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private String OrderNo;
        private String PlanSendTime;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getPlanSendTime() {
            return PlanSendTime;
        }

        public void setPlanSendTime(String PlanSendTime) {
            this.PlanSendTime = PlanSendTime;
        }
    }
}
