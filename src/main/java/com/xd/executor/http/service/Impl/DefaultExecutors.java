
package com.xd.executor.http.service.Impl;

import com.xd.executor.HttpExecutors;
import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.beans.RetryContainer;
import com.xd.executor.http.inf.ClientTask;
import com.xd.executor.http.inf.Retryer;
import com.xd.executor.http.inf.Router;
import com.xd.executor.http.inf.RestTask;
import org.apache.http.client.HttpClient;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;


/**
 * @ClassName: CommonExecutors
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 16:05
 */

public class DefaultExecutors implements HttpExecutors {

    private Logger log = LoggerFactory.getLogger(getClass());

    //默认重试机制
    private static final DefaultRetryer DEFAULT_RETRYER = new DefaultRetryer();

    //restTemplate路由器
    private Router restTemplateRouter = new DefaultRestTemplateRouter();
    //HttpClient路由器
    private Router httpClientRouter = new DefaultHttpClientRouter();

    //
    private static final Exception[] defaultExceptions = new Exception[]{new Exception()};

    @Override
    public <T> T executor(RestTask<T> restTask, Retryer retryer, Retryer.RetryMeta meta, RestTemplate restTemplate) throws Exception {
        T result = null;
        //new一个承载异常和正常返回值的容器
        RetryContainer container = new RetryContainer();
        long overallStartTime = System.currentTimeMillis();

        log.info("[HttpExecutors--进入HttpExecutors,本次任务最大需要重试{}次]", null == meta ? 0 : meta.getMaxRetryCount());
        while (true) {
            long startTime = System.currentTimeMillis();
            try {

                //每次循环时先清空RetryException容器
                log.info("[HttpExecutors--清空RetryContainer容器]");
                container.clear();
                //执行我们具体的任务
                log.info("[HttpExecutors--开始执行任务]");
                result = restTask.execute(restTemplate);
                log.info("[HttpExecutors--任务执行完成]");
                //如果返回的结果正常，将结果放入容器
                container.setResult(result);
            } catch (Exception e) {
                log.info("[HttpExecutors--当前任务异常,信息已放入容器:【{}】" , e.toString());
                container.setExceptions(e);
            } finally {
                log.info("[HttpExecutors--本次HTTP请求结束]" + "\r\n结果:【{}】" + "\r\n异常信息【{}】" + "\r\n耗时:【{}】", result, container.getExceptions()
                        , (System.currentTimeMillis() - startTime));
                log.info("[HttpExecutors--判断本次HTTP请求是否触发重试]");
                //判断是否重试
                if (retryer != null && retryer.continueOrEnd(retryer, container, meta)) {
                    log.info("[HttpExecutors--重试机制生效，已等待:【{}】,开始第【{}】次重试]", meta.getInterval(), meta.getRetryCount() - 1);
                    //进行重试，此时已经睡了间隔时间
                    continue;
                } else {
                    //跳出重试
                    log.info("[HttpExecutors--HTTP请求{}，准备退出]", null == retryer ? "无重试机制" : null == meta || meta.getMaxRetryCount() == 0 ? "无重试次数" : meta.getRetryCount() > meta.getMaxRetryCount() ? "到达重试临界值" : "不满足重试要求");
                    if (null != container.getResult()) {
                        //跳出重试时任务执行没有异常
                        log.info("[HttpExecutors--退出--HTTP请求状态正常，返回结果可能未能达到预期]-[总耗时：{}]", System.currentTimeMillis() - overallStartTime);
                        return result;
                    } else {
                        //跳出重试时任务执行发生异常
                        log.error("[HttpExecutors--退出--HTTP请求状态异常]-[总耗时：【{}】ms]", System.currentTimeMillis() - overallStartTime);
                        if (container != null && container.getExceptions() != null && container.getExceptions().length > 0) {
                            throw container.getExceptions()[0];
                        }
                        else {
                            throw new RuntimeException("运行时异常");
                        }
                    }
                }
            }
        }
    }

    @Override
    public <T> T executor(RestTask<T> restTask, boolean needRetry, RestTemplate restTemplate) throws Exception {
        //如果needRetry为true，使用默认的重试机制，以及默认的重试元数据（重试3次，间隔3秒）
        return this.executor(restTask, needRetry ? DEFAULT_RETRYER : null, needRetry ? new Retryer.RetryMeta() : Retryer.RetryMeta.NO_RETRY, restTemplate);
    }

    @Override
    public <T> T executor(RestTask<T> restTask, RestTemplate restTemplate, int retryCount, int intervalTime) throws Exception {
        return this.executor(restTask, DEFAULT_RETRYER, new Retryer.RetryMeta(retryCount, intervalTime), restTemplate);
    }

    @Override
    public <T> T executor(RestTask<T> restTask, RestTemplate restTemplate, Retryer retryer, int retryCount, int intervalTime) throws Exception {
        return this.executor(restTask, retryer, new Retryer.RetryMeta(retryCount, intervalTime), restTemplate);
    }


    @Override
    public <T> T executor(ClientTask<T> clientTask, ClientMeta meta, Exception...exceptions) throws Exception {
        T result = null;
        log.info("[HttpExecutors--初始化RetryContainer容器]");
        if (exceptions==null||exceptions.length==0)
        {
           log.info("未指定异常重试，默认异常重试机制");
            exceptions=defaultExceptions;
        }else {
            log.info("已使用您设定的异常重试机制");
        }
        RetryContainer container = new RetryContainer(exceptions);

        log.info("[HttpExecutors--初始化HttpClient......]");
        HttpClient httpClient = (HttpClient)httpClientRouter.choose(container,meta);

        log.info("[HttpExecutors--进入HttpExecutors,本次任务最大需要重试{}次]", null == meta ? 0 : meta.getRetryTimes());

        long startTime = System.currentTimeMillis();
        try
        {
            log.info("[HttpExecutors--开始执行任务]");
            result = clientTask.execute(httpClient);
            log.info("[HttpExecutors--任务执行完成]");
        } catch (Exception e)
        {
            log.error("[HttpExecutors--退出--HTTP请求状态异常]-[总耗时：【{}】],异常信息：【{}】", System.currentTimeMillis() - startTime,e.toString());
            throw e;
        }
        return result;
    }

    @Override
    public <T> T executor(ClientTask<T> clientTask, int connectTimeout, int socketTimeout,int retryTimes, Exception... exceptions) throws Exception {
        return this.executor(clientTask,new ClientMeta(connectTimeout,socketTimeout,100,10,retryTimes),exceptions);
    }

    @Override
    public <T> T executor(ClientTask<T> clientTask) throws Exception {
        return this.executor(clientTask,new ClientMeta());
    }
}

