
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
    // 待定
    private String imageUrl;// "/upload/image/xxx.png",

    private static IContentDecoder<MessageModel> decoder = new IContentDecoder.BeanDecoder<MessageModel>(
            MessageModel.class, "message");

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
        return MessageType.msgType.valueOf(type);
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
