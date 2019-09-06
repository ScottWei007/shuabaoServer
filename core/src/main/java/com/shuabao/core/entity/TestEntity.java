package com.shuabao.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Scott Wei on 4/7/2018.
 */
public class TestEntity{
    private int id;
    private String name;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")//返回格式化的時間json
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//接收字符串json的時間，格式化為date類型
    private Date datetime;

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Date getDatetime() {
        return datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
