package com.xd.executor.http.rest.Impl;

import com.xd.executor.http.inf.Router;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName: RestTemplateRouter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 15:01
 */
public abstract class RestTemplateRouter<T extends RestTemplate> implements Router<RestTemplate, String>
{
    @Override
    public abstract RestTemplate choose(String  key) ;
}
