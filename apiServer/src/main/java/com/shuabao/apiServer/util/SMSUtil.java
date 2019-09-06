package com.shuabao.apiServer.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.VerificationCodeEntity;
import com.shuabao.core.manager.HttpRequestManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by Scott Wei on 4/15/2018.
 */
public class SMSUtil {
    public static final String apikey = "f7fea851a097c4c2cc2f6d7f25c5bd62";
    public static final String nationURL = "https://sms.yunpian.com/v2/sms/single_send.json";
    public static final String overseaURL = "https://us.yunpian.com/v2/sms/single_send.json";
//    public static final String testURL = "https://test-api.yunpian.com";

    //校驗手機號碼
    public static boolean isValidNumber(VerificationCodeEntity entity) {
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber()
                .setCountryCode(NumberUtils.toInt(entity.getCountryCode()))
                .setNationalNumber(NumberUtils.toLong(entity.getPhone()));
        return PhoneNumberUtil.getInstance().isValidNumber(number);
    }

    // 6位随机数字验证码
    public static int getVerificationCode() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }

    // 发送短信
    public static boolean send(VerificationCodeEntity entity) throws Exception{
        ReturnCode code = null;
        if(entity.getType() == 1) {//注冊
            code = ReturnCode.REGREGISTER_CODE;
        }
        if(entity.getType() == 2) {//重置密碼
            code = ReturnCode.RESET_PASSWORD__CODE;
        }
        if(Objects.isNull(code)) {
            return false;
        }
        String url = nationURL;
        if(!"86".equals(entity.getCountryCode())) {//非國内手機
            entity.setPhone("+" + entity.getCountryCode() + entity.getPhone());
            url = overseaURL;
        }
        Map<String, Object> params = new HashMap();
        params.put("apikey", apikey);
        params.put("text", code.getMsg(entity.getLang()).replace("#code#",entity.getVerificationCode()));
        params.put("mobile", entity.getPhone());
        Optional<String> optional = HttpRequestManager.getSingleton().post(url, params);
        if(optional.isPresent()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(optional.get());
            if(jsonNode.get("code").asInt() == 0) {
                return true;
            }
        }
        return false;
    }



}
