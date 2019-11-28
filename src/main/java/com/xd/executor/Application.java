package com.xd.executor;

import com.xd.executor.http.client.common.HttpMethods;
import com.xd.executor.http.service.HttpClientFactory;
import com.xd.executor.http.service.HttpClientTemplate;
import com.xd.executor.http.service.Impl.DefaultExecutors;
import com.xd.executor.http.service.Impl.DefaultRetryer;
import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.inf.Retryer;
import com.xd.executor.test.HttpClientTask;
import com.xd.executor.test.RestTemplateRestTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * @ClassName: Application
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 18:26
 */

public class Application {
    private Logger log = LoggerFactory.getLogger(Retryer.class);

    public static void main(String[] args) throws Exception {
        ArrayList<Son> sons = new ArrayList<>();
        sons.add(new Son("zhangsan1", "class1"));
        sons.add(new Son("zhangsan2", "class2"));
        Fathor fathor = new Fathor();
        fathor.setId("1001");
        fathor.setAge(50);
        fathor.setSons(sons);
        String url = "http://localhost:9181/flash-discount-online-app/flash-discount/testXml4";
        HttpExecutors httpExecutors =new DefaultExecutors();
        RestTemplate restTemplate = new RestTemplate();

        RestTemplateRestTask restTemplateTask = new RestTemplateRestTask();
        DefaultRetryer defaultRetryer = new DefaultRetryer();
        Retryer.RetryMeta retryMeta = new Retryer.RetryMeta();
        String s;

//        DefaultRestTemplateRouter defaultRestTemplateRouter = new DefaultRestTemplateRouter();
//        defaultRestTemplateRouter.setM2T(restTemplate,new HashMap());
        try {
//            httpExecutors.executor(restTemplateTask,true,restTemplate);
//            org.apache.http.conn.ConnectTimeoutException();
//            httpExecutors.executor(new HttpClientTask());
//            HttpClientFactory.newInstance().get("a");
//            s = new HttpClientTemplate().doExecute("application/xml", url, HttpMethods.POST, fathor);
            s = new HttpClientTemplate().doExecute("application/json", url, HttpMethods.POST, fathor);
        } catch (Exception e) {
            System.out.println("异常信息:" + e);
        }
//        RetryContainer container = new RetryContainer();
//        RuntimeException runtimeException = new RuntimeException();
//        Exception exception = new Exception();
//        container.setExceptions(ExceptionType.exception,ExceptionType.resourceAccessException);
//        System.out.println(container);
//        Exception[] exceptions = container.getExceptions();
//        for (Exception exception1 : exceptions) {
//            System.out.println(exception1);
//        }
    }
}
