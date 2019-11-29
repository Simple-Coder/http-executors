package com.xd.executor;

import com.xd.executor.http.client.common.HttpHeader;
import com.xd.executor.http.client.common.HttpMethods;
import com.xd.executor.http.inf.Retryer;
import com.xd.executor.http.service.HttpClientTemplate;
import com.xd.executor.http.service.Impl.DefaultExecutors;
import com.xd.executor.http.service.Impl.DefaultRetryer;
import com.xd.executor.test.RestTemplateRestTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
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
//            Fathor s1 = new HttpClientTemplate().doExecute("application/json", url, HttpMethods.POST, fathor, Fathor.class);
//            Fathor s1 = new HttpClientTemplate(60000,60000,60000,100,10,2).doExecute(HttpHeader.custom().contentType("application/xml").build(), url, HttpMethods.POST, fathor, Fathor.class);
//            Fathor s1 = new HttpClientTemplate(60000,60000,60000,100,10,2).doExecute(HttpHeader.custom().contentType("application/json").build(), url, HttpMethods.POST, fathor, Fathor.class);
//            Fathor s1 = new HttpClientTemplate(60000,60000,60000,100,10,2,new ResourceAccessException("超时异常")).doExecute(HttpHeader.custom().contentType("application/json").build(), url, HttpMethods.POST, fathor, Fathor.class);
            Fathor s1 = new HttpClientTemplate(60000,60000,60000,100,10,2,new ConnectException()).doExecute(HttpHeader.custom().contentType("application/json").build(), url, HttpMethods.POST, fathor, Fathor.class);

            System.out.println("返回的内容："+s1);
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
