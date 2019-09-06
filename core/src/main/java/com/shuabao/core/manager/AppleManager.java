package com.shuabao.core.manager;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by Scott Wei on 4/24/2018.
 */
public final class AppleManager {
    protected static final Logger log = LoggerFactory.getLogger(AppleManager.class);

    private final static String APPLE_PRODUCT = "https://buy.itunes.apple.com/verifyReceipt";
    private final static String APPLE_SANDBOX = "https://sandbox.itunes.apple.com/verifyReceipt";


    /**
     *验证交易凭据
     *状态码
     *21000 App Store无法读取您提供的JSON对象。
    21002 receipt-data属性中的数据格式错误或丢失。
    21003 收据无法验证。
    21004 您提供的共享密码与您帐户的文件共享密码不匹配。
    21005 收据服务器当前不可用。
    21006 此收据有效，但订阅已过期。当此状态代码返回到您的服务器时，收据数据也被解码并作为响应的一部分返回。仅适用于iOS 6风格的交易收据用于自动续订订阅。
    21007 此收据来自测试环境，但已发送到生产环境进行验证。将其发送到测试环境。
    21008 此收据来自生产环境，但已发送到测试环境进行验证。将其发送到生产环境。
    21010 此收据无法授权。对待这一点，就好像从未做过购买一样。
    21100-21199 内部数据访问错误。
     **/
    public static List<Map<String, Object>> verifyReceipt(String receiptMd5, String receipt, boolean sandbox){
        String url = sandbox ? APPLE_SANDBOX : APPLE_PRODUCT;
        Map<String, Object> params = new HashMap<>();
        params.put("receipt-data", receipt);
        Optional<String> optional = HttpRequestManager.getSingleton().post(url, params);
        List<Map<String, Object>> list = new ArrayList<>();
        if(optional.isPresent()){
            try {
                ObjectMapper mapper = new ObjectMapper();
                params = mapper.readValue(optional.get(), Map.class);
                Integer status = (Integer) params.get("status");
                if(status.intValue() == 0){
                    params = mapper.readValue(String.valueOf(params.get("receipt")), Map.class);
                    List<Object> array =  mapper.readValue(String.valueOf(params.get("in_app")), List.class);
                    for (Object object : array) {
                        params = mapper.readValue(String.valueOf(object), Map.class);
                        /*String date = params.get("purchase_date_ms").toString();
                        String product_id = params.get("product_id").toString();
                        String transaction_id = params.get("transaction_id").toString();*/
                        list.add(params);
                    }
                }else{
                    log.error("check receipt {} fail status:{}",receiptMd5,status);
                    return null;
                }
            } catch (Exception e) {
                log.error("check receipt {} fail response:{}",receiptMd5,optional.get());
                return null;
            }
        }else{
            log.error("check receipt {} fail request error",receiptMd5);
            return null;
        }
        return list;
    }
}
