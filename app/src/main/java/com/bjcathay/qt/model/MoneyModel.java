package com.bjcathay.qt.model;

import java.io.Serializable;

/**
 * Created by jiangm on 15-9-14.
 */
public class MoneyModel implements Serializable {
    private long id;
    private String name;
    private String description;
    private double money;
    private double fan;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getFan() {
        return fan;
    }

    public void setFan(double fan) {
        this.fan = fan;
    }
}
