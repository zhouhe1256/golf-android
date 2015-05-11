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
    public static final String STSDIUM_LIST = "/api/golf_course";//场馆列表(GET /api/golf_course)


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
    public static final String MY_ORDERS = "/api/user/order";//我的订单(GET /api/user/order)

    public static final String COMMIT_ORDER = "/api/order";//提交订单(POST /api/order)
    public static final String ORDER_PAY_SUCESS = "/api/alipay_notify_url"; //订单支付成功回调URL(POST /api/alipay_notify_url)

    public static final String DELETE_MESSAGE = "/api/user/message"; //删除消息(DELETE /api/user/message)
    public static final String ALREADY_READ_MESSAGE = "/api/user/message"; //消息状态改为已读(PUT /api/user/message)
    public static final String MY_MESSAGE = "/api/user/message"; //我的消息(GET /api/user/message)
    public static final String MY_PROPS = "/api/user/prop"; //我的兑换(GET /api/user/prop)
    public static final String DELETE_EVENTS = "/api/user/event"; //删除赛事(DELETE /api/user/event)
    //public static final String DELETE_EVENTS = "/api/user/event"; //我的赛事(GET /api/user/event)


    //场馆详情(GET /api/golf_course/:id)
    public static String stadiumDetail(Long id) {
        return "/api/golf_course/" + id;
    }

    //赛事详情(GET /api/event/:id)
    public static String eventDetail(Long id) {
        return "/api/event/" + id;
    }

    //兑换道具(POST /api/prop/:id)
    public static String propDetail(Long id) {
        return "/api/prop/" + id;
    }

    //订单详情(GET /api/order/:id)
    public static String orderDetail(Long id) {
        return "/api/order/" + id;
    }

    //取消订单(PUT /api/order/:id)
    public static String orderCancle(Long id) {
        return "/api/order/" + id;
    }

    //删除订单(DELETE /api/user/order)
    public static String orderDelete(Long id) {
        return "/api/user/order" + id;
    }

    //验证订单是否支付完成(GET /api/user/order/:id/verify)
    public static String orderVerify(Long id) {
        return "/api/user/order/" + id + "/verify";
    }

    //赠送道具(POST /api/prop/:id/gift)
    public static String sendProp(Long id) {
        return "/api/prop/" + id + "/gift";
    }

    //参加赛事(POST /api/user/event/:id)
    public static String attendEvent(Long id) {
        return "/api/user/event/" + id;
    }

}
