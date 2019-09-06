package com.shuabao.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shuabao.core.util.MD5Encode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Scott Wei on 4/15/2018.
 */
public class UserInfoEntity extends BaseEntity {
    private int uid; // uid INT(11) COMMENT '用戶號',
    private String nickname; // nickname VARCHAR(12) COMMENT '昵稱',
    private String avatar;//  avatar VARCHAR(120) COMMENT '頭像',
    private int sex; //  sex TINYINT(2) COMMENT '性別，1男，2女',
    private String pwd;//  pwd CHAR(32) COMMENT '密碼',
    private String salt;//   salt CHAR(6) COMMENT '密碼鹽',
    private int countryCode; // countryCode INT(6) COMMENT '國家代碼',
    private long phone; // phone BIGINT(20) COMMENT '手機號碼',
    private int regType; // regType TINYINT(2) COMMENT '注冊類型',
    private String addr;//   addr VARCHAR(10) COMMENT '地址',
    private String sign;// sign VARCHAR(10) COMMENT '个性签名',
    private int level; //`level` TINYINT(2) COMMENT '账户等級',
    private int height; //`height` INT(11) DEFAULT NULL COMMENT '身高',
    private int education; // `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
    private int marriage; // `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',,
    private String income; // `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
    private int expectedtime; // `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内',
    private int viplevel; //`viplevel` TINYINT(2) COMMENT '消费等級',
    private long bonds;// bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
    private long gold;// gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
    private int price; // price INT(11) DEFAULT 0 COMMENT '聊天价格',
    private int preview; //preview TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
    private int status; //   `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
    private String token;//   token CHAR(32) DEFAULT '' COMMENT '登陸token',
    private String birth;// birth  VARCHAR(20) COMMENT '生日',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastlogintime;//  lastlogintime DATETIME COMMENT '上次登錄時間',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastlogouttime;//  lastlogouttime DATETIME COMMENT '上次退出时间',
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;//  logTime DATETIME DEFAULT NOW() COMMENT '創建時間',

    private static final String tableName = "user_info_";
    public static String getTableName(int uid){
        return tableName + (uid % 10);
    }
    public String getTableName() {//sql語句的${tableName}參數用到
        return getTableName(this.uid);
    }

    public void buildPasswd() {
        this.salt = RandomStringUtils.randomAlphabetic(6);
        this.pwd = MD5Encode.encodePWD(this.salt, this.pwd);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPreview() {
        return preview;
    }

    public void setPreview(int preview) {
        this.preview = preview;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getViplevel() {
        return viplevel;
    }

    public void setViplevel(int viplevel) {
        this.viplevel = viplevel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getRegType() {
        return regType;
    }

    public void setRegType(int regType) {
        this.regType = regType;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getBonds() {
        return bonds;
    }

    public void setBonds(long bonds) {
        this.bonds = bonds;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public Date getLastlogouttime() {
        return lastlogouttime;
    }

    public void setLastlogouttime(Date lastlogouttime) {
        this.lastlogouttime = lastlogouttime;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getMarriage() {
        return marriage;
    }

    public void setMarriage(int marriage) {
        this.marriage = marriage;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public int getExpectedtime() {
        return expectedtime;
    }

    public void setExpectedtime(int expectedtime) {
        this.expectedtime = expectedtime;
    }
}
