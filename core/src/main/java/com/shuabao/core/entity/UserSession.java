package com.shuabao.core.entity;

/**
 * Created by Scott Wei on 4/17/2018.
 */
public class UserSession {//注意：不能随便增减字段，不然会报错
    private final static String initString = "";//redis不能存null,String默认“”
    private final static String initNumber0 = "0";//redis不能存null，数字默认“0”
    private String uid = initNumber0;
    private String nickname = initString;
    private String avatar = initString;//'頭像',
    private String sex = initNumber0;// '性別
    private String addr = initString;//'地址',
    private String height = initNumber0; //'身高',
    private String education = initNumber0; //'学历, 1高中及以下、2专科、3本科、4研究、5博士',
    private String marriage = initNumber0; // '婚姻状况,1单身，2离异，3丧偶',
    private String income = initString; // '月收入',
    private String expectedtime = initNumber0; //'期望结婚时间,1,半年内、2,一年内，3,两年内',
    private String birth = initString;//  '生日',
    private String phone = initNumber0; //'手機號碼',
    private String bonds = initNumber0;//'券幣',
    private String gold = initNumber0;//'券幣',
    private String status = initNumber0; //'賬號狀態，1，正常，2異常',
    private String level = initNumber0; //'账号等級',
    private String viplevel = initNumber0; //'消费等級',
    private String price = initNumber0; //'聊天价格',
    private String preview = initNumber0; //'是否免费预览,1是，2，否',
    private String sign = initString;// '个性签名',
    private String token = initString;//用户token
    private String onlive = initNumber0; //'在线狀態，1，在，2不在',用户表没有
    private String host = initString;//,用户登录ip,用户表没有
    private String port = initNumber0;//,用户登录端口,用户表没有


    public String getOnlive() {
        return onlive;
    }

    public void setOnlive(String onlive) {
        this.onlive = onlive;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getBonds() {
        return bonds;
    }

    public void setBonds(String bonds) {
        this.bonds = bonds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpectedtime() {
        return expectedtime;
    }

    public void setExpectedtime(String expectedtime) {
        this.expectedtime = expectedtime;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
