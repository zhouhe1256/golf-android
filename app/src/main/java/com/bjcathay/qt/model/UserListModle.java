
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-4-30.
 */
public class UserListModle implements Serializable {

    @JSONCollection(type = UserModel.class)
    private List<UserModel> users;

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    private static IContentDecoder<UserListModle> decoder = new IContentDecoder.BeanDecoder<UserListModle>(
            UserListModle.class);

    // 根据用户手机号搜索用户
    public static IPromise searchUsers(List<String> phones) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < phones.size(); i++) {
            if (i < phones.size() - 1)
                buffer.append(phones.get(i) + ",");
            else
                buffer.append(phones.get(i));
        }
        return Http.instance().post(ApiUrl.USER_SEARCH).
                param("phones", buffer).contentDecoder(decoder).run();
    }

    // 根据用户手机号搜索用户
    public static IPromise searchUser(String phones) {
        return Http.instance().post(ApiUrl.USER_SEARCH).
                param("phones", phones).contentDecoder(decoder).run();
    }

    // 通过手机通讯录查询用户列表(POST /api/user/list)
    public static IPromise searchContactListUser(String data) {
        return Http.instance().post(ApiUrl.USER_CONTACT_LIST_SEARCH).
                param("data", data).contentDecoder(decoder).run();
    }
}
