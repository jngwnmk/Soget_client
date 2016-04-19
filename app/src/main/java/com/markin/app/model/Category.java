package com.markin.app.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by wonmook on 2016. 4. 12..
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Category {
    private String id;
    private String type;
    private String desc;
    private String author;
    private String imgUrl;
    private int check;
    private int unCheck;

    public Category(){
        this.id = "";
        this.type = "";
        this.desc = "";
        this.author = "";
        this.imgUrl="";
        this.check = 0;
        this.unCheck = 0;

    }
    public Category(String id, String type, String desc, String author, String imgUrl,int check, int unCheck) {
        this.id = id;
        this.type = type;
        this.desc = desc;
        this.author = author;
        this.imgUrl = imgUrl;
        this.check = check;
        this.unCheck = unCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getUnCheck() {
        return unCheck;
    }

    public void setUnCheck(int unCheck) {
        this.unCheck = unCheck;
    }
}
