package com.xd.executor.http.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xd.executor.http.client.common.HttpConfig;
import com.xd.executor.http.client.common.HttpMethods;
import com.xd.executor.http.client.util.HttpClientUtil;
import org.apache.http.client.HttpClient;
import org.springframework.util.Assert;

/**
 * @ClassName: HttpClientTemplate
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 21:40
 */
public class HttpClientTemplate {

    /*public <T> T doExecute(String contentType,String url, HttpMethods method, Object obj,Class<T> responseType){
        HttpClientFactory factory = HttpClientFactory.newInstance();
        HttpClient httpClient = factory.getHttpClient();
        try {
            HttpConfig cfg = factory.getHttpConfigConverter(contentType, url, method, obj, httpClient, "utf-8");
            String send = HttpClientUtil.send(cfg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
    public String doExecute(String contentType,String url, HttpMethods method, Object obj)
    {
        Assert.notNull(contentType, "contentType is required");
        Assert.notNull(url, "URI is required");

        HttpClientFactory factory = HttpClientFactory.newInstance();
        HttpClient httpClient = factory.getHttpClient();
        try {
            HttpConfig cfg = factory.getHttpConfigConverter(contentType, url, method, obj, httpClient, "utf-8");
           return HttpClientUtil.send(cfg);
        } catch (Exception e)
        {
            return "";
        }
    }
}
