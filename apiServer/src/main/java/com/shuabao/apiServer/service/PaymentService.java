package com.shuabao.apiServer.service;

import com.shuabao.core.entity.*;
import com.shuabao.core.exception.ShuabaoException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public interface PaymentService {
    Map<String, Object> addOrder(PaymentEntity entity)  throws ShuabaoException;

    void getAppleNotify(String oid, String receipt, String pkg, String version) throws ShuabaoException;
}
