package com.example.youxi.kfc;

/**
 * Created by youxi on 2016-6-4.
 */
public class Discount {
    private String img;
    private String product_name;
    private String product_price;
    private String validity;
    private String no;

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {

        return no;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getValidity() {

        return validity;
    }

    public String getImg() {
        return img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
