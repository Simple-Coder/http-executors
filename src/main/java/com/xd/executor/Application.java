package com.xd.executor;

import com.xd.executor.http.rest.Impl.DefaultRestExecutors;
import com.xd.executor.http.rest.Impl.DefaultRestRetryer;
import com.xd.executor.http.inf.Retryer;
import com.xd.executor.test.RestTemplateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

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
        HttpExecutors httpExecutors =new DefaultRestExecutors();
        RestTemplate restTemplate = new RestTemplate();

        RestTemplateTask restTemplateTask = new RestTemplateTask();
        DefaultRestRetryer defaultRestRetryer = new DefaultRestRetryer();
        Retryer.RetryMeta retryMeta = new Retryer.RetryMeta();
//        DefaultRestTemplateRouter defaultRestTemplateRouter = new DefaultRestTemplateRouter();
//        defaultRestTemplateRouter.setM2T(restTemplate,new HashMap());
        try
        {
//            httpRestExecutors.executor((RestTemplate r)->r.getForObject("x",String.class),(boolean container)->true,);
        } catch (Exception e) {
            System.out.println("异常信息:"+e);
        }
    }
}
