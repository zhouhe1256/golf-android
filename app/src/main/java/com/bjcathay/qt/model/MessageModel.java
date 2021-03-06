
package com.bjcathay.qt.model;

import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.Enumeration.MessageType;

import java.io.Serializable;

/**
 * Created by dengt on 15-5-11.
 */
public class MessageModel implements Serializable {
    private Long id;// 1,
    private String target;
    private String status;
    private String content;// "消息内容",
    private String name;// "赛事名称",
    private String type;// "xxx",
    private String description;// "描述",
    private String relativeDate;
    private String created;
    private String subType;
    // 待定
    private String imageUrl;// "/upload/image/xxx.png",

    private static IContentDecoder<MessageModel> decoder = new IContentDecoder.BeanDecoder<MessageModel>(
            MessageModel.class, "message");

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType.msgType getType() {
        try {
            return MessageType.msgType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return MessageType.msgType.OTHER;
        }
    }

    public MessageType.pushMsgType getSubType() {
        try {
            return MessageType.pushMsgType.valueOf(subType);
        } catch (IllegalArgumentException e) {
            return MessageType.pushMsgType.OTHER;
        }
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRelativeDate() {
        return relativeDate;
    }

    public void setRelativeDate(String relativeDate) {
        this.relativeDate = relativeDate;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public MessageType.msgReadType getStatus() {
        return MessageType.msgReadType.valueOf(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
