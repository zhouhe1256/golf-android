
package com.bjcathay.qt.model;

import java.io.Serializable;

/**
 * Created by dengt on 15-5-18.
 */
public class BookModel implements Serializable {
    private String name;
    private String phone;
    private String data;
    private String sortLetters; // 显示数据拼音的首字母
    private String status; // 是否是注册用户

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

    @Override
    public boolean equals(Object obj) {
        BookModel s = (BookModel) obj;
        return phone.equals(s.phone);
    }

    @Override
    public int hashCode() {
        String in = phone;
        return in.hashCode();
    }
}
