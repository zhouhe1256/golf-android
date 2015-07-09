
package com.bjcathay.qt.model;

import java.io.Serializable;

/**
 * Created by dengt on 15-6-25.
 */
public class CModel implements Serializable {
    private long id;
    private String name;

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
}
