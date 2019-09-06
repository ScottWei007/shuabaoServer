package com.shuabao.core.base;

/**
 * Created by Scott Wei on 4/7/2018.
 */

//返回碼
public enum ReturnCode {
    FAILURE("请求失败","請求失敗","failure", 0),
    SUCCESS("请求成功","請求成功","success", 1),
    ERROR("服务器错误","服務器錯誤","server error", 2),
    INVALID_PARAMETERS("参数不全","參數不全","invalid parameters",3),


    //手機驗證,注冊，登陸
    INVALID_PHONE("手机号码不合法","手機號碼不合法","invalid phone number",100),
    PHONE_REGREGISTERED("手机号码已经注册过","手機號碼已經注冊過","phone number has been registered",101),
    VERIFICATIONCODE_TOO_MUCH("获取验证码次数过多","獲取驗證碼次數過多","verification code too much",102),
    EXPIRE_VERIFICATIONCODE("验证码已过期","驗證碼已過期","verification code is expire",103),
    INVALID_VERIFICATIONCODE("验证码不正确","驗證碼不正確","verification code is wrong",104),
    DRIVER_BLACK_LIST("黑名单的设备","黑名單的設備","driver in black list",105),
    OPEN_PLAT_REGREGISTERED("该平台已经注册过","該平臺已經注冊過","this plat has been regregistered",106),
    INVALID_PASSWORD("密码不合法","密碼不合法","invalid password",107),
    PHONE_UNREGREGISTERED("手机号码沒有注册","手機號碼沒有注冊","phone number has not registered",108),
    CHECK_AUTH_FAILURE("第三方授权失败","第三方授權失敗","failed to check auth",109),
    USER_NOT_FOUND("用户不存在","用戶不存在","user not found",110),
    USER_BLACK_LIST("黑名单的用户","黑名單的用戶","user in black list",111),
    WRONG_PASSWORD("密码错误","密碼錯誤","wrong password",112),
    FAILED_SEND("发送短信验证码失败","發送短信驗證碼失敗","Failed to send text messages",113),


    //socket服務器業務
    USER_NOT_AUTH("用户未认证","用戶未認證","user have not auth",200),
    FAILED_AUTH("用户认证失败","用戶認證失敗","Failed to auth user",201),

    //訂單
    FAILED_ORDER("下订单失败","下訂單失敗","Failed to order",300),
    RECHARGE_NOT_FOUND("充值点不存在","充值點不存在","recharge not found",301),
    FAILED_PREORDER("第三方预下单失败","第三方預下單失敗","failed preorder",302),
    INVALID_RECEIPT("apple订单凭证无效","apple訂單憑證無效","invalid receipt",303),
    FAILED_RECEIPT_ADD("apple订单凭证插入失败","apple訂單憑證插入失敗","failed receipt add",304),
    ORDER_NOT_FOUND_OR_DONE("订单不存在或者已经处理","訂單不存在或者已經處理","order not found or done",305),
    FAILED_VERIFY_RECEIPT("苹果订单验证失败","蘋果訂單驗證失敗","failed verify receipt",306),
    FAILED_ORDER_UPDATE("订单更新失败","訂單更新失敗","failed order update",307),
    FAILED_RECEIPT_UPDATE("apple订单凭证更新失败","apple訂單憑證更新失敗","failed receipt update",308),

    //短信驗證模板
    REGREGISTER_CODE("【摩擦交友】验证码：#code#，此验证码仅用于注册摩擦交友平台；请在30分钟之内完成验证。","【摩擦交友】驗證碼：#code#，此驗證碼僅用於註冊摩擦交友平台；請在30分鐘之內完成驗證。","【摩擦交友】 Verification Code: #code#. This Verification Code is only used to register the 【摩擦交友】 platform. Please complete verification within 30 minutes.",900),
    RESET_PASSWORD__CODE("【摩擦交友】验证码：#code#，此验证码仅用于重置密码；请在30分钟之内完成验证。","【摩擦交友】驗證碼：#code#，此驗證碼僅用於重置密碼；請在30分鐘之內完成驗證。","【摩擦交友】 Verification Code: #code#.This verification code is only used to reset the password; please complete the verification within 30 minutes.",901);



    private String cn;//中文
    private String tw;//繁體
    private String en;//英語
    private int index;//代碼號
    ReturnCode(String cn, String tw, String en, int index) {
        this.cn = cn;
        this.tw = tw;
        this.en = en;
        this.index = index;
    }

    public String getMsg (int lang) {
        switch (lang) {
            case 1 :
                return cn;
            case 2 :
                return tw;
            case 3 :
                return en;
            default:
                return cn;
        }
    }

    public int getIndex () {
        return index;
    }

}
