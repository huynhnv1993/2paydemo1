package com.example.windows10gamer.a2paydemo1.model;

import org.json.JSONArray;

/**
 * Created by Windows 10 Gamer on 26/06/2017.
 */

public class SpinnerModel {
    private String Companyname;
    private Integer Image;
    private String sub;
    private JSONArray code;


    public SpinnerModel(String companyname, Integer image, String sub, JSONArray code) {
        Companyname = companyname;
        Image = image;
        this.sub = sub;
        this.code = code;
    }

    public JSONArray getCode() {
        return code;
    }

    public void setCode(JSONArray code) {
        this.code = code;
    }

    public Integer getImage() {
        return Image;
    }

    public void setImage(Integer image) {
        Image = image;
    }

    public String getCompanyname() {
        return Companyname;
    }

    public void setCompanyname(String companyname) {
        Companyname = companyname;
    }


    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}