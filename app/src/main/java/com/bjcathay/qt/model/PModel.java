package com.bjcathay.qt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-6-24.
 */
public class PModel implements Serializable {
    private String province;
    private List<CModel> city;
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<CModel> getCity() {
        return city;
    }

    public void setCity(List<CModel> city) {
        this.city = city;
    }
}
