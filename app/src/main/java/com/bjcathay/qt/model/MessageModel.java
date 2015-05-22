package com.bjcathay.qt.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-11.
 */
public class MessageModel implements Serializable {
    private Long id;//1,
    private String name;//"赛事名称",
    private String description;// "描述",
    private String content;// "消息内容",
    private String type;// "xxx",
    private String imageUrl;// "/upload/image/xxx.png",
    private String relativeDate;
    private String target;
    private String status;

    private static IContentDecoder<MessageModel> decoder = new IContentDecoder.BeanDecoder<MessageModel>(MessageModel.class, "message");

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

    public String getType() {
        return type;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
