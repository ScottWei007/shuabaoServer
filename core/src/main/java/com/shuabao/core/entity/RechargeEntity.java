package com.shuabao.core.entity;


/**
 * Created by Scott Wei on 4/15/2018.
 */
public class RechargeEntity{

    private int rid;// INT(11) NOT NULL COMMENT '充值点id',
    private int amount; // INT(11) NOT NULL COMMENT '金額面值',
    private int diamond; // INT(11) DEFAULT NULL COMMENT '钻石数',
    private int freenum;// INT(11) DEFAULT 0 COMMENT '赠送钻石数量',
    private int paytype;// TINYINT(2) DEFAULT NULL COMMENT '充值類型:1,apple;2,支付寶; 3,微信支付',
    private int issale; // TINYINT(2) DEFAULT 0 COMMENT '是否促销,0,否，1是',
    private int isdouble;// TINYINT(2) DEFAULT 0 COMMENT '是否双倍:0,否，1是',
    private String pkg; //VARCHAR(20) COMMENT '包',
    private String os; //VARCHAR(7) COMMENT '移動端系統,ios, android',
    private int sort;//TINYINT(2) DEFAULT 0 COMMENT '排序',

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getFreenum() {
        return freenum;
    }

    public void setFreenum(int freenum) {
        this.freenum = freenum;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public int getIssale() {
        return issale;
    }

    public void setIssale(int issale) {
        this.issale = issale;
    }

    public int getIsdouble() {
        return isdouble;
    }

    public void setIsdouble(int isdouble) {
        this.isdouble = isdouble;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
