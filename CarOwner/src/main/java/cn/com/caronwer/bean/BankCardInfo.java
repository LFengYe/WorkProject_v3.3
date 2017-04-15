package cn.com.caronwer.bean;

/**
 * Created by  on .
 * --------------------------
 * 版   权 ：
 * <p>
 * 作   者 ：X230
 * 文件名 ：
 * <p>
 * 创建于：2016/11/28 19:15
 * 概  述:
 */

public class BankCardInfo {


    /**
     * CardId : 1
     * BankId : 1
     * BankName :
     * BankBranch : 支行名称修改测试
     * AccountName : 户名
     * AccountNumber : 12345678945
     */

    private int CardId;
    private int BankId;
    private String BankName;
    private String BankBranch;
    private String AccountName;
    private String AccountNumber;

    public int getCardId() {
        return CardId;
    }

    public void setCardId(int CardId) {
        this.CardId = CardId;
    }

    public int getBankId() {
        return BankId;
    }

    public void setBankId(int BankId) {
        this.BankId = BankId;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public String getBankBranch() {
        return BankBranch;
    }

    public void setBankBranch(String BankBranch) {
        this.BankBranch = BankBranch;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String AccountNumber) {
        this.AccountNumber = AccountNumber;
    }
}
