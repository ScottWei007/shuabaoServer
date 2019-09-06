package com.shuabao.apiServer.action;

import com.shuabao.apiServer.service.UserService;
import com.shuabao.apiServer.util.UploadUtil;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.UserInfoEntity;
import com.shuabao.core.entity.UserOpenPlatInfoEntity;
import com.shuabao.core.entity.UserSession;
import com.shuabao.core.entity.VerificationCodeEntity;
import com.shuabao.apiServer.util.CheckAuthUtil;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.util.MD5Encode;
import com.shuabao.apiServer.util.SMSUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Scott Wei on 4/15/2018.
 * 用戶操作類
 */

@RestController
@RequestMapping("/user")
public class UserAction extends BaseAction {

    @Autowired
    private UserService userService;

    //發送驗證碼,生成驗證碼用Post方法
    @RequestMapping(value="/verificationCode", method = RequestMethod.POST)
    public String addVerificationCode(VerificationCodeEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            //校驗參數
            if(StringUtils.isEmpty(entity.getCountryCode()) || StringUtils.isEmpty(entity.getPhone()) || entity.getType() == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            //校驗手機號碼合法
            if(!SMSUtil.isValidNumber(entity)) {
                throw new ShuabaoException(ReturnCode.INVALID_PHONE);
            }
            //校驗手機號碼是否注冊過,1注冊類型驗證碼
            if(entity.getType() == 1 && !Objects.isNull(userService.getUserByPhone(NumberUtils.toLong(entity.getPhone())))) {
                throw new ShuabaoException(ReturnCode.PHONE_REGREGISTERED);
            }
            //校驗手機號碼是否注冊過,2,重置密碼類型驗證碼
            if(entity.getType() == 2 && Objects.isNull(userService.getUserByPhone(NumberUtils.toLong(entity.getPhone())))) {
                throw new ShuabaoException(ReturnCode.PHONE_UNREGREGISTERED);
            }
            //校驗是否惡意發送,總共不能超過50次
            VerificationCodeEntity temp = userService.getVerificationCodeByPhone(entity);
            if(!Objects.isNull(temp) && temp.getTimes() > 50) {
                throw new ShuabaoException(ReturnCode.VERIFICATIONCODE_TOO_MUCH);
            }
            //生成驗證碼
            userService.addVerificationCode(entity);
            if(!SMSUtil.send(entity)) {//發送驗證碼
                throw new ShuabaoException(ReturnCode.FAILED_SEND);
            }
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap,e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/verificationCode>>>>>>>>>phone:{},type:{},cause:{}",entity.getPhone(),entity.getType(),e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/verificationCode>>>>>>>>>phone:{},type:{},cause:{}",entity.getPhone(),entity.getType(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

    //校驗驗證碼,用get方法
    @RequestMapping(value="/verificationCode", method = RequestMethod.GET)
    public String checkVerificationCode(VerificationCodeEntity entity) {
         Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            //校驗參數
            if(StringUtils.isEmpty(entity.getCountryCode()) || StringUtils.isEmpty(entity.getPhone()) || StringUtils.isEmpty(entity.getVerificationCode()) || entity.getType() == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            //校驗驗證碼是否過期(超過30分)
            VerificationCodeEntity temp = userService.getVerificationCodeByPhone(entity);
            if(Objects.isNull(temp) || (System.currentTimeMillis() - temp.getLogTime()) > 1000 * 60 * 30) {
                throw new ShuabaoException(ReturnCode.EXPIRE_VERIFICATIONCODE);
            }
            //校驗驗證碼是否正確
            if(!entity.getVerificationCode().equals(temp.getVerificationCode())){
                throw new ShuabaoException(ReturnCode.INVALID_VERIFICATIONCODE);
            }
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap,e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/verificationCode>>>>>>>>>phone:{},type:{},cause:{}",entity.getPhone(),entity.getType(),e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/verificationCode>>>>>>>>>phone:{},type:{},cause:{}",entity.getPhone(),entity.getType(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }


    //用戶注冊,用Post方法
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request, UserInfoEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            //校驗是否被封,//did設備號 android的driver id, iOS的uuid
            if(userService.getDriverBlackListCount(entity.getDid()) > 0) {
                throw new ShuabaoException(ReturnCode.DRIVER_BLACK_LIST);
            }
            if(entity.getRegType() <= 0 ) {
                //校驗注冊類型參數合法
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }else if (entity.getRegType() == 1) { //校驗手機注冊類型為1
                //手機注冊參數
                String pwd = entity.getPwd();
                if(entity.getCountryCode() == 0 || entity.getPhone() == 0 || StringUtils.isEmpty(pwd)) {
                    throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
                }
                //校驗手機號碼是否注冊過
                if(!Objects.isNull(userService.getUserByPhone(entity.getPhone()))) {
                    throw new ShuabaoException(ReturnCode.PHONE_REGREGISTERED);
                }
                //校驗密碼格式
                if (pwd.length() < 6 || pwd.length() > 16 || !(pwd.matches("^.*[0-9]+.*$") &&
                        (pwd.matches("^.*[a-zA-Z]+.*$") || pwd.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$")))) {
                    throw new ShuabaoException(ReturnCode.INVALID_PASSWORD);
                }
                entity.buildPasswd();//構建加密密碼
            }else {//校驗第三方注冊
                String openId = request.getParameter("openId");//第三方平臺id
                if (StringUtils.isEmpty(openId)) {
                    throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
                }
                //校驗是否已經幫過該第三方
                if(!Objects.isNull(userService.getUserOpenPlatInfo(openId , entity.getRegType()))) {
                    throw new ShuabaoException(ReturnCode.OPEN_PLAT_REGREGISTERED);
                }
            }
            //開始注冊,統計,存缓存
            UserSession session = userService.register(entity, request);
            super.setData(returnDataMap, session);
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap,e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/register>>>>>>>>>regtype:{},cause:{}",entity.getRegType(), e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/register>>>>>>>>>regtype:{},cause:{}",entity.getRegType(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }


    //用戶登陸,用Post方法
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, UserInfoEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        int uid = 0;
        try{
            //校驗是否被封,//did設備號 android的driver id, iOS的uuid
            if(userService.getDriverBlackListCount(entity.getDid()) > 0) {
                throw new ShuabaoException(ReturnCode.DRIVER_BLACK_LIST);
            }
            if(entity.getRegType() <= 0 ) {
                //校驗注冊類型參數合法
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }else if (entity.getRegType() == 1) { //校驗手機登陸類型為1
                //手機注冊參數
                if(entity.getCountryCode() == 0 || entity.getPhone() == 0 || StringUtils.isEmpty(entity.getPwd())) {
                    throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
                }
                UserInfoEntity phoneEntity = userService.getUserByPhone(entity.getPhone());
                if(Objects.isNull(phoneEntity)) {
                    throw new ShuabaoException(ReturnCode.PHONE_UNREGREGISTERED);
                }else {
                    uid = phoneEntity.getUid();
                }
            }else {//校驗第三方
                String openId = request.getParameter("openId");
                if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(request.getParameter("authToken"))) {
                    throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
                }
                UserOpenPlatInfoEntity openPlatInfoEntity = userService.getUserOpenPlatInfo(openId , entity.getRegType());
                if(Objects.isNull(openPlatInfoEntity)) {//第三方沒有注冊則去注冊
                    return register(request, entity);
                }else {
                    uid = openPlatInfoEntity.getUid();
                }
                //第三方授權=================================
                if(!CheckAuthUtil.checkAuth(request, entity)){
                    throw new ShuabaoException(ReturnCode.CHECK_AUTH_FAILURE);
                }
            }
            //用戶信息
            entity.setUid(uid);
            UserInfoEntity userInfoEntity = userService.getUserInfoByUid(entity);
            if(Objects.isNull(userInfoEntity)) {
                throw new ShuabaoException(ReturnCode.USER_NOT_FOUND);
            }else {
                if(userInfoEntity.getStatus() == 2) {
                    throw new ShuabaoException(ReturnCode.USER_BLACK_LIST);
                }
                //衹有手機登陸才有密碼,校驗
                if(StringUtils.isNotEmpty(entity.getPwd()) && !userInfoEntity.getPwd().equals(MD5Encode.encodePWD(userInfoEntity.getSalt(), entity.getPwd()))){
                    throw new ShuabaoException(ReturnCode.WRONG_PASSWORD);
                }
            }
            //更新Token,更新session
            UserSession session = userService.updateUserToken(userInfoEntity, true);
            //日志
            userService.addUserLoginLog(request, entity);
            super.setData(returnDataMap, session);
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap,e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/login>>>>>>>>>regtype:{},uid:{},cause:{}",entity.getRegType(),uid, e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/login>>>>>>>>>regtype:{},uid:{},cause:{}",entity.getRegType(),uid,e.getMessage());
        }
        //不能用final
        return toJson(returnDataMap,false);
    }


    //提交詳細信息,用Post方法,
    @RequestMapping(value="/addUserInfo", method = RequestMethod.POST)
    public String addInfo(UserInfoEntity entity, @RequestParam("file") MultipartFile file) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            //校驗參數合法
            if(StringUtils.isEmpty(entity.getNickname()) || StringUtils.isEmpty(entity.getBirth()) || StringUtils.isEmpty(entity.getAddr()) || StringUtils.isEmpty(entity.getIncome())|| Objects.isNull(file)) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            if(entity.getSex() == 0 || entity.getHeight() == 0 || entity.getEducation() == 0 || entity.getMarriage() == 0 || entity.getExpectedtime() == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            //上傳文件
            entity.setAvatar(UploadUtil.uploadFile(file));
            //更新信息
            entity.setPreview(1);//已经完善信息
            userService.updateUserInfo(entity);
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap,e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/register>>>>>>>>>regtype:{},cause:{}",entity.getRegType(), e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/register>>>>>>>>>regtype:{},cause:{}",entity.getRegType(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

    //用戶重置密碼,用Post方法
    @RequestMapping(value="/password", method = RequestMethod.POST)
    public String resetPassword(UserInfoEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            //校驗參數
            String pwd = entity.getPwd();
            if(entity.getCountryCode() == 0 || entity.getPhone() == 0 || StringUtils.isEmpty(pwd)) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            //校驗密碼格式
            if (pwd.length() < 6 || pwd.length() > 16 || !(pwd.matches("^.*[0-9]+.*$") &&
                    (pwd.matches("^.*[a-zA-Z]+.*$") || pwd.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$")))) {
                throw new ShuabaoException(ReturnCode.INVALID_PASSWORD);
            }
            entity.buildPasswd();//構建加密密碼
            userService.updateUserPasswrod(entity);//重置密碼
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap,e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/password>>>>>>>>>phone:{},cause:{}",entity.getPhone(), e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/password>>>>>>>>>phone:{},cause:{}",entity.getPhone(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

    //用戶登出,用Post方法
    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logout(UserInfoEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            userService.updateUserToken(entity, false);
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/loginout>>>>>>>>>uid:{},cause:{}",entity.getUid(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

    //获取用户信息，用Post方法
    @RequestMapping(value="/userInfo", method = RequestMethod.POST)
    public String userInfo(UserInfoEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        try{
            super.setData(returnDataMap, userService.getUserSessionByUid(entity));
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, entity.getLang());
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/userInfo>>>>>>>>>uid:{},cause:{}",entity.getUid(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }


}
