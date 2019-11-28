package com.xd.executor.http.service.Impl;

import com.xd.executor.http.inf.Retryer;
import com.xd.executor.http.inf.Seter;
import org.apache.http.client.HttpClient;

/**
 * @ClassName: HttpClientSeter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 16:39
 */
public class HttpClientSeter implements Seter<HttpClient, Retryer>
{

    @Override
    public HttpClient setTer(HttpClient httpClient, Retryer o)
    {

        return httpClient;
    }
}
