package cn.com.caronwer.bean;

/**
 * Created by LFeng on 2017/7/21.
 */

public class PayResult {
    private WeChatPay result;
    private float Amount;

    public WeChatPay getResult() {
        return result;
    }

    public void setResult(WeChatPay result) {
        this.result = result;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }
}
