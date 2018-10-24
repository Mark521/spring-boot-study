package com.mark.wxpay.util;

/**
 * 常量
 */
public class WXPayConstants {

    public enum SignType {
        MD5, HMACSHA256
    }
    public static final String FAIL     = "FAIL";
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";
    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    //微信认证相关信息
    public static String APIID = "wxe11e4ce3d2084297";//appID
    public static String APPSECRET = "3d0ceff9ee5a0a20229e2af539da70dd";//appsecret
    public static String token = "markliang";//公众号认证TOKEN

    //微信支付相关信息
    public static String MCH_ID = "";//商铺信息
    public static String API_KEY = "";//API密钥

    //回调地址
    public static String AUTH_REDIRECT_URI="http://3b31e84e.ngrok.io/OnlineNegotiation/account/oauthGetUserInfo.go";//认证结果回调跳转地址
    public static String PAY_NOTIFY_URL = "";//支付结果回调通知路径

}

