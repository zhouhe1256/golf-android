package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.util.List;

/**
 * Created by bjcathay on 15-5-25.
 */
public class InviteListModel {
    @JSONCollection(type = InviteModel.class)
    private List<InviteModel> users;

    public List<InviteModel> getUsers() {
        return users;
    }

    public void setUsers(List<InviteModel> users) {
        this.users = users;
    }

    private static IContentDecoder<InviteListModel> decoder = new IContentDecoder.BeanDecoder<InviteListModel>(InviteListModel.class);

    //获取邀请用户列表(GET /api/user/invite)
    public static IPromise getInvite(int page) {
        return Http.instance().get(ApiUrl.USER_CONTACT_LIST_INVITE).
                contentDecoder(decoder).run();
    }
}
