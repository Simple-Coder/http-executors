package com.xd.executor.inf;

import com.xd.executor.beans.RetryContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Interface: Retryer
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 14:21
 */
//函数式编程
@FunctionalInterface
public interface Retryer
{
     Logger log = LoggerFactory.getLogger(Retryer.class);
    /**
     * 标识：是否进行重试
     * @param e
     * @return
     */
    boolean retry(RetryContainer e);

    default boolean continueOrEnd(Retryer retryer, RetryContainer container, RetryMeta meta){
        //1.判断是否超出重试次数
        if (null==meta||meta.retryCount++>meta.maxRetryCount)
        {
            return false;
        }
        //2.判断是否符合重试机制的要求
        boolean flag = retryer.retry(container);
        if (flag)
        {
            try
            {
                //重试间隔通过sleep实现
                Thread.sleep(meta.interval);
            } catch (InterruptedException e) {
                log.info("[Retryer--设置间隔时间异常]:【{}】",e);
            }
        }
        return flag;
    }

    /**
     *
     */
    class RetryMeta {
        public static final RetryMeta NO_RETRY = new RetryMeta(0,0);
        //最大重试次数
        private int maxRetryCount;
        //重试间隔，单位为毫秒
        private int interval;
        //尝试次数
        private int retryCount;

        /**
         * 默认构造
         * 重试3次
         * 间隔3秒
         */
        public RetryMeta()
        {
            this.interval=3000;
            this.retryCount=1;
            this.maxRetryCount=3;
        }

        /**
         * 对外提供参数设定
         * @param maxRetryCount
         * @param interval
         */
        public RetryMeta(int maxRetryCount,int interval)
        {
            this.retryCount=1;
            this.interval=interval;
            this.maxRetryCount=maxRetryCount;
        }

        public int getMaxRetryCount() {
            return maxRetryCount;
        }

        public void setMaxRetryCount(int maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }
    }
}
