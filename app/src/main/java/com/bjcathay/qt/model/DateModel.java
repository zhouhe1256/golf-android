package com.bjcathay.qt.model;

import java.util.List;

/**
 * Created by bjcathay on 15-5-18.
 */
public class DateModel {

    List<String> days;
    List<String> hours;
    boolean amOpm;
    double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public List<String> getHours() {
        return hours;
    }

    public void setHours(List<String> hours) {
        this.hours = hours;
    }

    public boolean isAmOpm() {
        return amOpm;
    }

    public void setAmOpm(boolean amOpm) {
        this.amOpm = amOpm;
    }
}
