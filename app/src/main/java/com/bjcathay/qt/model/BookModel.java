package com.bjcathay.qt.model;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-18.
 */
public class BookModel implements Serializable{
    private String name;
    private String phone;
    private String data;
    private String sortLetters;  //显示数据拼音的首字母
    private String  status; //是否是注册用户

    /*public BookModel(String name, String phone, String data) {
        this.name = name;
        this.phone = phone;
        this.data = data;
    }*/

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
