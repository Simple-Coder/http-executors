package com.xd.executor.inf;

import org.apache.http.client.HttpClient;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: Task
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 14:49
 */
public interface Task<T>
{
    T execute(RestTemplate restTemplate);
}
