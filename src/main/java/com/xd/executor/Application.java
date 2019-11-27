package com.xd.executor;

import com.xd.executor.impl.CommonExecutors;
import com.xd.executor.impl.CommonRetryer;
import com.xd.executor.impl.DefaultRestTemplateRouter;
import com.xd.executor.inf.Retryer;
import com.xd.executor.inf.Task;
import com.xd.executor.test.RestTemplateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @ClassName: Application
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 18:26
 */

public class Application
{
    private Logger log = LoggerFactory.getLogger(Retryer.class);
    public static void main(String[] args)
    {
        HttpExecutors httpExecutors=new CommonExecutors();
        RestTemplate restTemplate = new RestTemplate();

        RestTemplateTask restTemplateTask = new RestTemplateTask();
        CommonRetryer commonRetryer = new CommonRetryer();
        Retryer.RetryMeta retryMeta = new Retryer.RetryMeta();
//        DefaultRestTemplateRouter defaultRestTemplateRouter = new DefaultRestTemplateRouter();
//        defaultRestTemplateRouter.setM2T(restTemplate,new HashMap());
        try
        {
            httpExecutors.executor(restTemplateTask,commonRetryer,retryMeta,restTemplate,null);

        } catch (Exception e) {
            System.out.println("异常信息:"+e);
        }
    }
}
