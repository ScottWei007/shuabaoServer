package com.shuabao.apiServer.util;


import com.shuabao.core.entity.UserInfoEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Scott Wei on 4/21/2018.
 */
public class CheckAuthUtil {

    //可以做個開關
    public static boolean checkAuth(HttpServletRequest request, UserInfoEntity entity) {
        return true;
    }
}
