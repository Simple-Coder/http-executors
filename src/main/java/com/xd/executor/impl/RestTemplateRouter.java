package com.xd.executor.impl;

import com.xd.executor.inf.Router;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName: RestTemplateRouter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 15:01
 */
public abstract class RestTemplateRouter<T extends RestTemplate> implements Router<RestTemplate, Map>
{
    @Override
    public abstract RestTemplate setM2T(RestTemplate restTemplate, Map map);
}
