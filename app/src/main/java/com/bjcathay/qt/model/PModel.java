
package com.bjcathay.qt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-6-24.
 */
public class PModel implements Serializable {
    private long id;
    private String province;
    private List<CModel> city;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
