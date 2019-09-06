package com.shuabao.apiServer.service.impl;

import com.shuabao.apiServer.action.BaseAction;
import com.shuabao.apiServer.manager.UserSessionManager;
import com.shuabao.apiServer.service.UserService;
import com.shuabao.core.entity.*;
import com.shuabao.core.mapper.UserDao;
import com.shuabao.core.util.RedisUtil;
import com.shuabao.apiServer.util.SMSUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Scott Wei on 4/15/2018.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserInfoEntity getUserByPhone(long phone) {
        return userDao.getUserByPhone(phone);
    }

    @Override
    public int addVerificationCode(VerificationCodeEntity entity) {
        entity.setVerificationCode(String.valueOf(SMSUtil.getVerificationCode()));
        entity.setLogTime(System.currentTimeMillis());
        entity.setTimes(1);
        return userDao.addVerificationCode(entity);
    }

    @Override
    public VerificationCodeEntity getVerificationCodeByPhone(VerificationCodeEntity entity) {
        return userDao.getVerificationCodeByPhone(entity);
    }

    @Override
    public Integer getDriverBlackListCount(String did) {
        return userDao.getDriverBlackListCount(did);
    }

    @Override
    public UserOpenPlatInfoEntity getUserOpenPlatInfo(String openId, int regType) {
        return userDao.getUserOpenPlatInfo(openId, regType);
    }

    @Override
    public UserSession register(UserInfoEntity entity, HttpServletRequest request) {
        Map<String, Integer> idMap = new HashMap();
        idMap.put("id",0);
        userDao.buildUid(idMap);//build uid
        entity.setUid(idMap.get("id") * 11);//自增步長改為11
        entity.setIp(BaseAction.getIpAddr(request));//必須手動設置ip
        entity.setToken(UUID.randomUUID().toString().replace("-", ""));
        entity.setLogTime(new Date());
        entity.setLastlogintime(entity.getLogTime());
        if(entity.getRegType() != 1) {
            String thirdNickname = request.getParameter("thirdNickname");//第三方昵称,昵称字段长度限制为16个字符
            thirdNickname = StringUtils.isEmpty(thirdNickname) ? String.valueOf((int)((Math.random() * 9 + 1) * 1000000)) : thirdNickname;
            thirdNickname = thirdNickname.length() <= 16 ? thirdNickname : thirdNickname.substring(0,16);
            entity.setNickname(thirdNickname);
            entity.setAvatar(StringUtils.defaultString(request.getParameter("thirdAvatar"), ""));// 第三方头像
            entity.setSex("1".equals(request.getParameter("thirdSex")) ? 1 : 2);
            UserOpenPlatInfoEntity openPlatInfoEntity = new UserOpenPlatInfoEntity();
            openPlatInfoEntity.setUid(entity.getUid());
            openPlatInfoEntity.setOpenId(request.getParameter("openId"));//第三方平臺id
            openPlatInfoEntity.setNickname(entity.getNickname());
            openPlatInfoEntity.setAvatar(entity.getAvatar());
            openPlatInfoEntity.setRegType(entity.getRegType());
            openPlatInfoEntity.setLogTime(entity.getLogTime());
            //addUserOpenPlatInfoEntity
            userDao.addUserOpenPlatInfo(openPlatInfoEntity);
        }
        //addUserInfoEntity用戶基本信息，分表
        userDao.addUserInfo(entity);
        //addUserPhoneInfo用戶手機號，不分表
        userDao.addUserPhoneInfo(entity);
        //統計
        userDao.addNewRegisterLog(entity);
        //缓存session
        UserSession session = new UserSession();
        session.setUid(String.valueOf(entity.getUid()));
        session.setToken(entity.getToken());
        session.setPhone(String.valueOf(entity.getPhone()));
        session.setEducation("3");
        session.setMarriage("1");
        session.setLevel("1");
        session.setViplevel("2");//不是vip
        session.setPreview("2");//未完善资料
        if(StringUtils.isNotEmpty(entity.getNickname()))
            session.setNickname(entity.getNickname());
        if(StringUtils.isNotEmpty(entity.getAvatar()))
            session.setAvatar(entity.getAvatar());
        if(entity.getSex() != 0)
            session.setSex(String.valueOf(entity.getSex()));
        //存redis的session缓存
        RedisUtil.setUserSession(session);
        //存redis的新人缓存
        RedisUtil.setHomepageType(entity.getUid(),System.currentTimeMillis(),HomePageTypeEntity.HomePageType.NEW,null);
        return session;
    }

    @Override
    public void updateUserPasswrod(UserInfoEntity entity) {
        UserInfoEntity userInfoEntity = userDao.getUserByPhone(entity.getPhone());
        //通過phone去查uid，並賦值uid
        entity.setUid(userInfoEntity.getUid());
        userDao.updateUserPasswrod(entity);
    }

    @Override
    public void updateUserInfo(UserInfoEntity entity) {
        int result = userDao.updateUserInfo(entity);
        if(result == 1) {
            //更新缓存
            UserSession session = RedisUtil.getUserSession(String.valueOf(entity.getUid()), userDao);
            if(Objects.nonNull(session)) {
                if(StringUtils.isNotEmpty(entity.getNickname()))
                    session.setNickname(entity.getNickname());
                if(StringUtils.isNotEmpty(entity.getAvatar()))
                    session.setAvatar(entity.getAvatar());
                if(entity.getSex() != 0)
                    session.setSex(String.valueOf(entity.getSex()));
                if(StringUtils.isNotEmpty(entity.getAddr()))
                    session.setAddr(entity.getAddr());
                if(StringUtils.isNotEmpty(entity.getSign()))
                    session.setSign(entity.getSign());
                if(entity.getLevel() != 0)
                    session.setLevel(String.valueOf(entity.getLevel()));
                if(entity.getHeight() != 0)
                    session.setHeight(String.valueOf(entity.getHeight()));
                if(entity.getEducation() != 0)
                    session.setEducation(String.valueOf(entity.getEducation()));
                if(entity.getMarriage() != 0)
                    session.setMarriage(String.valueOf(entity.getMarriage()));
                if(StringUtils.isNotEmpty(entity.getIncome()))
                    session.setIncome(entity.getIncome());
                if(entity.getExpectedtime() != 0)
                    session.setExpectedtime(String.valueOf(entity.getExpectedtime()));
                if(entity.getViplevel() != 0)
                    session.setViplevel(String.valueOf(entity.getViplevel()));
                if(entity.getBonds() != 0)
                    session.setBonds(String.valueOf(NumberUtils.toLong(session.getBonds()) + entity.getBonds()));//加减
                if(entity.getGold() != 0)
                    session.setGold(String.valueOf(NumberUtils.toLong(session.getGold()) + entity.getGold()));//加减
                if(entity.getPrice() != 0)
                    session.setPrice(String.valueOf(entity.getPrice()));
                if(entity.getPreview() != 0)
                    session.setPreview(String.valueOf(entity.getPreview()));
                if(entity.getStatus() != 0)
                    session.setStatus(String.valueOf(entity.getStatus()));
                if(StringUtils.isNotEmpty(entity.getBirth()))
                    session.setBirth(entity.getBirth());

                RedisUtil.setUserSession(session);
            }
        }
    }

    @Override
    public UserSession updateUserToken(UserInfoEntity entity, boolean isLogin) {
        if(isLogin) {//登錄
            entity.setToken(UUID.randomUUID().toString().replace("-", ""));
            entity.setLastlogintime(new Date());
            entity.setLastlogouttime(null);//確保不會更新登出時間
        }else {//登出
            entity.setToken("");
            entity.setLastlogintime(null);
            entity.setLastlogouttime(new Date());
        }
        //更新表
        int result = userDao.updateUserToken(entity);
        UserSession session = null;
        if(result == 1) {
            //更新缓存
            session = RedisUtil.getUserSession(String.valueOf(entity.getUid()), userDao);
            if(Objects.nonNull(session)) {
                session.setToken(entity.getToken());
                RedisUtil.setUserSession(session);
            }
        }

        //清理本地缓存
        UserSessionManager.getInstance().removeUserToken(entity.getUid());
        return session;
    }

    @Override
    public UserInfoEntity getUserInfoByUid(UserInfoEntity entity) {
        return userDao.getUserInfoByUid(entity);
    }

    @Override
    public void addUserLoginLog(HttpServletRequest request, UserInfoEntity entity) {
        entity.setIp(BaseAction.getIpAddr(request));//手動設置ip
        entity.setLogTime(new Date());
        userDao.addUserLoginLog(entity);
    }

    @Override
    public UserSession getUserSessionByUid(UserInfoEntity entity) {
        return RedisUtil.getUserSession(String.valueOf(entity.getUid()), userDao);
    }

    @Override
    public void addUserChangeLog(int uid, long num, int logtype) {
        UserChangeLogEntity userChangeLogEntity = new UserChangeLogEntity();
        userChangeLogEntity.setUid(uid);
        userChangeLogEntity.setNum(num);
        userChangeLogEntity.setLogtype(logtype);
        userDao.addUserChangeLog(userChangeLogEntity);
    }

}
