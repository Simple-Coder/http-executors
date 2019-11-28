package com.xd.executor.http.inf;

import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: Task
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 14:49
 */
public interface RestTask<T>
{
    T execute(RestTemplate restTemplate);
}
