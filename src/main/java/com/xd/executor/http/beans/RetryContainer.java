package com.xd.executor.http.beans;

import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;

/**
 * @ClassName: RetryContainer
 * @Description: 承载异常和正常返回值的容器
 * @Author: xiedong
 * @Date: 2019/11/26 14:24
 */

public class RetryContainer extends RuntimeException{
    private Object result;
    private Exception[] exceptions;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(Exception...exceptions) {
        this.exceptions=exceptions;
    }

    public RetryContainer() {
    }

    public RetryContainer(Object result, Exception... exceptions)
    {
        this.result = result;
        this.exceptions = exceptions;
    }
    public RetryContainer( Exception... exceptions)
    {
        this.exceptions = exceptions;
    }
    //清空容器
    public void clear(){
        this.result = null;
        this.exceptions = null;
    }

    @Override
    public String toString() {
        return "RetryContainer{" +
                "result=" + result +
                ", exceptions=" + Arrays.toString(exceptions) +
                '}';
    }

}
