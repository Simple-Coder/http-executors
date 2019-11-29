package com.xd.executor.http.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.beans.RetryContainer;
import com.xd.executor.http.client.builder.HCB;
import com.xd.executor.http.client.common.HttpConfig;
import com.xd.executor.http.client.common.HttpHeader;
import com.xd.executor.http.client.common.HttpMethods;
import com.xd.executor.http.client.common.MsgHeader;
import com.xd.executor.utils.ObjectUtil;
import com.xd.executor.utils.XStreamUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @ClassName: HttpClientFactory
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 19:33
 */
public class HttpClientFactory {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static volatile HttpClientFactory instance = null;

    public static HttpClientFactory newInstance() {

        if (instance == null) {
            synchronized (HttpClientFactory.class) {
                if (instance == null) {
                    instance = new HttpClientFactory();
                }
            }
        }

        return instance;
    }

    //构造方法私有化
    private HttpClientFactory() {
    }

    public HttpClient getHttpClient() {
        return HCB.custom().put4HttpClient(new ClientMeta()).build();
    }

    public HttpClient getHttpClient(ClientMeta meta) {
        return HCB.custom().put4HttpClient(meta).build();
    }

    public HttpClient getHttpClient(int connectTimeout, int requestTimeout, int socketTimeout, int maxTotal, int maxPerRoute, int retryTimes,Exception...exceptions) {
        ClientMeta meta = new ClientMeta();
        meta.setConnectTimeout(connectTimeout);
        meta.setConnectRequestTimeout(requestTimeout);
        meta.setSocketTimeout(socketTimeout);
        meta.setMatTotal(maxTotal);
        meta.setDefaultMaxPerRoute(maxPerRoute);
        meta.setRetryTimes(retryTimes);
        RetryContainer container = new RetryContainer();
        container.setExceptions(exceptions);
//        return this.getHttpClient(meta,exceptions);
        return this.getHttpClient(meta,container);
    }
    public HttpClient getHttpClient(ClientMeta meta,Exception...exceptions)
    {
        RetryContainer container = new RetryContainer();
        container.setExceptions(exceptions);
        return this.getHttpClient(meta,container);
    }
    public HttpClient getHttpClient(ClientMeta meta,RetryContainer container)
    {
        return HCB.custom().put4HttpClient(meta).setRetryer(container,meta.getRetryTimes()).build();
    }
    public HttpConfig getHttpConfig4Map(Header[] headers, String url, HttpMethods method, Map params, HttpClient client) {
        return HttpConfig.custom()
                .headers(headers)
                .url(url)
                .map(params)
                .encoding("utf-8")
                .client(client)
                .method(method);
    }

    public HttpConfig getHttpConfig4Json(Header[] headers, String url, HttpMethods method, String json, HttpClient client) {
        return HttpConfig.custom()
                .headers(headers)
                .url(url)
                .json(json)
                .encoding("utf-8")
                .client(client)
                .method(method);
    }

    public HttpConfig getHttpConfigConverter(String contentType, String url, HttpMethods method, Object obj, HttpClient client, String charset) throws JsonProcessingException {
        HttpConfig cfg = HttpConfig.custom()
                .url(url)
                .encoding(charset)
                .client(client)
                .method(method)
                .headers(HttpHeader.custom().contentType(contentType).build());
//                .reqStr(reqStr);
        String xmlHeader = "";
        String reqStr = "";
        if (HttpHeader.Headers.APPLICATION_XML.equals(contentType)) {
            if (charset != null &&charset.equals("utf-8")) {
                xmlHeader = MsgHeader.xmlUTF8Header1+"\""+charset+"\"?>";;
            } else {
                xmlHeader = MsgHeader.xmlGBKHeader1+"\""+charset+"\"?>";
            }
            reqStr = xmlHeader.concat(XStreamUtil.bean2Xml(obj));
            cfg.reqStr(reqStr);
            return cfg;
        }
        if (HttpHeader.Headers.APPLICATION_JSON.equals(contentType)) {
            reqStr= ObjectUtil.mapper.writeValueAsString(obj);
            cfg.json(reqStr);
            return cfg;
        }
        return cfg;
    }
    public HttpConfig getHttpConfigConverter(Header[] headers, String url, HttpMethods method, Object obj, HttpClient client, String charset)  {
        HttpConfig cfg = HttpConfig.custom()
                .url(url)
                .encoding(charset)
                .client(client)
                .method(method)
                .headers(headers);
//                .reqStr(reqStr);
        String xmlHeader = "";
        String reqStr = "";
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
        if (contentType==null){
            log.info("请求头ContentType格式不正确！");
            return cfg;
        }
        if (HttpHeader.Headers.APPLICATION_XML.equals(contentType))
        {
            if ("GBK".equals(charset)) {

                xmlHeader = MsgHeader.xmlGBKHeader1+"\""+charset+"\"?>";
            } else {
                charset = "utf-8";
                xmlHeader = MsgHeader.xmlUTF8Header1+"\""+charset+"\"?>";;
            }
            reqStr = xmlHeader.concat(XStreamUtil.bean2Xml(obj));
            cfg.reqStr(reqStr);
            return cfg;
        }
        if (HttpHeader.Headers.APPLICATION_JSON.equals(contentType)) {
            try {
                reqStr= ObjectUtil.mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
            }
            cfg.json(reqStr);
            return cfg;
        }
        return cfg;
    }
}
