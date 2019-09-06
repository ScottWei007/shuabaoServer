package com.shuabao.core.entity;


/**
 * Created by Scott Wei on 4/15/2018.
 */
public class VerificationCodeEntity extends BaseEntity {
    private String countryCode;//國家代碼
    private String phone;//手機號
    private String verificationCode;//驗證碼
    private long logTime;//插入時間
    private int times;//校驗的次數
    private int type;//驗證碼類型,1是注冊，2，重置密碼


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
