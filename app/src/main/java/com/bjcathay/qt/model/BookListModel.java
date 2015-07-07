
package com.bjcathay.qt.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-7-3.
 */
public class BookListModel implements Serializable {

    @JSONCollection(type = BModel.class)
    private List<BModel> persons;

    public List<BModel> getPersons() {
        return persons;
    }

    public void setPersons(List<BModel> persons) {
        this.persons = persons;
    }
}
