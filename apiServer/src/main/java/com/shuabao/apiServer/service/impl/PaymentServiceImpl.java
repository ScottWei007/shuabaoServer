package com.shuabao.apiServer.service.impl;

import com.shuabao.apiServer.service.PaymentService;
import com.shuabao.apiServer.service.UserService;
import com.shuabao.apiServer.util.OrderIdUtil;
import com.shuabao.core.base.ReturnCode;
import com.shuabao.core.entity.PaymentEntity;
import com.shuabao.core.entity.RechargeEntity;
import com.shuabao.core.entity.UserInfoEntity;
import com.shuabao.core.exception.ShuabaoException;
import com.shuabao.core.manager.AlipayManager;
import com.shuabao.core.manager.AppleManager;
import com.shuabao.core.manager.PushMessageManager;
import com.shuabao.core.manager.WechatManager;
import com.shuabao.core.mapper.PaymentDao;
import com.shuabao.core.mapper.SettingDao;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.core.util.MD5Encode;
import com.shuabao.core.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by Scott Wei on 4/15/2018.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private UserService userService;

    @Override
    public  Map<String, Object> addOrder(PaymentEntity entity) throws ShuabaoException{
        //校驗支付點
        RechargeEntity rechargeEntity = RedisUtil.getRechargeByRid(entity.getRid(), settingDao);
        if(Objects.isNull(rechargeEntity) || rechargeEntity.getAmount() != entity.getAmount()){
            throw new ShuabaoException(ReturnCode.RECHARGE_NOT_FOUND);
        }
        //創建訂單id
        entity.setOid(OrderIdUtil.getOrderId());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("oid", entity.getOid());
        switch (entity.getPaytype()) {//充值類型
            case 1: //1,apple,不用操作
                break;
            case 2: //2,支付寶
                switch (entity.getPayment()) {//'充值方式
                    case 1://,APP应用
                        resultMap.put("payInfo", AlipayManager.getSingleton().appPay(entity));
                        break;
                    case 2://,H5
                        resultMap.put("payInfo", AlipayManager.getSingleton().h5Pay(entity));
                        break;
                    case 3://,扫码
                        resultMap.put("payInfo", AlipayManager.getSingleton().qrPay(entity));
                        break;
                }
                break;
            case 3: //3,微信支付
                resultMap.put("payInfo", WechatManager.prepareOrder(entity));
                break;
        }
        int count = paymentDao.addOrder(entity);
        if(count == 1) {
            return resultMap;
        }else {
            throw new ShuabaoException(ReturnCode.FAILED_ORDER);
        }
    }

    @Override
    public void getAppleNotify(String oid, String receipt, String pkg, String version) throws ShuabaoException {
        //校验
        PaymentEntity paymentEntity = paymentDao.getOrderById(oid);
        if(Objects.isNull(paymentEntity) || paymentEntity.getStatus() != 1) {
            throw new ShuabaoException(ReturnCode.ORDER_NOT_FOUND_OR_DONE);
        }
        RechargeEntity rechargeEntity = RedisUtil.getRechargeByRid(paymentEntity.getRid(), settingDao);
        if(Objects.isNull(rechargeEntity) || rechargeEntity.getAmount() != paymentEntity.getAmount()){
            throw new ShuabaoException(ReturnCode.RECHARGE_NOT_FOUND);
        }
        String receiptMd5 = MD5Encode.getMD5Str(receipt);
        if(StringUtils.isEmpty(receiptMd5) || paymentDao.getCount(receiptMd5) != 0) {
            throw new ShuabaoException(ReturnCode.INVALID_RECEIPT);
        }
        //调用苹果验证服务
        List<Map<String, Object>> list = AppleManager.verifyReceipt(receiptMd5, receipt, RedisUtil.isSandbox(pkg + version));
        paymentEntity.setNotifytime(new Date());
        if(Objects.isNull(list)) {
            paymentEntity.setStatus(3);//支付失败
            paymentDao.updateOrderStatus(paymentEntity);
            throw new ShuabaoException(ReturnCode.FAILED_VERIFY_RECEIPT);
        }
        paymentEntity.setStatus(2);//支付成功
        paymentEntity.setNotifyoid(String.valueOf(list.get(0).get("transaction_id")));
        if(paymentDao.updateOrderStatus(paymentEntity) != 1) {
            throw new ShuabaoException(ReturnCode.FAILED_ORDER_UPDATE);
        }
        //记录凭据（发货前）
        if(paymentDao.addReceipt(receiptMd5, receipt, oid) != 1) {
            throw new ShuabaoException(ReturnCode.FAILED_RECEIPT_ADD);
        }
        ///////////发货流程开始
        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUid(paymentEntity.getUid());
        userInfo.setGold(rechargeEntity.getDiamond() + rechargeEntity.getFreenum());
        userService.updateUserInfo(userInfo);
        userService.addUserChangeLog(userInfo.getUid(), userInfo.getGold(), 1);
        ///////////发货流程结束
        //更新凭据被使用过（发货后）
        if(paymentDao.updateReceipt(receiptMd5) != 1) {
            throw new ShuabaoException(ReturnCode.FAILED_RECEIPT_UPDATE);
        }
        paymentEntity.setStatus(4);//支付交易完成状态
        paymentEntity.setNotifyoid(null);
        paymentEntity.setNotifytime(new Date());
        if(paymentDao.updateOrderStatus(paymentEntity) != 1) {
            throw new ShuabaoException(ReturnCode.FAILED_ORDER_UPDATE);
        }
        //发送通知/
//        MessageWrapper messageWrapper = new MessageWrapper();
//        messageWrapper.putData("title", "苹果支付充值成功");
//        messageWrapper.putData("amount", paymentEntity.getAmount());
//        PushMessageManager.pushMessage(paymentEntity.getUid(),messageWrapper);
    }
}
