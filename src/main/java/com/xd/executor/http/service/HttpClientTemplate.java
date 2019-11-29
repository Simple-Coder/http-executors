package com.xd.executor.http.service;

import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.beans.RetryContainer;
import com.xd.executor.http.client.builder.HCB;
import com.xd.executor.http.client.common.HttpConfig;
import com.xd.executor.http.client.common.HttpHeader;
import com.xd.executor.http.client.common.HttpMethods;
import com.xd.executor.http.client.util.HttpClientUtil;
import com.xd.executor.utils.ObjectUtil;
import com.xd.executor.utils.XStreamUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @ClassName: HttpClientTemplate
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 21:40
 */
public class HttpClientTemplate
{
    private Logger log = LoggerFactory.getLogger(getClass());
    private HttpClient httpClient;
    private HttpConfig cfg;
    private HttpClientFactory factory = HttpClientFactory.newInstance();

    public HttpClientTemplate(){
        if (httpClient==null)
        {
            log.info("You don`t have settings for httpclient,that will use default httpclient");
            this.httpClient=this.factory.getHttpClient();
        }
    }
    public HttpClientTemplate(int connectTimeout, int requestTimeout, int socketTimeout, int maxTotal, int maxPerRoute, int retryTimes,Exception...exceptions ){
        ClientMeta meta = new ClientMeta();
        meta.setConnectTimeout(connectTimeout);
        meta.setConnectRequestTimeout(requestTimeout);
        meta.setSocketTimeout(socketTimeout);
        meta.setMatTotal(maxTotal);
        meta.setDefaultMaxPerRoute(maxPerRoute);
        meta.setRetryTimes(retryTimes);
        RetryContainer container = new RetryContainer();
        container.setExceptions(exceptions);
        log.info("The httpclient has used your settings,that will use default httpclient");
        this.httpClient=factory.getHttpClient(meta,container);
    }
    public HttpClientTemplate(ClientMeta meta,Exception...exceptions ){
        RetryContainer container = new RetryContainer();
        container.setExceptions(exceptions);
        log.info("The httpclient has used your settings,that will use default httpclient");
        this.httpClient=this.factory.getHttpClient(meta,container);
    }

    public void setCfg(String contentType, String url, HttpMethods method, Object obj,String chartSet) {
        this.cfg = this.factory.getHttpConfigConverter(HttpHeader.custom().contentType(contentType).build()
                                                ,url,method,obj,this.httpClient,chartSet);
    }

    public <T> T doExecute(Header[] headers, String url, HttpMethods method, Object param, Class<T> responseType){
        Assert.notNull(headers, "Headers is required");
        Assert.notNull(url, "URI is required");
        try
        {
            cfg = factory.getHttpConfigConverter(headers, url, method, param, httpClient, "utf-8");
            return this.convert(headers,responseType,HttpClientUtil.send(cfg));
        } catch (Exception e)
        {
            log.info("execute exception info:【{}】",e.toString());
            return null;
        }
    }

    public <T> T doExecute(String contentType, String url, HttpMethods method, Object param, Class<T> responseType){
        Assert.notNull(contentType, "contentType is required");
        Assert.notNull(url, "URI is required");
        try
        {
            cfg = factory.getHttpConfigConverter(contentType, url, method, param, httpClient, "utf-8");
            return this.convert(contentType,responseType,HttpClientUtil.send(cfg));
        } catch (Exception e)
        {
            log.info("execute exception info:【{}】",e.toString());
            return null;
        }
    }
    private <T> T convert(String contentType,Class<T> responseType,String resData) throws Exception {
          if (HttpHeader.Headers.APPLICATION_XML.equals(contentType)){
              return (T)XStreamUtil.xml2Bean(resData,responseType);
          }
          else if (HttpHeader.Headers.APPLICATION_JSON.equals(contentType)){
              return (T) ObjectUtil.mapper.readValue(resData,responseType);
          }else {
              throw new Exception("The result:"+resData+" can`t transfer to"+responseType+" by your contenType:"+ contentType);
          }
    }
    private <T> T convert(Header[] headers,Class<T> responseType,String resData) throws Exception {
        String contentType=null;
        Assert.notNull(headers, "Headers is required");

        for (Header header : headers) {
            String name = header.getName();
            String value = header.getValue();
            if ("Content-Type".equals(name)){
                contentType=value;
                break;
            }
        }
        return this.convert(contentType,responseType,resData);

    }
}
