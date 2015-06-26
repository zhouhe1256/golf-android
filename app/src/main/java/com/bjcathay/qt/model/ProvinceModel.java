package com.bjcathay.qt.model;

import java.io.Serializable;

/**
 * Created by dengt on 15-6-25.
 */
public class ProvinceModel implements Serializable {
    private long id;//1,
    private String name;//"北京",
    private boolean hot;//true|false,
    private String firstLetter;// "b",
    private String shortLetter;// "bj",
    private String allLetter;// "beijing"

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getShortLetter() {
        return shortLetter;
    }

    public void setShortLetter(String shortLetter) {
        this.shortLetter = shortLetter;
    }

    public String getAllLetter() {
        return allLetter;
    }

    public void setAllLetter(String allLetter) {
        this.allLetter = allLetter;
    }
}
