package com.xd.executor.http.beans;

/**
 * @ClassName: RetryContainer
 * @Description: 承载异常和正常返回值的容器
 * @Author: xiedong
 * @Date: 2019/11/26 14:24
 */

public class RetryContainer extends RuntimeException{
    private Object result;
    private Exception exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    //清空容器
    public void clear(){
        this.result = null;
        this.exception = null;
    }
}
