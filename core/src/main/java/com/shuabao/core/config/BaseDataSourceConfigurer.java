package com.shuabao.core.config;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.ArrayList;

/**
 * Created by Scott Wei on 4/7/2018.
 */
public class BaseDataSourceConfigurer implements Configurer<DruidDataSource> {
    @Override
    public DruidDataSource configure (DruidDataSource druidDataSource) {
        druidDataSource.setMaxActive(50);//連接池最大活躍數
        druidDataSource.setInitialSize(3);//初始化數
        druidDataSource.setMinIdle(3);//最小空閑數
        druidDataSource.setMaxWait(60000);//獲取鏈接最大超時時間.毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);//间隔多久才进行一次检测需要关闭的空闲连接
        druidDataSource.setMinEvictableIdleTimeMillis(300000);//一个连接在池中最小空闲的时间
        druidDataSource.setValidationQuery("SELECT 'z'");//測試sql
        druidDataSource.setTestWhileIdle(true);//開啓測試
        druidDataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效,做了这个配置会降低性能
        druidDataSource.setTestOnReturn(false);//归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        ArrayList<String> sql = new ArrayList<String>();
        sql.add("SET NAMES utf8mb4");
        druidDataSource.setConnectionInitSqls(sql);//物理连接初始化的时候执行的sql
        return druidDataSource;
    }
}
