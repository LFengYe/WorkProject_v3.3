package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/16.
 */
public class Duichang {
    private int no;
    private String StorageYardNmae;
    private String Address;
    private String Price;

    public Duichang(int no, String StorageYardNmae, String Address, String Price){
        this.no=no;
        this.StorageYardNmae = StorageYardNmae;
        this.Address = Address;
        this.Price =Price;

    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setStorageYardNmae(String storageYardNmae) {
        StorageYardNmae = storageYardNmae;
    }

    public String getAddress() {
        return Address;
    }

    public int getNo() {
        return no;
    }

    public String getPrice() {
        return Price;
    }

    public String getStorageYardNmae() {
        return StorageYardNmae;
    }
}
