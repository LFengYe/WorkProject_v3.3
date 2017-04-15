package com.gpw.app.bean;

/**
 * Created by Administrator on 2016/11/18.
 * ---个人专属
 */

public class MoneyInfo {

    /**
     * Balance : 100
     * Frozen : 10
     */

    private double Balance;
    private double Frozen;

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public double getFrozen() {
        return Frozen;
    }

    public void setFrozen(double Frozen) {
        this.Frozen = Frozen;
    }
}

