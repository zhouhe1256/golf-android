package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.mallfm.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-1-4.
 */
public class UpdateModel implements Serializable {

    private double version;
    private String downUrl;
    private String description;

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private static IContentDecoder<UpdateModel> decoder = new IContentDecoder.BeanDecoder<UpdateModel>(UpdateModel.class, "version");

    public static IPromise sendVersion(String version) {
        return Http.instance().get(ApiUrl.UPDATE).
                param("version", version).param("driveType", "ANDROID").contentDecoder(decoder).run();
    }
}
