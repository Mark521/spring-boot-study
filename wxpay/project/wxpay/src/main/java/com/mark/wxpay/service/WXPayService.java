package com.mark.wxpay.service;

import com.mark.wxpay.model.UserOrderInfo;
import com.mark.wxpay.model.UserPayInfo;
import org.springframework.stereotype.Service;

@Service
public class WXPayService {

    public Integer updatePayInfo(UserPayInfo payInfo){
        return 1;
    }

    public UserPayInfo getUserPayInfo(String out_trade_no){
        UserPayInfo info = new UserPayInfo();
        return info;
    }

    public Integer updateOrderInfo(UserOrderInfo orderInfo){
        return 1;
    }


}
