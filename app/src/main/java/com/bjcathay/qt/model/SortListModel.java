package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.util.List;

/**
 * Created by bjcathay on 15-5-21.
 */
public class SortListModel {
    @JSONCollection(type = SortModel.class)
    private List<SortModel> users;
    private static IContentDecoder<SortListModel> decoder = new IContentDecoder.BeanDecoder<SortListModel>(SortListModel.class);

    public List<SortModel> getUsers() {
        return users;
    }

    public void setUsers(List<SortModel> users) {
        this.users = users;
    }

    //通过手机通讯录查询用户列表(POST /api/user/list)
    public static IPromise searchContactListUser(String data) {
        return Http.instance().post(ApiUrl.USER_CONTACT_LIST_SEARCH).
                param("data", data).contentDecoder(decoder).run();
    }
}
