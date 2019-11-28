package com.xd.executor.http.inf;

import org.apache.http.client.HttpClient;

/**
 * @ClassName: ClientTask
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 15:20
 */
public interface ClientTask<T>
{
    <T> T execute(HttpClient httpClient);
}
