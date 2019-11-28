package com.xd.executor.http.rest.Impl;

import com.xd.executor.http.beans.RetryContainer;
import com.xd.executor.http.inf.Retryer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;

/**
 * @ClassName: CommonRetryer
 * @Description: 默认重试机制，超时异常重试
 * @Author: xiedong
 * @Date: 2019/11/26 16:00
 */
public class DefaultRestRetryer implements Retryer
{
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean retry(RetryContainer e)
    {
        if (e.getException() instanceof ResourceAccessException)
        {
            log.info("[CommonRetryer--满足重试机制,当前信息]：【{}】",e.getException());
            return true;
        }
        log.info("[CommonRetryer--不满足重试机制，当前信息]:【{}】",e.getException());
        return false;
    }
}
