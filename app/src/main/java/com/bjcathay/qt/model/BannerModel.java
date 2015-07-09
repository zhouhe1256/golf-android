
package com.bjcathay.qt.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by dengt on 15-5-5.
 */
public class BannerModel implements Serializable {
    private Long id;
    private String title;
    private String type;
    private String target;
    private String imageUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private static IContentDecoder<BannerModel> decoder = new IContentDecoder.BeanDecoder<BannerModel>(
            BannerModel.class, "banner");

}
