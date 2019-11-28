package com.xd.executor.test;

import com.xd.executor.http.client.builder.HCB;
import com.xd.executor.http.client.common.HttpConfig;
import com.xd.executor.http.client.common.HttpHeader;
import com.xd.executor.http.client.exception.HttpProcessException;
import com.xd.executor.http.client.util.HttpClientUtil;
import com.xd.executor.http.inf.ClientTask;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: HttpClientTask
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 18:15
 */
public class HttpClientTask implements ClientTask<String> {

    @Override
    public String execute(HttpClient httpClient) throws HttpProcessException {
        String url ="http://localhost:9181/flash-discount-online-app/flash-discount/testXml2";
        //插件式配置Header（各种header信息、自定义header）
        Header[] headers 	= HttpHeader.custom()
                .userAgent("javacl")
                .other("customer", "自定义")
                .build();

        //插件式配置生成HttpClient时所需参数（超时、连接池、ssl、重试）
        HCB hcb 				= HCB.custom();

        HttpClient client = hcb.build();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        //插件式配置请求参数（网址、请求参数、编码、client）
        HttpConfig config = HttpConfig.custom()
                .headers(headers)	//设置headers，不需要时则无需设置
                .timeout(1000) 		//超时
                .url(url)           //设置请求的url
                .map(map)			//设置请求参数，没有则无需设置
                .encoding("utf-8")  //设置请求和返回编码，默认就是Charset.defaultCharset()
                .client(httpClient)     //如果只是简单使用，无需设置，会自动获取默认的一个client对象
                //.inenc("utf-8")   //设置请求编码，如果请求返回一直，不需要再单独设置
                //.inenc("utf-8")   //设置返回编码，如果请求返回一直，不需要再单独设置
                //.json("json字符串") //json方式请求的话，就不用设置map方法，当然二者可以共用。
                //.context(HttpCookies.custom().getContext())      //设置cookie，用于完成携带cookie的操作
                //.out(new FileOutputStream("保存地址"))              //下载的话，设置这个方法,否则不要设置
                //.files(new String[]{"d:/1.txt","d:/2.txt"})      //上传的话，传递文件路径，一般还需map配置，设置服务器保存路径
                ;


        //使用方式：
        String result1 = HttpClientUtil.get(config);    //get请求
        System.out.println(result1);
        return result1;
    }
}
