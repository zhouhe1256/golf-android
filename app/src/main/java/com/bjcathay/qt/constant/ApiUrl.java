package com.bjcathay.qt.constant;

import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.util.SystemUtil;

/**
 * Created by dengt on 15-4-20.
 */
public class ApiUrl {
    public static final String VERSION = SystemUtil.getCurrentVersionName(GApplication.getInstance());
    public static final String HOST_URL = GApplication.getInstance().getResources().getString(R.string.host);
    public static final String IMG_HOST_URL = GApplication.getInstance().getResources().getString(R.string.img_host);
    //192.168.1.54  192.168.1.22:8080
    public static final String OS = SystemUtil.getVersion();

    public static final String HOME_BANNER = "/api/banner";//首页活动(GET /api/banner)
    public static final String STSDIUM_LIST = "/api/goods";//场馆列表(GET /api/golf_course)
    public static final String PRODUCT_LIST = "/api/products";//产品列表(GET /api/products)


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
    public static final String USER_CONTACT_LIST_SEARCH = "/api/user/list";//通过手机通讯录查询用户列表(POST /api/user/list
    public static final String USER_CONTACT_LIST_INVITE = "/api/user/invite";//    获取邀请用户列表(GET /api/user/invite)

    public static final String MY_EVENT = "/api/user/event";//我的赛事(GET /api/user/event)
    public static final String PROP_LIST = "/api/prop";//兑换列表(GET /api/prop)


    public static final String EVENT_LIST = "/api/event";//赛事列表(GET /api/event)
    public static final String MY_ORDERS = "/api/user/order";//我的订单(GET /api/user/order)

    public static final String COMMIT_ORDER = "/api/user/order";//提交订单(POST /api/user/order)
    public static final String ORDER_TO_PAY = "/api/user/order/pay";//订单支付 (GET /api/user/order/pay)

    public static final String ORDER_PAY_SUCESS = "/api/alipay_notify_url"; //订单支付成功回调URL(POST /api/alipay_notify_url)

    public static final String DELETE_MESSAGE = "/api/user/empty_message"; // 清空消息(DELETE /api/user/empty_message)
    public static final String ALREADY_READ_MESSAGE = "/api/user/message"; //消息状态改为已读(PUT /api/user/message)
    public static final String MY_MESSAGE = "/api/user/message"; //我的消息(GET /api/user/message)
    public static final String MY_PROPS = "/api/user/prop"; //我的兑换(GET /api/user/prop)
    public static final String DELETE_EVENTS = "/api/user/event"; //删除赛事(DELETE /api/user/event)
    public static final String FEED_BACK = "/api/user/feedback"; //用户反馈(POST /api/user/feedback)
    public static final String SHARE_APP = "/api/share/app"; //分享app(GET /api/share/app)
    public static final String SHARE_PRODUCTS = "/api/share/products"; //分享产品（只能是团购，特卖和限购）(GET /api/share/products)
    public static final String SHARE_COMPETITION = "/api/share/competitions"; //分享赛事 (GET /api/share/competitions)
    public static final String SHARE_ORDER = "/api/share/orders"; //分享订单 (GET /api/share/orders)
    public static final String SOFT_UPDATE = "/api/update"; //检查更新(GET /api/update)


    //产品详情(GET /api/products/:id)
    public static String productDetail(Long id) {
        return "/api/products/" + id;
    }

    //场馆详情(GET /api/golf_course/:id)
    public static String stadiumDetail(Long id) {
        return "/api/golf_courses/" + id;
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

    //取消订单(PUT /api/user/order/:id)
    public static String orderCancle(Long id) {
        return "/api/user/order/" + id;
    }

    //删除订单(DELETE /api/user/order)
    public static String orderDelete() {
        return "/api/user/order/";
    }

    //验证订单是否支付完成(GET /api/user/order/:id/verify)
    public static String orderVerify(Long id) {
        return "/api/user/order/" + id + "/verify";
    }

    //赠送道具(POST /api/prop/:id/give)
    public static String sendProp(Long id) {
        return "/api/prop/" + id + "/give";
    }

    //参加赛事(POST /api/user/event/:id)
    public static String attendEvent(Long id) {
        return "/api/user/event/" + id;
    }

}
