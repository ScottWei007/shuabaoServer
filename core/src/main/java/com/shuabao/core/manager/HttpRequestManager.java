package com.shuabao.core.manager;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 4/24/2018.
 */
public class HttpRequestManager {
    protected final Logger log = LoggerFactory.getLogger(HttpRequestManager.class);
    //线程安全的单例
    private static volatile HttpRequestManager singleton;
    private final static Lock lock = new ReentrantLock();

    public static HttpRequestManager getSingleton () {
        if (singleton == null) {
            try {
                lock.lock();
                if(singleton == null) {
                    singleton = new HttpRequestManager();
                }
            }finally {
                lock.unlock();
            }
        }
        return singleton;
    }

    private HttpRequestManager() {
        //初始化httpclient连接池
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setMaxTotal(200);
        pool.setDefaultMaxPerRoute(20);
        this.client = HttpClients.custom().setConnectionManager(pool).build();
    }

    private final CloseableHttpClient client;

    //单例里面的方法
    public Optional<String> post(String url, Map<String,Object> params){
        if(StringUtils.isEmpty(url) || Objects.isNull(params)){
            return Optional.empty();
        }
        try {
            HttpPost httpPost = new HttpPost(url); //初始化httpPost
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(getNameValuePairs(params),"UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            setHeader(httpPost);
            return Optional.ofNullable(getResult(url, httpPost));
        }catch (Exception e) {
            log.error(">>>>>>>>>post異常：>>>>>>>>>url:{},cause:{}",url , e.getMessage());
        }
        return Optional.empty();
    }

    //get
    public Optional<String> get(String url, Map<String,Object> params) {
        if(StringUtils.isEmpty(url) || Objects.isNull(params)) {
            return Optional.empty();
        }
        try {
            URIBuilder urlBuilder = new URIBuilder(url);
            urlBuilder.setParameters(getNameValuePairs(params));
            HttpGet httpget = new HttpGet(urlBuilder.build()); //初始化HttpGet
            setHeader(httpget);
            return Optional.ofNullable(getResult(url, httpget));
        } catch (URISyntaxException e) {
            log.error(">>>>>>>>>get请求异常：>>>>>>>>>url:{},cause:{}",url , e.getMessage());
            return Optional.empty();
        }
    }

    private final void setHeader(HttpRequestBase httpRequestBase) {
        httpRequestBase.setHeader(HTTP.CONTENT_TYPE , "application/x-www-form-urlencoded;charset=utf-8");
        httpRequestBase.setHeader("Accept", "application/json; charset=utf-8" );
        RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();//设置请求和传输超时时间
        httpRequestBase.setConfig(config);
    }


    private final String getResult(String url, HttpRequestBase httpRequestBase){
        CloseableHttpResponse response = null;
        try {
            response = this.client.execute(httpRequestBase);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            } else {
                log.info(">>>>>>>>>Http返回异常：>>>>>>>>>url:{},msg:{}",url, response.getEntity());
            }
        }catch (Exception e) {
            log.error(">>>>>>>>>Http请求异常：>>>>>>>>>url:{},cause:{}",url , e.getMessage());
        }finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(">>>>>>>>>Http请求异常：>>>>>>>>>url:{},cause:{}",url , e.getMessage());
                }
            }
        }
        return null;
    }

    private final List<NameValuePair> getNameValuePairs(Map<String, Object> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return nameValuePairs;
    }
}
