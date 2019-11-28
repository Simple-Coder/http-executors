package com.xd.executor.http.inf;

/**
 * @Interface: Router
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 14:52
 */
public interface Router<T,R,M>
{
    T choose(R retryer,M meta);
}
