package com.xd.executor;

import com.xd.executor.http.inf.Retryer;
import com.xd.executor.http.inf.Task;
import org.springframework.web.client.RestTemplate;

/**
 * @Interface: HttpExecutors
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 13:57
 */
public interface HttpExecutors
{
    /**
     *
     * @param task          重试执行的任务
     * @param retryer       重试机制
     * @param meta          重试机制元数据
     * @param <T>           返回值类型
     * @param restTemplate  restTemplate
     * @return
     * @throws Exception
     */
    <T> T executor(Task<T> task, Retryer retryer, Retryer.RetryMeta meta, RestTemplate restTemplate) throws Exception;

    <T> T executor(Task<T> task, boolean needRetry, RestTemplate restTemplate)throws Exception;

    <T> T executor(Task<T> task, RestTemplate restTemplate,int retryCount,int intervalTime)throws Exception;

    <T> T executor(Task<T> task, RestTemplate restTemplate,Retryer retryer,int retryCount,int intervalTime)throws Exception;
}
