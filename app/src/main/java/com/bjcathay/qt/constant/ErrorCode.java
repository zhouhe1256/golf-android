package com.bjcathay.qt.constant;

/**
 * -1 系统内部错误
 * 1 参数错误
 * 10000 用户未登陆
 * 10001 用户不存在
 * 10002 账号已被冻结
 * 10003 无效的api token
 * 10004 用户名密码不匹配
 * 10005 原密码错误
 * 10006 账号已被占用
 * 10007 手机号码格式不正确
 * 10008 验证码错误
 * 10009 邀请码不存在
 * 10010 验证码已发送，重新发送需要等待60秒
 * 11001 没有足够的名额完成该交易
 * 12001 赛事不存在
 * 12002 已参与过该赛事
 * 12003 赛事已下线
 * 12004 赛事名额已满
 * 13001 订单已经取消
 * 13002 订单状态错误
 * 13003 处理中的订单不能删除
 * 13004 订单未支付成功
 * 14001 兑换目标不存在
 * 14002 物品转让失败
 * 14003 兑换条件尚未满足
 * 14004 赠送用户已被冻结
 * 14005 参与条件尚未满足
 * 15001 反馈内容不能为空
 */
public enum ErrorCode {

    INTERNAL_ERROR(-1, "系统内部错误"),

    INVALID_ARGUMENT(1, "参数错误"),

    THIRD_PARTY_ERROR(2, "第三方调用失败"),

    USER_NOT_LOGIN(10000, "用户未登陆"),

    USER_NOT_EXISTS(10001, "用户不存在"),

    USER_FROZE(10002, "账号已被冻结"),

    INVALID_TOKEN(10003, "用户无效"),

    USERNAME_PASSWORD_NOT_MATCH(10004, "用户名密码不匹配"),

    BAD_OLD_PASSWORD(10005, "原密码错误"),

    USERNAME_EXISTS(10006, "账号已被占用"),

    SEND_CODE_FAILURE(10007, "手机号码格式不正确"),

    TEL_FORMAT_ERROR(10008, "验证码错误"),

    CHECK_CODE_ERROR(10009, "邀请码不存在"),

    CHECK_CODE_INVALID(10010, "验证码已发送，重新发送需要等待60秒"),

    INVITE_INFO_NOT_ERROR(10011, "邀请码不存在"),

    ALREADY_SENT_CODE(10012, "验证码已发送，重新发送需要等待60秒"),

    USER_INFO_DEFECTS(10013, "用户信息不完善"),

    STADIUM_NOT_ERROR(11001, "没有足够的名额完成该交易"),

    ACTIVITY_CANCEL(12001, "赛事不存在"),

    ACTIVITY_NOT_EXISTS(12002, "已参与过该赛事"),

    ACTIVITY_DUPLICATE_ACTION(12003, "赛事已下线"),

    ORDER_ALREADY_CANCEL(13001, "订单已经取消"),

    ORDER_STATUS_ERROR(13002, "订单状态错误"),

    ORDER_DELETE_ERROR(13003, "处理中的订单不能删除"),
    ORDER_PAY_ERROR(13004, "订单未支付成功"),

    PROP_DUPLICATE_EXCHANGE(14001, "兑换目标不存在"),

    PROP_NOT_EXISTS(14002, "物品转让失败"),

    PROP_GIVE_ERROR(14003, "兑换条件尚未满足"),

    PROP_CONDITION_NOT_MEET(14004, "赠送用户已被冻结"),

    GIVE_USER_FROZE(14005, "参与条件尚未满足"),

    ACTIVITY_CONDITION_NOT_MEET(14006, "参与条件尚未满足"),
    FEED_BACK(15001, "反馈内容不能为空");


    private int code;
    private String codeName;

    private ErrorCode(int code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }

    public static String getCodeName(int code) {
        for (ErrorCode error : ErrorCode.values()) {
            if (error.getCode() == code)
                return error.codeName;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
