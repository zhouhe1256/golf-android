package com.bjcathay.golf.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-11.
 */
public class MessageModel implements Serializable {
    private Long id;//1,
    private String name;//"赛事名称",
    private String description;// "赛事描述",
    private String content;// "消息内容",
    private String type;// "xxx",
    private String targetId;// "xxx",
    private String imageUrl;// "/upload/image/xxx.png",
    private String websiteUrl;//"http://www.bjcathay.com"
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
