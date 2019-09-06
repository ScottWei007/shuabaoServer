package com.shuabao.core.mapper;


import com.shuabao.core.entity.PaymentEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public interface PaymentDao {

    int addOrder(PaymentEntity entity);

    PaymentEntity getOrderById(String oid);

    int updateOrderStatus(PaymentEntity entity);

    int getCount(String receiptMd5);

    int addReceipt(@Param("receiptMd5") String receiptMd5 , @Param("receipt") String receipt, @Param("oid") String oid);

    int updateReceipt(String receiptMd5);

}
