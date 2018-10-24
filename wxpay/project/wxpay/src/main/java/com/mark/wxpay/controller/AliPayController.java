package com.mark.wxpay.controller;/**
 * Created by thinkpad on 2018/8/21.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.mark.wxpay.util.AliPayOnlineConstants.*;

/**
 * @author jianyu.liang
 * @create 2018-08-21 14:54
 **/
@RestController
public class AliPayController {

    @RequestMapping("/alipay")
    public void payInit(HttpServletResponse response) throws AlipayApiException, IOException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                APP_ID,
                APP_PRIVATE_KEY,
                "json",
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                "RSA2");
        //SDK已经封装掉了公共参数，这里只需要传入业务参数
        //此次只是参数展示，未进行字符串转义，实际情况下请转义
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 超时时间 可空
        String timeout_express="2m";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        String id = UUID.randomUUID().toString().replace("-", "");
        System.out.println(id);
        model.setOutTradeNo(id);
        model.setSubject("活动报名费");
        model.setTotalAmount("88");
        //model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(PRODUCT_CODE);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(NOTIFY_URL);
        // 设置同步地址
        //alipay_request.setReturnUrl(RETURN_URL);
        /*//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
        AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();

        JSONObject json = new JSONObject();
        json.put("out_trade_no", UUID.randomUUID().toString().replace("-", ""));
        json.put("total_amount", "88.88");
        json.put("subject", "iphone8");
        json.put("seller_id", MTH_ID);
        json.put("product_code", PRODUCT_CODE);
        System.out.println(json);
        request.setBizContent(json.toString());
        AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);*/
        String  form = alipayClient.pageExecute(alipay_request).getBody();
       /* System.out.println(response.getBody());
        //调用成功，则处理业务逻辑
        if(response.isSuccess()){
            responseS.setContentType("text/html;charset="+CHARSET);
            responseS.getWriter().write(response.getBody());
            responseS.getWriter().flush();
        }*/
        response.setContentType("text/html;charset=" + CHARSET_UTF8);
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping("/notifyAlipayURL")
    public void notifyAlipayURL(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        String payID = params.get("out_trade_no");
        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, params.get("sign_type"));
        System.out.println(flag);
    }


    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }
}
