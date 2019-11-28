package com.xd.executor.http.enums;

import org.springframework.web.client.ResourceAccessException;

/**
 * @ClassName: ExceptionType
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 17:30
 */
public interface ExceptionType {
    public static final Exception exception = new Exception();
    public static final ResourceAccessException resourceAccessException= new ResourceAccessException("超时异常");

}
