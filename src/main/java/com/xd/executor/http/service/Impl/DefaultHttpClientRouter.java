package com.xd.executor.http.service.Impl;

import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.beans.RetryContainer;
import com.xd.executor.http.client.builder.HCB;
import org.apache.http.client.HttpClient;

/**
 * @ClassName: DefaultHttpClientRouter
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 14:14
 */
public class DefaultHttpClientRouter extends AbstractHttpClientRouter
{
    public HttpClient choose(String key)
    {
        switch (key)
        {
            case "DEFAULT":
                ClientMeta meta = new ClientMeta();
                return HCB.custom().put4HttpClient(meta).build();
            default:
                return null;
        }
    }

    @Override
    public HttpClient choose(RetryContainer container, ClientMeta meta)
    {
        return HCB.custom().put4HttpClient(meta).setRetryer(container,meta.getRetryTimes()).build();
    }
}
