package com.shuabao.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Scott Wei on 4/17/2018.
 */
public class UserOpenPlatInfoEntity {
    private int uid; // uid INT(11) COMMENT ' 用戶號',
    private String openId; //  openId VARCHAR(255) COMMENT '平臺id',
    private int regType;// regType TINYINT(2) COMMENT '注冊平臺類型，1，手機(這個沒有)，2，',
    private String nickname;//nickname VARCHAR(50) COMMENT '平臺昵稱',
    private String avatar;// avatar VARCHAR(255) COMMENT '平臺頭像',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")//返回格式化的時間json
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//接收字符串json的時間，格式化為date類型
    private Date logTime;//logTime DATETIME DEFAULT NOW() COMMENT '記錄時間',

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getRegType() {
        return regType;
    }

    public void setRegType(int regType) {
        this.regType = regType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }
}
