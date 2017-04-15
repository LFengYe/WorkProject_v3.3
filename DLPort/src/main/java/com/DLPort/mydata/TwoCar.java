package com.DLPort.mydata;

/**
 * Created by Administrator on 2016/5/18.
 */
public class TwoCar {

    private String CarImage;
    private String CarBrand;
    private String CarType;
    private String Price;
    private String Telephone;

    public TwoCar(  String CarImage, String CarBrand, String CarType,
         String Price, String Telephone){

        this.CarImage = CarImage;
        this.CarBrand = CarBrand;
        this.CarType = CarType;
        this.Price = Price;
        this.Telephone = Telephone;

    }

    public void setCarImage(String carImage) {
        CarImage = carImage;
    }

    public void setCarBrand(String carBrand) {
        CarBrand = carBrand;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }



    public void setPrice(String price) {
        Price = price;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getCarBrand() {
        return CarBrand;
    }

    public String getCarImage() {
        return CarImage;
    }

    public String getCarType() {
        return CarType;
    }



    public String getPrice() {
        return Price;
    }

    public String getTelephone() {
        return Telephone;
    }
}
