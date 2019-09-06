package com.shuabao.apiServer.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.util.ObfuseTableBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * Created by Scott Wei on 4/7/2018.
 */

public class BaseAction {
    //日志
    protected final Logger log = LoggerFactory.getLogger(BaseAction.class);

    //響應碼key
    public static final String CODE = "code";
    //響應信息key
    public static final String MSG = "msg";
    //结果信息key
    public static final String DATA = "data";
    //路徑地址key
    public static final String BASEPATH = "basePath";

    //返回是否加密json
    public String toJson(Map<String,Object> returnDataMap, boolean isEncode) {
        String json= "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(returnDataMap);
            if(isEncode) {
                json = ObfuseTableBase64.encode(json);
            }
        }catch (Exception e) {
            log.error("解析json出錯：" + e);
        }
        return json;
    }

    //返回頁面
    public ModelAndView render(Map<String,Object> returnDataMap, String viewName, HttpServletRequest request) {
        returnDataMap.put(BASEPATH, getBasePath(request));
        return new ModelAndView(viewName, returnDataMap);
    }


    //獲取當前用戶ip
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() > 0 && ip.indexOf(",") > -1) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    //返回scheme
    public static String getScheme(HttpServletRequest request){
        String scheme = request.getHeader("X-Forwarded-Proto");
        if(scheme == null || "".equals(scheme) || "unknown".equalsIgnoreCase(scheme))
            scheme = request.getScheme();
        return scheme;
    }

    //返回路徑
    public String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = getScheme(request) + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        return basePath;
    }


    //設置返回信息碼
    protected void setReturnInfo(Map<String,Object> returnDataMap, ReturnCode code, int lang) {
        returnDataMap.put(CODE, code.getIndex());
        returnDataMap.put(MSG, code.getMsg(lang));
    }

    //設置返回结果
    protected void setData(Map<String,Object> returnDataMap, Object obj) {
        returnDataMap.put(DATA, obj);
    }

}
