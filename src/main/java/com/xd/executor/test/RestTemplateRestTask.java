package com.xd.executor.test;

import com.xd.executor.http.inf.RestTask;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: Task
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/26 18:32
 */
public class RestTemplateRestTask implements RestTask<String> {
    @Override
    public String execute(RestTemplate restTemplate)
    {
        String xxx = restTemplate.getForObject("http://localhost:9181/flash-discount-online-app/flash-discount/testXml2", String.class);
        return xxx;
    }

}
