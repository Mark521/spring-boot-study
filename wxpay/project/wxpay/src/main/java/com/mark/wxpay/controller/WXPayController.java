package com.mark.wxpay.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mark.wxpay.model.UnifiedOrderRequest;
import com.mark.wxpay.model.UnifiedOrderRespose;
import com.mark.wxpay.model.UserOrderInfo;
import com.mark.wxpay.model.UserPayInfo;
import com.mark.wxpay.service.WXPayService;
import com.mark.wxpay.util.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/*import com.photovoltaic.model.ResultEnum;
import com.photovoltaic.model.UserOrderInfo;
import com.photovoltaic.model.UserPayInfo;
import com.photovoltaic.util.publicUtil;
import com.photovoltaic.weixin.model.TemplateData;
import com.photovoltaic.weixin.model.TimedTask;
import com.photovoltaic.weixin.model.UnifiedOrderRequest;
import com.photovoltaic.weixin.model.UnifiedOrderRespose;
import com.photovoltaic.weixin.model.WxTemplate;
import com.photovoltaic.weixin.util.WeixinUtil;*/

/**
 * 微信支付controller
 * 1.用户发起微信支付，初始化数据、调用统一下单接口。生成JSAPI页面调用的支付参数并签名（paySign,prepay_id,nonceStr,timestamp）
 * 2.js如果返回Ok，提示支付成功，实际支付结果已收到通知为主。
 * 3.在微信支付结果通知中，获取微信提供的最终用户支付结果信息，支付结果等信息更新用户支付记录中
 * 4.根据微信支付结果通知中的微信订单号调用查询接口，如果查询是已经支付成功，则发送支付成功模板信息给客户
 * @author hl
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class WXPayController{
    private static Logger log = LoggerFactory.getLogger(WXPayController.class);

    @Autowired
    private WXPayService wxPayService;
    /**
     * 获取终端IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request)  {
        String ip  =  request.getHeader( " x-forwarded-for " );
        if (ip == null || ip.length() == 0 || " unknown " .equalsIgnoreCase(ip))  {
            ip = request.getHeader( " Proxy-Client-IP " );
        }
        if (ip  == null || ip.length() == 0 || " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " WL-Proxy-Client-IP " );
        }
        if (ip  == null || ip.length() == 0 || " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getRemoteAddr();
        }
        return  ip;
    }
    /**
     * 支付初始化
     * @param payMoney
     * @return
     */
    @RequestMapping("/toPayInit")
    @ResponseBody
    public Map<String,Object> toPay(String payMoney, String userWeixinOpenId, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        String orderId = String.valueOf(WXPayUtil.generateUUID());
        String noncestr = WXPayUtil.generateNonceStr();
        Map<String,String> requestMap = new HashMap<String, String>();
        requestMap.put("appId", "wx3fda6310becfe801");
        requestMap.put("userWeixinOpenId",userWeixinOpenId);
        requestMap.put("out_trade_no",orderId);
        requestMap.put("mch_id", "商家号");
        requestMap.put("payMoney",payMoney);
        requestMap.put("spbill_create_ip", getIpAddr(request));
        requestMap.put("notify_url", "支付结果回调通知路径");
        requestMap.put("noncestr", noncestr);
        requestMap.put("body","一元联系");
        requestMap.put("detail","获取电站用户的联系方式");
        Map<String,Object> requestInfo = WXPayUtil.createOrderInfo(requestMap);
        String orderInfo_toString = (String) requestInfo.get("orderInfo_toString");
        //判断返回码
        UnifiedOrderRespose orderResponse = WXPayUtil.httpOrder(orderInfo_toString);// 调用统一下单接口
        //根据微信文档return_code 和result_code都为SUCCESS的时候才会返回code_url
        if(null!=orderResponse  && "SUCCESS".equals(orderResponse.getReturn_code()) && "SUCCESS".equals(orderResponse.getResult_code())){
            String timestamp = String.valueOf(WXPayUtil.getCurrentTimestamp());
            map.put("timestamp",timestamp);
            map.put("noncestr",noncestr);
            UnifiedOrderRequest unifiedOrderRequest = (UnifiedOrderRequest) requestInfo.get("unifiedOrderRequest");
            map.put("unifiedOrderRequest",unifiedOrderRequest);
            SortedMap<String, String> packageParams = new TreeMap<String, String>();
            packageParams.put("appId","你的appId");
            packageParams.put("signType","MD5");
            packageParams.put("nonceStr", noncestr);
            packageParams.put("timeStamp", timestamp);
            String packages = "prepay_id="+orderResponse.getPrepay_id();
            packageParams.put("package",packages);
            String sign = null;//这个梗，就是开头说的，弄了半天才弄出来的
            try {
                sign = WXPayUtil.generateSignature(packageParams,"你的密匙");
            } catch (Exception e) {
                map.put("result",-1);
                e.printStackTrace();
            }
            if(sign!=null && !"".equals(sign)){
                map.put("paySign",sign);
                map.put("result",1);
            }else{
                map.put("result",-1);
            }
            map.put("prepay_id",orderResponse.getPrepay_id());
            return map;
        }else{ //不成功
            String text = "调用微信支付出错，返回状态码："+orderResponse.getReturn_code()+"，返回信息："+orderResponse.getReturn_msg();
            if(orderResponse.getErr_code()!=null && !"".equals(orderResponse.getErr_code())){
                text = text +"，错误码："+orderResponse.getErr_code()+"，错误描述："+orderResponse.getErr_code_des();
            }
            log.error(text);
            map.put("result",-1);
            return map;
        }
    }

    /**
     * 异步回调接口
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/paymentNotice",produces="text/html;charset=utf-8")
    @ResponseBody
    public String WeixinParentNotifyPage(HttpServletRequest request,HttpServletResponse response) throws Exception{
        ServletInputStream instream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        int len = -1;
        byte[] buffer = new byte[1024];
        while((len = instream.read(buffer)) != -1){
            sb.append(new String(buffer,0,len));
        }
        instream.close();
//		log.error("支付通知回调信息："+sb.toString());
//		Map<String,String> map = WXPayUtil.doXMLParseWithSorted(sb.toString());//接受微信的通知参数
        Map<String,String> map = WXPayUtil.xmlToMap(sb.toString());//接受微信的回调的通知参数
        Map<String,String> return_data = new HashMap<String,String>();
        //判断签名是否正确
        if(WXPayUtil.isSignatureValid(map, "你的密匙")){
            if(map.get("return_code").toString().equals("FAIL")){
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", map.get("return_msg"));
            }else if(map.get("return_code").toString().equals("SUCCESS")){
                String result_code = map.get("result_code").toString();
                String out_trade_no = map.get("out_trade_no").toString();
                //获得你自己的订单详情
                UserPayInfo payInfo = wxPayService.getUserPayInfo(out_trade_no);
                if(payInfo == null){
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "订单不存在");
                    //return WeixinUtil.GetMapToXML(return_data);
                    return WXPayUtil.GetMapToXML(return_data);
                }else{
                    //2	已支付（不确定是否支付成功）3 支付完成  4 取消支付	 5支付失败
                    if(result_code.equals("SUCCESS")){//支付成功
                        //如果订单已经支付直接返回成功
                        if(payInfo.getPayStatus()==3){
                            return_data.put("return_code", "SUCCESS");
                            return_data.put("return_msg", "OK");
                            return WXPayUtil.GetMapToXML(return_data);
                        }else{
                            String sign = map.get("sign").toString();
                            String total_fee = map.get("total_fee").toString();//订单金额
                            if(!payInfo.getTotal_fee().toString().equals(total_fee)){//订单金额是否一致
                                return_data.put("return_code", "FAIL");
                                return_data.put("return_msg", "金额异常");
                            }else{
                                String time_end = map.get("time_end").toString();
                                String bank_type = map.get("bank_type").toString();
                                String settlement_total_fee = map.get("settlement_total_fee");
                                if(settlement_total_fee==null || "".equals(settlement_total_fee)){
                                    settlement_total_fee = "0";
                                }
                                payInfo.setSign(sign);
                                payInfo.setResult_code(result_code);
                                payInfo.setPayStatus(3);
                                payInfo.setTime_end(time_end);
                                payInfo.setSettlement_total_fee(settlement_total_fee);
                                payInfo.setBank_type(bank_type);
                                payInfo.setCoupon_fee("0");
                                int result = wxPayService.updatePayInfo(payInfo);
                                if(result<=0){
                                    return_data.put("return_code", "FAIL");
                                    return_data.put("return_msg", "更新订单失败");
                                    return WXPayUtil.GetMapToXML(return_data);
                                }else{
                                    UserOrderInfo orderInfo = new UserOrderInfo();
                                    orderInfo.setId(payInfo.getOrderId());
                                    orderInfo.setStatus(2);
                                    result = wxPayService.updateOrderInfo(orderInfo);
                                    if(result<=0){
                                        return_data.put("return_code", "FAIL");
                                        return_data.put("return_msg", "更新订单失败");
                                        return WXPayUtil.GetMapToXML(return_data);
                                    }else{
                                        return_data.put("return_code", "SUCCESS");
                                        return_data.put("return_msg", "OK");
                                        return WXPayUtil.GetMapToXML(return_data);
                                    }
                                }
                            }
                        }
                    }else{//支付失败，更新支付结果
                        if(payInfo!=null){
                            payInfo.setResult_code(result_code);
                            payInfo.setPayStatus(5);
                            payInfo.setErr_code(map.get("err_code").toString());
                            payInfo.setErr_code_des(map.get("err_code_des").toString());
                            wxPayService.updatePayInfo(payInfo);
                        }
                        return_data.put("return_code", "FAIL");
                        return_data.put("return_msg",map.get("return_msg").toString());
                        return WXPayUtil.GetMapToXML(return_data);
                    }
                }
            }
        }else{
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "签名错误");
        }
        String xml = WXPayUtil.GetMapToXML(return_data);
        log.error("支付通知回调结果："+xml);
        return xml;
    }
}

