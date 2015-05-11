package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.golf.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-7.
 */
public class PropListModel implements Serializable {
    @JSONCollection(type = PropModel.class)
    private List<PropModel> props;

    public List<PropModel> getProps() {
        return props;
    }

    public void setProps(List<PropModel> props) {
        this.props = props;
    }

    private static IContentDecoder<PropListModel> decoder = new IContentDecoder.BeanDecoder<PropListModel>(PropListModel.class);

    public static IPromise get() {
        return Http.instance().get(ApiUrl.PROP_LIST).
                contentDecoder(decoder).run();
    }
    public static IPromise getMyProps() {
        return Http.instance().get(ApiUrl.MY_PROPS).
                contentDecoder(decoder).run();
    }
}
