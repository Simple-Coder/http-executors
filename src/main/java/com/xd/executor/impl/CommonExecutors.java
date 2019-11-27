package com.xd.executor.impl;

import com.xd.executor.HttpExecutors;
import com.xd.executor.beans.RetryContainer;
import com.xd.executor.inf.Retryer;
import com.xd.executor.inf.Router;
import com.xd.executor.inf.Task;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import java.util.Map;

/**
 * @ClassName: CommonExecutors
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 16:05
 */
public class CommonExecutors implements HttpExecutors {

    //默认重试机制
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final CommonRetryer DEFAULT_RETRYER = new CommonRetryer();


    @Override
    public <T> T executor(Task<T> task, Retryer retryer, Retryer.RetryMeta meta, RestTemplate restTemplate) throws Exception {
        T result = null;
        //new一个承载异常和正常返回值的容器
        RetryContainer container = new RetryContainer();
        long overallStartTime=System.currentTimeMillis();

        log.info("[HttpExecutors--进入HttpExecutors-{},本次任务最大需要重试{}次]", overallStartTime,null == meta ? 0 : meta.getMaxRetryCount());
        while(true)
        {
            long startTime=System.currentTimeMillis();
            try
            {

                //每次循环时先清空RetryException容器
                log.info("[HttpExecutors--清空RetryContainer容器]");
                container.clear();
                //执行我们具体的任务
                log.info("[HttpExecutors--开始执行任务]");
                result = task.execute(restTemplate);
                log.info("[HttpExecutors--任务执行完成]");
                //如果返回的结果正常，将结果放入容器
                container.setResult(result);
            } catch (Exception e)
            {
                log.info("[HttpExecutors--当前任务异常,信息已放入容器:【{}】"+e);
                container.setException(e);
            } finally
            {
                log.info("[HttpExecutors--本次HTTP请求结束]" +"\r\n结果:【{}】" + "\r\n异常信息【{}】" + "\r\n耗时:【{}】", result,container.getException()
                        ,(System.currentTimeMillis()-startTime));
                log.info("[HttpExecutors--判断本次HTTP请求是否触发重试]");
                //判断是否重试
                if(retryer!=null&&retryer.continueOrEnd(retryer,container,meta)){
                    log.info("[HttpExecutors--重试机制生效，已等待:【{}】,开始第【{}】次重试]",meta.getInterval(), meta.getRetryCount() - 1);
                    //进行重试，此时已经睡了间隔时间
                    continue;
                }else {
                    //跳出重试
                    log.info("[HttpExecutors--HTTP请求{}，准备退出]",null == retryer ? "无重试机制" : null == meta || meta.getMaxRetryCount() == 0 ? "无重试次数" : meta.getRetryCount() > meta.getMaxRetryCount() ? "到达重试临界值" : "不满足重试要求");
                    if (null!=container.getResult()){
                        //跳出重试时任务执行没有异常
                        log.info("[HttpExecutors--退出--HTTP请求状态正常，返回结果可能未能达到预期]-[总耗时：{}]",System.currentTimeMillis() - overallStartTime);
                        return result;
                    }else
                        {
                        //跳出重试时任务执行发生异常
                            log.error("[HttpExecutors--退出--HTTP请求状态异常]-[总耗时：{}]", System.currentTimeMillis() - overallStartTime);
                        throw container.getException();
                    }
                }
            }
        }
    }

    @Override
    public <T> T executor(Task<T> task, boolean needRetry, RestTemplate restTemplate) throws Exception
    {
        //如果needRetry为true，使用默认的重试机制，以及默认的重试元数据（重试3次，间隔3秒）
        return this.executor(task,needRetry?DEFAULT_RETRYER:null,needRetry?new Retryer.RetryMeta():Retryer.RetryMeta.NO_RETRY,restTemplate);
    }
}
