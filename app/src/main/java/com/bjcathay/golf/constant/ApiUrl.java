package com.bjcathay.golf.constant;

import com.bjcathay.golf.application.GApplication;
import com.bjcathay.golf.util.SystemUtil;

/**
 * Created by dengt on 15-4-20.
 */
public class ApiUrl {
    public static final String VERSION = SystemUtil.getCurrentVersionName(GApplication.getInstance());
    public static final String HOST_URL = "http://192.168.1.22:8080/";
    public static final String OS = SystemUtil.getVersion();

    public static final String HOME_BANNER = "/api/banner";//首页活动(GET /api/banner)
    public static final String STSDIUM_LIST = "/api/stadium";//场馆列表(GET /api/stadium)


    public static final String REGISTER = "/api/user/register";//注册用户(POST /api/user/register)
    public static final String USER_LOGIN = "/api/user/login";//登录(POST /api/login)
    public static final String USER_INFO = "/api/user";//获取当前用户信息(GET /api/user)
    public static final String SEND_CHECK_CODE = "/api/user/send_check_code";//发送验证码(POST /api/user/send_check_code)
    public static final String VERIFY_CHECK_CODE = "/api/user/verify_check_code";//检查验证码是否正确(POST /api/user/verify_check_code)
    public static final String CHANGE_PASSWORD = "/api/user/change_password";//修改密码(PUT /api/user/change_password)
    public static final String RESET_PASSWORD = "/api/user/reset_password";//修改密码(PUT /api/user/reset_password)
    public static final String CHANGE_USER_INFO = "/api/user";//更新用户信息(PUT /api/user)
    public static final String SET_AVATAR = "/api/user/set_avatar";//设置用户头像(POST /api/user/set_avatar)
    public static final String USER_SEARCH = "/api/user/search";//根据用户手机号搜索用户(POST /api/user/search)
    public static final String MY_EVENT = "/api/user/event";//我的赛事(GET /api/user/event)
    public static final String PROP_LIST = "/api/prop";//兑换列表(GET /api/prop)


    public static final String EVENT_LIST = "/api/event";//赛事列表(GET /api/event)


    //场馆详情(GET /api/stadium/:id)
    public static String stadiumDetail(Long id) {
        return "/api/stadium/" + id;
    }

    //赛事详情(GET /api/event/:id)
    public static String eventDetail(Long id) {
        return "/api/event/" + id;
    }

    //兑换道具(POST /api/prop/:id)
    public static String propDetail(Long id) {
        return "/api/prop/" + id;
    }


}
