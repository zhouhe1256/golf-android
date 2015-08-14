
package com.bjcathay.qt.model;

import com.bjcathay.qt.Enumeration.MessageType;

import java.io.Serializable;

/**
 * Created by dengt on 15-5-22.
 */
public class PushModel implements Serializable{
    private String t;
    private String id;
    private String m;

    public MessageType.pushMsgType getT() {
        try {
            return MessageType.pushMsgType.valueOf(t);
        } catch (IllegalArgumentException e) {
            return MessageType.pushMsgType.OTHER;
        }
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
