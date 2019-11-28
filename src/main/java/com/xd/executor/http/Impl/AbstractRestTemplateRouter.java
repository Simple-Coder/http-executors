package com.xd.executor.http.Impl;

import com.xd.executor.http.inf.Router;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName: RestTemplateRouter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 15:01
 */
public abstract class AbstractRestTemplateRouter<T extends RestTemplate> implements Router
{
    @Override
    public Object choose(Object retryer, Object meta) {
        return null;
    }
}
