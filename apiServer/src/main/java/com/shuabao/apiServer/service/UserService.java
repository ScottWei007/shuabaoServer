package com.shuabao.apiServer.service;

import com.shuabao.core.entity.UserInfoEntity;
import com.shuabao.core.entity.UserOpenPlatInfoEntity;
import com.shuabao.core.entity.UserSession;
import com.shuabao.core.entity.VerificationCodeEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public interface UserService {
    UserInfoEntity getUserByPhone(long phone);

    int addVerificationCode(VerificationCodeEntity entity);

    VerificationCodeEntity getVerificationCodeByPhone(VerificationCodeEntity entity);

    Integer getDriverBlackListCount(String id);

    UserOpenPlatInfoEntity getUserOpenPlatInfo(String openId, int regType);

    UserSession register(UserInfoEntity entity, HttpServletRequest request);

    void updateUserPasswrod(UserInfoEntity entity);

    void updateUserInfo(UserInfoEntity entity);

    UserSession updateUserToken(UserInfoEntity entity, boolean isLogin);

    UserInfoEntity getUserInfoByUid(UserInfoEntity entity);

    void addUserLoginLog(HttpServletRequest request, UserInfoEntity entity);

    UserSession getUserSessionByUid(UserInfoEntity entity);

    void addUserChangeLog(int uid, long num, int logtype);
}
