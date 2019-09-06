package com.shuabao.core.manager;


import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.PaymentEntity;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.util.MD5Encode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.*;


/**
 * Created by Scott Wei on 4/24/2018.
 */
public final class WechatManager {
    protected static final Logger log = LoggerFactory.getLogger(WechatManager.class);


    private static final String APP_ID = EnvironmentManager.getEnvironment().getProperty("wechat.app.id");
    private static final String MCH_ID = EnvironmentManager.getEnvironment().getProperty("wechat.mch.id");
    private static final String WECHAT_PUBLIC_KEY = EnvironmentManager.getEnvironment().getProperty("wechat.app.publickey");
    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //服務器回調url
    private static final String NOTIFYURL = EnvironmentManager.getEnvironment().getProperty("wechat.app.notifyurl");


    public static Map<String, Object> prepareOrder(PaymentEntity entity) throws ShuabaoException{
        if(Objects.isNull(entity)){
            return null;
        }
        Map<String, Object> params = new TreeMap<>();
        params.put("appid", APP_ID);
        params.put("mch_id",MCH_ID);
        params.put("nonce_str", RandomStringUtils.randomAlphabetic(15));
        params.put("body", entity.getUid());
        params.put("out_trade_no", entity.getOid());
        params.put("total_fee", entity.getAmount());
        params.put("spbill_create_ip",entity.getIp());
        params.put("notify_url", NOTIFYURL);
        params.put("trade_type", entity.getPayment());
        if(entity.getPayment() == 2) {//h5网页公众号支付 必传
           // params.put("openid", entity.getExtend());
        }
        if(entity.getPayment() == 3) {//扫码支付 必传
            params.put("product_id", "scan" + entity.getAmount());
        }
        params.put("sign", createSign(params));
        String result = httpsRequest(getRequestXml(params));
        Map<String,Object> resultmap = doXMLParse(result);
        if(Objects.isNull(resultmap)){
            String return_code = String.valueOf(resultmap.get("return_code"));//请求成功码
            String result_code = String.valueOf(resultmap.get("result_code"));//业务结果
            if("SUCCESS".equals(return_code)&&"SUCCESS".equals(result_code)){
                //统一参数
                Map<String,Object> wxMap;
                String prepay_id = String.valueOf(resultmap.get("prepay_id"));//得到微信预支付订单
                String trade_type = String.valueOf(resultmap.get("trade_type"));//交易类型，取值为：JSAPI，NATIVE，APP等
                if("JSAPI".equals(trade_type)){//公众号支付
                    wxMap = getH5PayInfo( prepay_id);
                }else if("NATIVE".equals(trade_type)){// 扫码支付
                    String code_url = String.valueOf(resultmap.get("code_url"));//二维码链接
                    wxMap = getQRPayInfo(code_url);
                }else{
                    wxMap = getAppPayInfo(prepay_id);
                }
                return wxMap;
            }else{
                log.error("WechatManager prepareOrder error oid:{} return_msg：{}", entity.getOid(),String.valueOf( resultmap.get("return_msg")));
                throw new ShuabaoException(ReturnCode.FAILED_PREORDER);
            }
        }else {
            log.error("WechatManager prepareOrder error oid:{}", entity.getOid());
            throw new ShuabaoException(ReturnCode.FAILED_PREORDER);
        }
    }

    public static String createSign(Map<String,Object> params){
        StringBuilder sb = new StringBuilder();
        Set set = params.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WECHAT_PUBLIC_KEY);
        String sign = MD5Encode.getMD5Str(sb.toString()).toUpperCase();
        return sign;
    }

    public static String getRequestXml(Map<String,Object> params){
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Set set = params.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String) entry.getKey();
            String v = entry.getValue().toString();
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String httpsRequest(String outputStr) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        HttpsURLConnection conn = null;
        OutputStream outputStream = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(UNIFIED_ORDER_URL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (StringUtils.isNotEmpty(outputStr)) {
                outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
            }
            // 从输入流读取返回内容
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (ConnectException ce) {
            log.error("WechatManager 连接超时：{}", ce);
        } catch (Exception e) {
            log.error("WechatManager https请求异常：{}", e);
        }finally {
            // 释放资源
            try {
                if(!Objects.isNull(bufferedReader))
                    bufferedReader.close();
                if(!Objects.isNull(inputStreamReader))
                    inputStreamReader.close();
                if(!Objects.isNull(inputStream))
                    inputStream.close();
                if(!Objects.isNull(outputStream))
                    outputStream.close();
                if(!Objects.isNull(conn))
                    conn.disconnect();
            } catch (IOException e) {
                log.error("WechatManager 释放资源异常：{}", e);
            }
        }
        return null;
    }

    public static Map<String,Object> doXMLParse(String strxml) {
        if(StringUtils.isEmpty(strxml)) {
            return null;
        }
        try {
            Map<String,Object> m = new HashMap<String, Object>();
            strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
            InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List list = root.getChildren();
            Iterator it = list.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                List children = e.getChildren();
                if(children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = getChildrenText(children);
                }
                m.put(k, v);
            }
            //关闭流
            in.close();
            return new TreeMap<String,Object>(m);
        } catch (Exception e) {
            log.error("WechatManager doXMLParse异常：{}", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    public static Map<String,Object> getH5PayInfo(String prepay_id){
        Map<String,Object> wxMap = new TreeMap<>();
        wxMap.put("appId", APP_ID);
        wxMap.put("timeStamp", System.currentTimeMillis() / 1000);
        wxMap.put("nonceStr", RandomStringUtils.randomAlphabetic(15));
        wxMap.put("package", "prepay_id="+prepay_id);
        wxMap.put("signType", "MD5");
        wxMap.put("paySign", createSign(wxMap));
        return wxMap;
    }

    public static Map<String,Object> getQRPayInfo(String code_url){
        Map<String,Object> wxMap = new TreeMap<>();
        wxMap.put("qr", code_url);
        return wxMap;
    }

    public static Map<String,Object> getAppPayInfo(String prepay_id){
        Map<String,Object> wxMap = new TreeMap<>();
        String packinfo = "Sign=WXPay";
        wxMap.put("appid", APP_ID);
        wxMap.put("partnerid", MCH_ID);
        wxMap.put("prepayid", prepay_id);
        wxMap.put("package", packinfo);
        wxMap.put("noncestr", RandomStringUtils.randomAlphabetic(15));
        wxMap.put("timestamp", System.currentTimeMillis() / 1000);
        wxMap.put("sign", createSign(wxMap));
        wxMap.remove("package");
        wxMap.put("packinfo", packinfo);
        return wxMap;
    }
}
