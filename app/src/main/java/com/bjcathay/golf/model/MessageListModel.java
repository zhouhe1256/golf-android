package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.golf.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-11.
 */
public class MessageListModel implements Serializable {
    @JSONCollection(type = MessageModel.class)
    private List<MessageModel> messages;

    public List<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
    }

    private static IContentDecoder<MessageListModel> decoder = new IContentDecoder.BeanDecoder<MessageListModel>(MessageListModel.class);

    public static IPromise getMyMessage(int page, String lastUpdatedAt) {
        return Http.instance().get(ApiUrl.MY_MESSAGE).
                param("page", page).
                param("lastUpdatedAt", lastUpdatedAt).
                contentDecoder(decoder).run();
    }

    public static IPromise changeAlreadyRead(List<String> ids) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            if (i < ids.size() - 1)
                buffer.append(ids.get(i) + ",");
            else
                buffer.append(ids.get(i));
        }
        return Http.instance().get(ApiUrl.ALREADY_READ_MESSAGE).
                param("from", buffer).contentDecoder(decoder).run();
    }

    public static IPromise deleteMessages(String ids) {
        return Http.instance().post(ApiUrl.DELETE_MESSAGE).
                param("_method", "DELETE").
                param("from", ids).contentDecoder(decoder).run();
    }

    public static IPromise deleteMessages(List<String> ids) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            if (i < ids.size() - 1)
                buffer.append(ids.get(i) + ",");
            else
                buffer.append(ids.get(i));
        }
        return Http.instance().post(ApiUrl.DELETE_MESSAGE).
                param("_method", "DELETE").
                param("from", buffer).contentDecoder(decoder).run();
    }
}