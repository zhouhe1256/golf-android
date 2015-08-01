
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-4-30.
 */
public class UserModel implements Serializable {
    private Long id;
    private String nickname;
    private String mobileNumber;
    private String imageUrl;
    private String apiToken;
    private String inviteCode;
    private String differCount;
    private String realName;
    private String address;
    private double longitude;
    private double latitude;
    private int inviteAmount;
    private Long inviteUserId;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(Long inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getInviteAmount() {
        return inviteAmount;
    }

    public void setInviteAmount(int inviteAmount) {
        this.inviteAmount = inviteAmount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getDifferCount() {
        return differCount;
    }

    public void setDifferCount(String differCount) {
        this.differCount = differCount;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private static IContentDecoder<UserModel> decoder = new IContentDecoder.BeanDecoder<UserModel>(
            UserModel.class, "user");

    public static IPromise register(String mobileNo, String password, String code, String inviteCode) {
        return Http.instance().post(ApiUrl.REGISTER).
                param("name", mobileNo).param("password", password)
                .param("code", code)
                .param("inviteCode", inviteCode).run();
    }

    public static IPromise verifyCheckCode(String phone, String code, String type) {
        return Http.instance().post(ApiUrl.VERIFY_CHECK_CODE).
                param("phone", phone).param("code", code).param("type", type).run();
    }

    public static IPromise sendCheckCode(String phone, String type) {
        return Http.instance().post(ApiUrl.SEND_CHECK_CODE).
                param("phone", phone).param("type", type).run();
    }

    public static IPromise login(String user, String password) {
        return Http.instance().post(ApiUrl.USER_LOGIN).
                param("name", user).param("password", password).run();
    }

    public static IPromise get() {
        return Http.instance().get(ApiUrl.USER_INFO).
                contentDecoder(decoder).run();
    }

    // 修改密码
    public static IPromise changePassword(String oldPassword, String newPassword) {
        return Http.instance().put(ApiUrl.CHANGE_PASSWORD).
                param("oldPassword", oldPassword).param("password", newPassword).run();
    }

    // 重置密码
    public static IPromise resetPassword(String mobileNumber, String password, String code) {
        return Http.instance().put(ApiUrl.RESET_PASSWORD).
                param("password", password).param("mobileNumber", mobileNumber).param("code", code)
                .run();
    }

    // 更新用户信息
    public static IPromise updateUserInfo(String nickname, String realName
            , String pushClientId, String longitude, String latitude) {
        return Http.instance().put(ApiUrl.CHANGE_USER_INFO).
                param("nickname", nickname).param("realName", realName).
                param("pushClientId", pushClientId)
                .param("longitude", longitude).
                param("latitude", latitude).contentDecoder(decoder).run();
    }

    // 更新用户邀请信息
    public static IPromise updateUserInfo(String inviteCode) {
        return Http.instance().put(ApiUrl.CHANGE_USER_INFO).
                param("inviteCode", inviteCode).run();
    }

    // 设置用户头像
    public static IPromise setAvatar(byte[] data) {
        return Http.instance().post(ApiUrl.SET_AVATAR).
                data(data).contentDecoder(decoder).run();
    }

    // 用户反馈(POST /api/user/feedback)
    public static IPromise feedBack(String contactWay, String content) {
        return Http.instance().post(ApiUrl.FEED_BACK).
                param("contactWay", contactWay).param("content", content).run();
    }
}
