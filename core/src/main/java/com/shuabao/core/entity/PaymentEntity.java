package com.shuabao.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shuabao.core.util.MD5Encode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public class PaymentEntity extends BaseEntity {
    private long oid; // INT(11) NOT NULL COMMENT '訂單號',
    private int uid; // INT(11) NOT NULL COMMENT '用戶號',
    private int paytype;// TINYINT(2) DEFAULT NULL COMMENT '充值類型:1,apple，2,支付寶, 3,微信支付',
    private int payment; //TINYINT(2) DEFAULT 1 COMMENT '充值方式:1,APP应用;2,H5;3,扫码;',
    private int amount;//  INT(11) DEFAULT NULL COMMENT '申请支付金额',
    private int rid;// INT(11) DEFAULT NULL COMMENT '充值点id',
    private int status;//TINYINT(2)) DEFAULT 1 COMMENT '訂單状态:1等待支付,2支付成功,3支付失败,4交易完成(支付成功才會進入交易階段)',
    private String prepayorderId;//VARCHAR(128) DEFAULT NULL COMMENT '第三方预支付订单号',
    private String notifyurl;//VARCHAR(128) DEFAULT NULL COMMENT '回调地址',
    private String country;// VARCHAR(10) DEFAULT NULL COMMENT '国家代码',
    private String notifyoid;// VARCHAR(128) DEFAULT NULL COMMENT '第三方支付订单号',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notifytime;// DATETIME DEFAULT NULL COMMENT '第三方支付平台回调时间',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;//  logTime DATETIME DEFAULT NOW() COMMENT '創建時間',

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNotifyoid() {
        return notifyoid;
    }

    public void setNotifyoid(String notifyoid) {
        this.notifyoid = notifyoid;
    }

    public Date getNotifytime() {
        return notifytime;
    }

    public void setNotifytime(Date notifytime) {
        this.notifytime = notifytime;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getPrepayorderId() {
        return prepayorderId;
    }

    public void setPrepayorderId(String prepayorderId) {
        this.prepayorderId = prepayorderId;
    }
}
