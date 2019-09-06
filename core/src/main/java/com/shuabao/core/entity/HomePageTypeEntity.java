package com.shuabao.core.entity;


public class HomePageTypeEntity extends BaseEntity {
    private int uid; // '用戶號',
    private int index = 1; //第几页,默认第一页
    private int size = 20;// 每页大小，,默认每页20
    private HomePageType type;//主页类型
    private int followUid;

    public enum HomePageType{
        FOLLOW,//关注
        NEW, //新人
        HOT//热门
    }

    public HomePageType getType() {
        return type;
    }
    public void setType(int type) {// 1，关注，新人，3，热门',,与数据库的setting_homepage_tag表一致
        switch (type) {
            case 1:
                this.type = HomePageType.FOLLOW;
                break;
            case 2:
                this.type = HomePageType.NEW;
                break;
            case 3:
                this.type = HomePageType.HOT;
                break;
            default:
                this.type = null;
        }
    }

    public int getStart() {
        return (index - 1) * size;
    }
    public int getEnd() {
        return (index * size) - 1;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFollowUid() {
        return followUid;
    }

    public void setFollowUid(int followUid) {
        this.followUid = followUid;
    }
}
