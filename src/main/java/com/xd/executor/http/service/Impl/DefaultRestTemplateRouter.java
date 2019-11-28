package com.xd.executor.http.service.Impl;

import com.xd.executor.utils.ReflexObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName: DynamicRestTemplateRouter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 15:05
 */
public class DefaultRestTemplateRouter extends AbstractRestTemplateRouter {
//    private static Logger log = Logger.getLogger(DefaultRestTemplateRouter.class);
    private Logger log = LoggerFactory.getLogger(getClass());


    public RestTemplate setM2T(RestTemplate restTemplate, Map map)
    {
        HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory)restTemplate.getRequestFactory();
//        SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();

        //连接上服务器(握手成功)的时间，超出抛出connect timeout
        if (map==null||CollectionUtils.isEmpty((Map<?, ?>) map.get("connectTimeOut"))){
            log.info("连接超时时间未设置，默认为：【{}】",ReflexObjectUtil.getValueByKey(requestFactory, "connectTimeout"));
        }else {
            requestFactory.setConnectTimeout(Integer.parseInt(map.get("connectTimeOut")+""));
            log.info("连接超时时间设置为：【{}】",Integer.parseInt(map.get("connectTimeOut")+""));
        }


        //服务器返回数据(response)的时间，超过抛出read timeout
        if (map==null||CollectionUtils.isEmpty((Map<?, ?>) map.get("readTimeOut"))){
            log.info("接受响应超时时间未设置，默认为：【{}】",ReflexObjectUtil.getValueByKey(requestFactory, "readTimeout"));
        }
        else {
            requestFactory.setReadTimeout(Integer.parseInt(map.get("readTimeOut")+""));
            log.info("接收响应超时时间设置为：【{}】",Integer.parseInt(map.get("readTimeOut")+""));
        }
        return restTemplate;
    }
}
