package com.shuabao.core.mapper;

import com.shuabao.core.entity.UserChangeLogEntity;
import com.shuabao.core.entity.UserInfoEntity;
import com.shuabao.core.entity.UserOpenPlatInfoEntity;
import com.shuabao.core.entity.VerificationCodeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public interface UserDao {

    void buildUid(Map<String, Integer> idMap);

    UserInfoEntity getUserByPhone(@Param("phone") long phone);

    UserInfoEntity getPhoneByUid(@Param("uid") int uid);

    int addVerificationCode(VerificationCodeEntity entity);

    VerificationCodeEntity getVerificationCodeByPhone(VerificationCodeEntity entity);

    Integer getDriverBlackListCount(String did);

    UserOpenPlatInfoEntity getUserOpenPlatInfo(@Param("openId") String openId, @Param("regType") int regType);

    void addUserInfo(UserInfoEntity entity);

    void addUserPhoneInfo(UserInfoEntity entity);

    void addNewRegisterLog(UserInfoEntity entity);

    void addUserOpenPlatInfo(UserOpenPlatInfoEntity entity);

    void updateUserPasswrod(UserInfoEntity entity);

    int updateUserToken(UserInfoEntity entity);

    int updateUserInfo(UserInfoEntity entity);

    UserInfoEntity getUserInfoByUid(UserInfoEntity entity);

    void addUserLoginLog(UserInfoEntity entity);

    void addUserChangeLog(UserChangeLogEntity userChangeLogEntity);
}
