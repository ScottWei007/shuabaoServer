package com.shuabao.apiServer.init;

import com.shuabao.apiServer.manager.UserSessionManager;
import com.shuabao.core.entity.UserSession;
import com.shuabao.core.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Created by Scott Wei on 4/8/2018.
 */

//參數攔截器
@Component
public class ParamsInterceptor implements HandlerInterceptor {

    private static final String CHECK_PARAMS = "{\"msg\":\"拦截器参数校验未通过\",\"code\":\"8\"}";
    private static final String AUTH_TOKEN = "{\"msg\":\"拦截器token校验未登录\",\"code\":\"9\"}";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //校驗參數，具體的校驗後面根據實際情況而定
        String lang = request.getParameter("lang");
        String uid = request.getParameter("uid");
        String token = request.getParameter("token");

        if(StringUtils.isAnyEmpty(lang, uid, token)) {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(CHECK_PARAMS);
            printWriter.flush();
            printWriter.close();
            return false;
        }

        //先查询本地缓存
        if(token.equals(UserSessionManager.getInstance().getUserToken(NumberUtils.toInt(uid)))){
            return true;
        }

        UserSession userSession = RedisUtil.getUserSession(uid, null);
        if(Objects.nonNull(userSession) && token.equals(userSession.getToken())) {
            //设置本地缓存
            UserSessionManager.getInstance().addUserToken(NumberUtils.toInt(uid), userSession.getToken());
            return true;
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(AUTH_TOKEN);
        printWriter.flush();
        printWriter.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
