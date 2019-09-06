package com.shuabao.core.manager;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.PaymentEntity;
import com.shuabao.core.exception.ShuabaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 4/24/2018.
 */
public class AlipayManager {
    protected final Logger log = LoggerFactory.getLogger(AlipayManager.class);
    //线程安全的单例
    private static volatile AlipayManager singleton;
    private final static Lock lock = new ReentrantLock();

    public static AlipayManager getSingleton () {
        if (singleton == null) {
            try {
                lock.lock();
                if(singleton == null) {
                    singleton = new AlipayManager();
                }
            }finally {
                lock.unlock();
            }
        }
        return singleton;
    }

    private AlipayManager() {
        //初始化client
        this.alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
    }

    private final String APP_ID = EnvironmentManager.getEnvironment().getProperty("alipay.app.id");
    private final String APP_PRIVATE_KEY = EnvironmentManager.getEnvironment().getProperty("alipay.app.privatekey");
    private final String ALIPAY_PUBLIC_KEY = EnvironmentManager.getEnvironment().getProperty("alipay.app.publickey");
    private final AlipayClient alipayClient;
    //服務器回調url
    private final String NOTIFYURL = EnvironmentManager.getEnvironment().getProperty("alipay.app.notifyurl");
    //H5支付
    private final String ALIPAY_H5_CONTENT = "{\"out_trade_no\":\"%s\",\"total_amount\":\"%s\",\"subject\":\"%s\",\"product_code\":\"QUICK_WAP_PAY\"}";
    //扫码支付
    private final String ALIPAY_QR_CONTENT = "{\"out_trade_no\":\"%s\",\"seller_id\":\"%s\",\"total_amount\":\"%s\",\"subject\":\"%s\",\"timeout_express\":\"30m\"}";
    //扫码支付 PID
    private final String ALIPAY_QR_SELLER_ID = ""; //"2088621779144087";


    //app客戶端支付
    public String appPay(PaymentEntity entity) throws ShuabaoException{
        if(Objects.isNull(entity)){
            return null;
        }
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(String.valueOf(entity.getUid()));
        model.setSubject(entity.getUid() + "用戶充值");
        model.setOutTradeNo(String.valueOf(entity.getOid()));
        model.setTimeoutExpress("30m");
        model.setTotalAmount(String.valueOf(entity.getAmount()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(NOTIFYURL);//商户外网可以访问的异步地址
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();//就是orderString 可以直接给客户端请求，无需再做处理。
        }catch (Exception e) {
            log.error(">>>>>>>>>AlipayManager.appPay異常：>>>>>>>>>uid:{},cause:{}",entity.getUid() , e.getMessage());
            throw new ShuabaoException(ReturnCode.FAILED_PREORDER);
        }
    }


    //H5客戶端支付
    public String h5Pay(PaymentEntity entity) throws ShuabaoException{
        if(Objects.isNull(entity)){
            return null;
        }
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        ////////////////////////////////////////////////


//        request.setReturnUrl(info.getExtend());
        //回调通知地址
        request.setNotifyUrl(NOTIFYURL);
        // 订单号,订单总金额,订单标题
        String bizContent = String.format(ALIPAY_H5_CONTENT, entity.getOid(), entity.getAmount(), entity.getUid() + "用戶充值");
        request.setBizContent(bizContent);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeWapPayResponse response= alipayClient.pageExecute(request);
            return response.getBody();//就是orderString 可以直接给客户端请求，无需再做处理。
        }catch (Exception e) {
            log.error(">>>>>>>>>AlipayManager.h5Pay異常：>>>>>>>>>uid:{},cause:{}",entity.getUid() , e.getMessage());
            throw new ShuabaoException(ReturnCode.FAILED_PREORDER);
        }
    }

    //掃碼支付
    public String qrPay(PaymentEntity entity) throws ShuabaoException{
        if(Objects.isNull(entity)){
            return null;
        }
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(NOTIFYURL);
        // 订单号,订单总金额,订单标题,商户号
        String bizContent = String.format(ALIPAY_QR_CONTENT, entity.getOid(), entity.getAmount(), entity.getUid() + "用戶充值", ALIPAY_QR_SELLER_ID);
        request.setBizContent(bizContent);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            return response.getBody();//就是orderString 可以直接给客户端请求，无需再做处理。
        }catch (Exception e) {
            log.error(">>>>>>>>>AlipayManager.qrPay異常：>>>>>>>>>uid:{},cause:{}",entity.getUid() , e.getMessage());
            throw new ShuabaoException(ReturnCode.FAILED_PREORDER);
        }
    }

}
