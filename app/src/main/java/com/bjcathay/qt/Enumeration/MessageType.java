
package com.bjcathay.qt.Enumeration;

import java.io.Serializable;

/**
 * Created by dengt on 15-8-1.
 */
public class MessageType implements Serializable {
    public enum msgType {
        NOTIFY, ORDER, SYSTEM, PROPERTY, COMPETITION;
    }
    public enum msgReadType{
        UNREAD,READ;
    }
}
