package com.shuabao.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Scott Wei on 4/17/2018.
 */
public class UserChangeLogEntity {
    private int uid; // INT(11) NOT NULL COMMENT '用戶號',
    private long num; //  INT(11) NOT NULL COMMENT '数值',
    private int logtype;//TINYINT(2) DEFAULT NULL COMMENT '记录類型:1,gold;2,bonds;',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")//返回格式化的時間json
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//接收字符串json的時間，格式化為date類型
    private Date logTime;//logTime DATETIME DEFAULT NOW() COMMENT '記錄時間',

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int getLogtype() {
        return logtype;
    }

    public void setLogtype(int logtype) {
        this.logtype = logtype;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }
}
