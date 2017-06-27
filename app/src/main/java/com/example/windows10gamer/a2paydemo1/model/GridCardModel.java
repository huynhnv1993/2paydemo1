package com.example.windows10gamer.a2paydemo1.model;

/**
 * Created by Windows 10 Gamer on 27/06/2017.
 */

public class GridCardModel {
    public int id;
    public String code,name;
    public double import_discount,discount;
    public int sale_price,price;

    public GridCardModel(int id, String code, String name, double import_discount, double discount, int sale_price, int price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.import_discount = import_discount;
        this.discount = discount;
        this.sale_price = sale_price;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getImport_discount() {
        return import_discount;
    }

    public void setImport_discount(double import_discount) {
        this.import_discount = import_discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setImport_discount(float import_discount) {
        this.import_discount = import_discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getSale_price() {
        return sale_price;
    }

    public void setSale_price(int sale_price) {
        this.sale_price = sale_price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
