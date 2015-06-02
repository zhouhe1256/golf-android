package com.bjcathay.qt.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-25.
 */
public class InviteModel implements Serializable {
    private Long userId;// 7,
    private String name;// "18810734340",
    private String date;// "2015-09-09 09:09:09",
    private boolean valid;// false|true
    private static IContentDecoder<InviteModel> decoder = new IContentDecoder.BeanDecoder<InviteModel>(InviteModel.class);

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
