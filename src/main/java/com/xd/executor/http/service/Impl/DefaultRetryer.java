package com.xd.executor.http.service.Impl;

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
public class DefaultRetryer implements Retryer {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean retry(RetryContainer e) {
        if (e == null) {
            log.info("承载异常与正常返回值容器为null，使用无重试机制");
            return false;
        } else
        {
            for (Exception temp : e.getExceptions())
            {
                if (temp instanceof ResourceAccessException)
                {
                    log.info("[CommonRetryer--满足重试机制,当前信息]：【{}】", temp.toString());
                    return true;
                }
                log.info("[CommonRetryer--不满足重试机制，当前信息]:【{}】", temp.toString());
            }
        return false;
        }
    }
}
