package com.shuabao.apiServer.action;

import com.shuabao.apiServer.service.PaymentService;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.PaymentEntity;
import com.shuabao.core.exception.ShuabaoException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Scott Wei on 8/9/2018.
 */

@RestController
@RequestMapping("/payment")
public class PaymentAction extends BaseAction {

    @Autowired
    private PaymentService paymentService;

    //下单
    @RequestMapping(value="/order", method = RequestMethod.POST)
    public String addOrder(HttpServletRequest request, PaymentEntity entity) {
        Map<String,Object> returnDataMap = new HashMap<>();//返回參數
        try{
            //校驗參數
            if(entity.getAmount() <= 0 || entity.getRid() == 0 || entity.getPaytype() == 0){
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            entity.setIp(BaseAction.getIpAddr(request));
            super.setData(returnDataMap, paymentService.addOrder(entity));
            super.setReturnInfo(returnDataMap, ReturnCode.SUCCESS, entity.getLang());
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap, e.getCode(),entity.getLang());
            super.log.error(">>>>>>>>>/order>>>>>>>>>uid:{},Paytype:{},cause:{}",entity.getUid(),entity.getPaytype(),e.getMsg(entity.getLang()));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap, ReturnCode.FAILURE, entity.getLang());
            super.log.error(">>>>>>>>>/order>>>>>>>>>uid:{},Paytype:{},cause:{}",entity.getUid(),entity.getPaytype(),e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

    //apple回调订单
    @RequestMapping(value="appleNotify", method = RequestMethod.POST)
    public String appleNotify(HttpServletRequest request) {
        Map<String,Object> returnDataMap = new HashMap<>();//返回參數
        String oid  = request.getParameter("oid");
        String receipt = request.getParameter("receipt");
        String pkg = request.getParameter("pkg");
        String version = request.getParameter("version");
        int lang = NumberUtils.toInt(request.getParameter("lang"));
        try{
            //校驗參數
            if(StringUtils.isAnyEmpty(oid, receipt, pkg, version)){
                throw new ShuabaoException(ReturnCode.INVALID_PARAMETERS);
            }
            paymentService.getAppleNotify(oid, receipt, pkg, version);
            super.setReturnInfo(returnDataMap, ReturnCode.SUCCESS, lang);
        }catch(ShuabaoException e) {
            super.setReturnInfo(returnDataMap, e.getCode(), lang);
            super.log.error(">>>>>>>>>/appleNotify>>>>>>>>>oid:{},cause:{}", oid, e.getMsg(lang));
        }catch(Exception e) {
            super.setReturnInfo(returnDataMap, ReturnCode.FAILURE, lang);
            super.log.error(">>>>>>>>>/appleNotify>>>>>>>>>oid:{},cause:{}", oid, e.getMessage());
        }finally {
            return toJson(returnDataMap,false);
        }
    }

}
