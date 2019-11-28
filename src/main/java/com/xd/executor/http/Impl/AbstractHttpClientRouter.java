package com.xd.executor.http.Impl;

import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.inf.Retryer;
import com.xd.executor.http.inf.Router;
import org.apache.http.client.HttpClient;

/**
 * @ClassName: HttpClientRouter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 14:13
 */
public abstract class AbstractHttpClientRouter<T extends HttpClient> implements Router<HttpClient, Retryer, ClientMeta> {

    @Override
    public abstract HttpClient choose(Retryer retryer, ClientMeta meta);
}
