
package com.bjcathay.qt.model;

import com.bjcathay.qt.Enumeration.MessageType;

/**
 * Created by dengt on 15-5-22.
 */
public class PushModel {
    private String t;
    private String g;
    private String m;

    public MessageType.pushMsgType getT() {
        return MessageType.pushMsgType.valueOf(t);
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
