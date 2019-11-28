package com.xd.executor.http.inf;

import com.xd.executor.http.client.exception.HttpProcessException;
import org.apache.http.client.HttpClient;

/**
 * @ClassName: ClientTask
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 15:20
 */
public interface ClientTask<T>
{
     T execute(HttpClient httpClient) throws HttpProcessException;
}
