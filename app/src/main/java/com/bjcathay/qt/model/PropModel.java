package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-7.
 */
public class PropModel implements Serializable {
    private Long id;// 1,
    private String name;// "名称",
    private String description;// "描述",
    private String type;// COMPETITION,
    private int needAmount;// 5,
    private int targetId;// 2,
    private String imageUrl;// "/upload/image/xxx.png"
 /*   private String exchange;// true|false, 是否已兑换
    private String status;// UNUSED|USED*/
    private static IContentDecoder<PropModel> decoder = new IContentDecoder.BeanDecoder<PropModel>(PropModel.class, "prop");

    //兑换道具
    public static IPromise getProp(Long id) {
        return Http.instance().post(ApiUrl.propDetail(id)).
                run();
    }

    //赠送道具
    public static IPromise sendProp(Long id,String phone) {
        return Http.instance().post(ApiUrl.sendProp(id)).
                param("phone",phone).run();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNeedAmount() {
        return needAmount;
    }

    public void setNeedAmount(int needAmount) {
        this.needAmount = needAmount;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
