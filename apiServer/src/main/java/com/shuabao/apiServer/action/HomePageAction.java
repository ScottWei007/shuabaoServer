package com.shuabao.apiServer.action;

import com.shuabao.core.entity.BaseEntity;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.HomePageTypeEntity;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.mapper.SettingDao;
import com.shuabao.core.mapper.UserDao;
import com.shuabao.core.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Scott Wei on 8/9/2018.
 */

@RestController
@RequestMapping("/homePage")
public class HomePageAction extends BaseAction {

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private UserDao userDao;


    //獲取首頁標簽
    @RequestMapping(value="/tag", method = RequestMethod.POST)
    public String getTag(BaseEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<>();//返回參數
        try{
            super.setData(returnDataMap, RedisUtil.getHomepageTagAndAdvertise(settingDao));
            super.setReturnInfo(returnDataMap, ReturnCode.SUCCESS, entity.getLang());
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap, ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/getTag>>>>>>>>>cause:{}",e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

    //首页内容
    @RequestMapping(value="/type", method = RequestMethod.POST)
    public String getType(HomePageTypeEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<>();//返回參數
        try{
            //校驗參數
            if(Objects.isNull(entity.getType())){
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            super.setData(returnDataMap, RedisUtil.getHomepageType(entity, userDao));
            super.setReturnInfo(returnDataMap, ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap, e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/getType>>>>>>>>>type:{},cause:{}",entity.getType(),e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap, ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/getType>>>>>>>>>type:{},cause:{}",entity.getType(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }


    //关注接口
    @RequestMapping(value="/follow", method = RequestMethod.POST)
    public String follow(HomePageTypeEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<>();//返回參數
        try{
            //校驗參數
            if(entity.getUid() == 0 || entity.getFollowUid() == 0){
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            //我的关注
            RedisUtil.setHomepageType(entity.getFollowUid(),System.currentTimeMillis(), HomePageTypeEntity.HomePageType.FOLLOW, entity.getUid());
            super.setReturnInfo(returnDataMap, ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap, e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/follow>>>>>>>>>uid:{},cause:{}",entity.getUid(),e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap, ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/follow>>>>>>>>>uid:{},cause:{}",entity.getUid(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

}
