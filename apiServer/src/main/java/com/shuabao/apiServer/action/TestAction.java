package com.shuabao.apiServer.action;

import com.shuabao.apiServer.service.TestService;

import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.config.redis.CommonRedisConfigurer;
import com.shuabao.core.entity.TestEntity;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.manager.PushMessageManager;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.core.rpc.zookeeper.ZookeeperManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Scott Wei on 4/6/2018.
 */

@RestController
@Scope("singleton")//默認同一個實例
@RequestMapping("/test")
public class TestAction extends BaseAction {

    @Autowired
    private TestService testService;


    //獲取列表
    @RequestMapping(value ="/findAll",method = RequestMethod.GET)
    public String findAllEntity (HttpServletRequest request) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        int lang = 0;//獲取語言版本，1中文，2繁體，3英語
        try{
            /*redis測試----開始*/
            CommonRedisConfigurer.getInstance().set("TEST_REDIS","測試redis");
            returnDataMap.put("TEST_REDIS", CommonRedisConfigurer.getInstance().get("TEST_REDIS"));
            /*redis測試----結束*/
            lang = NumberUtils.toInt(request.getParameter("lang"));
            /*一，檢驗參數,攔截器已經對所有請求進行參數校驗，這裏衹是示範抛出異常*/
            if(lang == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS); //沒有lang參數，即自定義的參數不全異常
            }
             /*二，獲取數據*/
            List<TestEntity> entities = testService.findAllEntity();
             /*三,存放縂Map數據*/
            returnDataMap.put("result", entities);
            /*四，根據request裏面的lang字段返回對應語言的響應碼*/
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, lang);
            super.log.debug("===============TestController請求成功");//日志
        }catch(ShuabaoException e) {//自定義異常
            super.setReturnInfo(returnDataMap,e.getCode(), lang);
            super.log.error("===============TestController請求異常，信息為：" + e.getMsg(lang));//日志
        }catch (Exception e2) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, lang);//返回失敗信息
            super.log.error("===============TestController請求異常，信息為：" + e2);//日志
        }

        ///////////
        Map<String,List<String>> getServers = ZookeeperManager.getInstance().getServers();
        MessageWrapper messageWrapper = new MessageWrapper();
        messageWrapper.setLang("1");
        messageWrapper.putData("message", "消息");
        PushMessageManager.pushMessage(7777,messageWrapper);


            /*五,運行到這裏，Map有result，code，msg的key。這裏的toJson返回這個Map是否加密的json,參數false表示不對json加密*/
        return super.toJson(returnDataMap,false);
    }


    //通過id查詢單個實體類
    //查詢可以不用指定請求方法類型,有多個請求參數，可以/findById/{id}/{name}的請求,也可以直接用實體類接收
    @RequestMapping(value = "/findById/{id}")
    public String findEntityById (HttpServletRequest request,  @PathVariable("id") Integer id) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        int lang = 0;//獲取語言版本，1中文，2繁體，3英語
        try{
            lang = NumberUtils.toInt(request.getParameter("lang"));
            /*一，檢驗參數,攔截器已經對所有請求進行參數校驗，這裏衹是示範抛出異常*/
            if(lang == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS); //沒有lang參數，即自定義的參數不全異常
            }
             /*二，查詢數據*/
            TestEntity testEntity = testService.findEntityById(id);
             /*三,存放縂Map數據*/
            returnDataMap.put("result", testEntity);
            /*四，根據request裏面的lang字段返回對應語言的響應碼*/
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, lang);
            super.log.debug("===============TestController請求成功");//日志
        }catch(ShuabaoException e1) {//自定義異常
            super.setReturnInfo(returnDataMap,e1.getCode(), lang);
            super.log.error("===============TestController請求異常，信息為：" + e1.getMsg(lang));//日志
        }catch (Exception e2) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, lang);//返回失敗信息
            super.log.error("===============TestController請求異常，信息為：" + e2);//日志
        }
            /*五,運行到這裏，Map有result，code，msg的key。這裏的toJson返回這個Map是否加密的json,參數false表示不對json加密*/
        return super.toJson(returnDataMap,false);
    }


    //添加,Spring自動把參數初始化到TestEntity實體類
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addEntity (HttpServletRequest request, TestEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        int lang = 0;//獲取語言版本，1中文，2繁體，3英語
        try{
            lang = NumberUtils.toInt(request.getParameter("lang"));
            /*一，檢驗參數,攔截器已經對所有請求進行參數校驗，這裏衹是示範抛出異常*/
            if(lang == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS); //沒有lang參數，即自定義的參數不全異常
            }
             /*二，插入數據*/
            testService.addEntity(entity);
            /*三，根據request裏面的lang字段返回對應語言的響應碼*/
            super.setReturnInfo(returnDataMap, ReturnCode.SUCCESS, lang);
            super.log.debug("===============TestController請求成功");//日志
        }catch(ShuabaoException e1) {//自定義異常
            super.setReturnInfo(returnDataMap,e1.getCode(), lang);
            super.log.error("===============TestController請求異常，信息為：" + e1.getMsg(lang));//日志
        }catch (Exception e2) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, lang);//返回失敗信息
            super.log.error("===============TestController請求異常，信息為：" + e2);//日志
        }
            /*四,運行到這裏，Map有result，code，msg的key。這裏的toJson返回這個Map是否加密的json,參數false表示不對json加密*/
        return super.toJson(returnDataMap,false);
    }

    //修改,Spring自動把參數初始化到TestEntity實體類
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateEntity (HttpServletRequest request, TestEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        int lang = 0;//獲取語言版本，1中文，2繁體，3英語
        try{
            lang = NumberUtils.toInt(request.getParameter("lang"));
            /*一，檢驗參數,攔截器已經對所有請求進行參數校驗，這裏衹是示範抛出異常*/
            if(lang == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS); //沒有lang參數，即自定義的參數不全異常
            }
             /*二，修改數據*/
            testService.updateEntity(entity);
            /*三，根據request裏面的lang字段返回對應語言的響應碼*/
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, lang);
            super.log.debug("===============TestController請求成功");//日志
        }catch(ShuabaoException e1) {//自定義異常
            super.setReturnInfo(returnDataMap,e1.getCode(), lang);
            super.log.error("===============TestController請求異常，信息為：" + e1.getMsg(lang));//日志
        }catch (Exception e2) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, lang);//返回失敗信息
            super.log.error("===============TestController請求異常，信息為：" + e2);//日志
        }
            /*四,運行到這裏，Map有result，code，msg的key。這裏的toJson返回這個Map是否加密的json,參數false表示不對json加密*/
        return super.toJson(returnDataMap,false);
    }

    //刪除
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String deleteEntity (HttpServletRequest request,  @PathVariable("id") Integer id) {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();//返回參數
        int lang = 0;//獲取語言版本，1中文，2繁體，3英語
        try{
            lang = NumberUtils.toInt(request.getParameter("lang"));
            /*一，檢驗參數,攔截器已經對所有請求進行參數校驗，這裏衹是示範抛出異常*/
            if(lang == 0) {
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS); //沒有lang參數，即自定義的參數不全異常
            }
             /*二，刪除數據*/
            testService.deleteEntity(id);
            /*三，根據request裏面的lang字段返回對應語言的響應碼*/
            super.setReturnInfo(returnDataMap,ReturnCode.SUCCESS, lang);
            super.log.debug("===============TestController請求成功");//日志
        }catch(ShuabaoException e1) {//自定義異常
            super.setReturnInfo(returnDataMap,e1.getCode(), lang);
            super.log.error("===============TestController請求異常，信息為：" + e1.getMsg(lang));//日志
        }catch (Exception e2) {
            super.setReturnInfo(returnDataMap,ReturnCode.FAILURE, lang);//返回失敗信息
            super.log.error("===============TestController請求異常，信息為：" + e2);//日志
        }
            /*四,運行到這裏，Map有result，code，msg的key。這裏的toJson返回這個Map是否加密的json,參數false表示不對json加密*/
        return super.toJson(returnDataMap,false);
    }

}
