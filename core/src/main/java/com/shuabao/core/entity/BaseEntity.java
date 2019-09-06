package com.shuabao.core.entity;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public class BaseEntity {
    private String pkg; //包名 package
    private String ip; //ip地址
    private String cid; //渠道號 channel id
    private String did; //設備號 android的driver id, iOS的uuid
    private String os; //移動端系統
    private String version;//app版本號
    private String lang; //app語言版本
    private String model;//設備型號

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getIp() {
        return  this.ip;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public int getLang() {
        return NumberUtils.toInt(this.lang);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
