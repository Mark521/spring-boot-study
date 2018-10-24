package com.mark.wxpay.util;/**
 * Created by thinkpad on 2018/8/21.
 */

/**
 * @author jianyu.liang
 * @create 2018-08-21 14:57
 **/
public class AliPayOnlineConstants {

        public static final String SIGN_TYPE = "sign_type";
        public static final String SIGN_TYPE_RSA = "RSA";
        public static final String SIGN_TYPE_RSA2 = "RSA2";
        public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
        public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
        public static final String ENCRYPT_TYPE_AES = "AES";
        public static final String FORMAT = "format";
        public static final String METHOD = "method";
        public static final String TIMESTAMP = "timestamp";
        public static final String VERSION = "version";
        public static final String SIGN = "sign";
        public static final String ALIPAY_SDK = "alipay_sdk";
        public static final String ACCESS_TOKEN = "auth_token";
        public static final String APP_AUTH_TOKEN = "app_auth_token";
        public static final String TERMINAL_TYPE = "terminal_type";
        public static final String TERMINAL_INFO = "terminal_info";
        public static final String CHARSET = "utf-8";
        public static final String NOTIFY_URL = "https://cf2b4f84.ngrok.io/notifyAlipayURL";
        public static final String RETURN_URL = "https://cf2b4f84.ngrok.io/notifyAlipayURL";
        public static final String ENCRYPT_TYPE = "encrypt_type";
        public static final String BIZ_CONTENT_KEY = "biz_content";
        public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_TIMEZONE = "GMT+8";
        public static final String CHARSET_UTF8 = "UTF-8";
        public static final String CHARSET_GBK = "GBK";
        public static final String FORMAT_JSON = "json";
        public static final String FORMAT_XML = "xml";
        public static final String SDK_VERSION = "alipay-sdk-java-3.3.49.ALL";
        public static final String PROD_CODE = "prod_code";
        public static final String ERROR_RESPONSE = "error_response";
        public static final String RESPONSE_SUFFIX = "_response";
        public static final String RESPONSE_XML_ENCRYPT_NODE_NAME = "response_encrypted";


        public static final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCbpUbawJEm+f5NFN0g5QEeoN+0FppulkWL0ftg9P5AUqIUx7eCG151zgJwSDMmgg80pYQ15kOW87OnV4zjL7lv3+unYrwGagYPFqJ7+g6kp/DvsUo5tbfLWcymqAFt9m21BObIXrLKDSSxD2LX/xJvTfgDGTxsEpSP6GC4etIG4Q8RsAcNEOHdiChpdeOpxj50X24VsHS+fRZuqPHrDjrwM1FY1mjcYKiTQGiT2bAZCEdECiQwnUwbE6D1wNZsD/SIyOTMMjxv5arpSbsp/71hbCPCPnrBtgIJhNbqyz0ahyVWUItgopcICGjPdXtWuFnhGJqaZavcsGL0I+/Niz3rAgMBAAECggEANYkd2/RMeQmYDM9WWBAs2GohSAJWUB9kAHnTa0AZi+pgUaF4X0omrYKUmCZdYatuq38bAKcZLHzamH6muSYbNH3iwopVfidNujbg4J/cFYqmduuWSk9Uzp8uo0w6YaZXieILt3PLL3Bh5H0UVJMpthstaXNuswWZD/6v9gRY5YBLrMSk8PWbH6ichoVD4fowTfcvsxntzQE5AioXzVAr2cLinuipq2BFJt3b51UkiFyyOjdIwEDAE2XVWCaLu9IOWn9o+S2rrIMo56Xw9++AcVtdZ3PjJ96f78TzlQaE4+quOG+V9Jbi2hJocLpt5vRNiymq1+K+PV9h2BlayiHtgQKBgQD13VwzRW0twXsgeeq4HBsdZj+M4iT27HLnMaD2rHUOkmYtineJzRUQ9NUEoXLslUbp9Y3x+BuYdLX/eWIYIhZVt2Tlk0pA5AbVLmPOGMbrX1sfs12T6PstiuEueiYw7vRbto9eMlubeCo7Y+mxuIekD2B3cge7gia/QMnvSUWlIQKBgQCiD9LjUfTVgZq8fhIx/WTIhH0TWUaRzCU8WdGm5QSX5Cn8pqdGOPOvtF8jp7VrwbPvLs5LEv18F4BCi4VhNM8Nz5OE1k0Rtlt39gLJlzloFjzpaANT9p9wsEe48+ckzxztbhVHY8eot1oMljEExR5IwbcpHRVM/9NaQIuwNXL1iwKBgQDbVH3Lv0OGbfPJJPmfBHOr5msK4zLWlFhspnlRlMdYGIHOXpgE9k60sbTWTLCZHRIF5GicUxlLB9LT9RTtMnt6PABmTN8QxFrUEm42jSPnFUnVHc19BXoNRrdbHtyzCngy2MlM3t+Z8bi7AoaXZGaclJsZ8z+ypoiqVMHQFS1sAQKBgDuO3yzlkLnqYIiHOH6gpio+7oxu0c2BHeoZBEB2iAKeOoP8ZxsakQvwmMpMtxK75DmxlOToPLofFObfyYvY0YM81vUJoNcyklj60zls1w8WpRoOPLLdvui2EBW7pSXU68eiHjXXSKgLgg+AHtGm86M/deWpJ4vdtLnFYNDIQ9BTAoGBAK4HM68mQg/gY/sEZpXjUY6xMHeN6luP0/NUOKlcceWzvQB/TRYRYchUDbvIrX+SxKS+A65CA0CFwY5sFs4QnTQVqtrevgx/+Mp4pxqBqmi+d9IA3Kw2+wdfhq01/d/gwHEpmxrHZu7ITFmYsxS+847mL7170imDb310St7rD02T";
        public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyQxvnnDuamXPPszFqupt9D7s3Bk7qFi2DZ7DEdW9J2RvNSE8rbwk8SMVa96ldJVp+BakTdITAZwTKbs55qOrAGhG03rkjHVeoXS75VMe8hYNoS1etOEjcGl+MygVZsTcKJfLdsVT75jGsFP/KDm0321ea5F43um4XA8AdVM3+Cg/2GsIQXgcjbG3UIGuNjVSDFdCixhcZfNGdAMO2LVDD5zpoKuUONWKdmId86flgVrG5EI92fXsewbA5FprHQ/11W7Uor/5iJrNxowU+27h+ES+Y8EEfVsPLrsEJUHa1bhEBWNphVUjipFlpXGPY256cNehq0ALcK8LVM3znUneQQIDAQAB";
        public static final String MTH_ID = "2088102176102174";
        public static final String APP_ID = "2016091700535719";
        public static final String PRODUCT_CODE = "QUICK_WAP_WAY";
        public AliPayOnlineConstants() {
        }
}
