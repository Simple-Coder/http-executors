package com.xd.executor.http.client.builder;

import com.xd.executor.http.beans.ClientMeta;
import com.xd.executor.http.beans.RetryContainer;
import com.xd.executor.http.client.exception.HttpProcessException;
import com.xd.executor.http.inf.Retryer;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * @ClassName: HCB
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/27 22:36
 */
public class HCB extends HttpClientBuilder
{
    private Logger log = LoggerFactory.getLogger(getClass());

    public boolean isSetPool=false;//记录是否设置了连接池
    private HCB(){}
    public static HCB custom()
    {
        return new HCB();
    }
    public HCB put4HttpClient(ClientMeta meta)
    {
        return this.timeout(meta.getConnectTimeout(),meta.getSocketTimeout(),meta.getConnectTimeout())
                .pool(meta.getMatTotal(),meta.getDefaultMaxPerRoute())
                .proxy(meta.getProxyIP(),meta.getPort());
    }
    /**
     * 设置超时时间
     */
    private HCB timeout(int connectTimeout,int SocketTimeout,int connectRequestTimeout)
    {
        //配置请求的超时设置
        RequestConfig cfg = RequestConfig.custom()
                .setConnectionRequestTimeout(connectRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(SocketTimeout)
                .build();
        return (HCB) this.setDefaultRequestConfig(cfg);
    }

    /**
     * 连接池设置
     */
    private HCB pool(int maxTotal,int defaultMaxPerRoute){
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).build();
//                .register("https", ssls.getSSLCONNSF(sslpv)).build();
        //设置连接池大小
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(maxTotal);// Increase max total connection to $maxTotal
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);// Increase default max connection per route to $defaultMaxPerRoute
        isSetPool=true;
        return (HCB) this.setConnectionManager(connManager);
    }
    /**
     * 设置代理
     *
     * @param hostOrIP		代理host或者ip
     * @param port			代理端口
     * @return	返回当前对象
     */
    private HCB proxy(String hostOrIP, int port){
        if (hostOrIP!=null&&!"".equals(hostOrIP))
        {
            // 依次是代理地址，代理端口号，协议类型
            HttpHost proxy = new HttpHost(hostOrIP, port, "http");
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            return (HCB) this.setRoutePlanner(routePlanner);
        }
        return this;
    }
    /**
     * 重试（如果请求是幂等的，就再次尝试）
     *
     * @param tryTimes		重试次数
     * @return	返回当前对象
     */
    public HCB retry(final int tryTimes){
        return retry(tryTimes, false);
    }
    /**
     * 重试（如果请求是幂等的，就再次尝试）
     *
     * @param tryTimes						重试次数
     * @param retryWhenInterruptedIO		连接拒绝时，是否重试
     * @return	返回当前对象
     */
    public HCB retry(final int tryTimes, final boolean retryWhenInterruptedIO){
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= tryTimes) {// 如果已经重试了n次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    //return false;
                    return retryWhenInterruptedIO;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return true;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        this.setRetryHandler(httpRequestRetryHandler);
        return this;
    }

    public HCB setRetryer(RetryContainer container,int retryTimes)
    {
        if (container==null){
            return this;
        }
        log.info("正在设置client的重试机制...");
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler()
        {
            @Override
            public boolean retryRequest(IOException e, int executionCount, HttpContext context) {
                if (executionCount > retryTimes) {// 如果已经重试了n次，就放弃
                    log.info("重试已达最大【{}】次",retryTimes);
                    return false;
                }
                log.info("正在重试第【{}】次",executionCount);
                for (Exception exception : container.getExceptions()) {
                    log.info("正在验证当前异常：【{}】是否满足您设置的重试机制：【{}】",e.toString(),exception.toString());

                    if (exception.getClass().isAssignableFrom( e.getClass())){
                        log.info("当前异常：【{}】满足您设置的重试机制：【{}】",e.toString(),exception.toString());
                        return true;
                    }
                }
                log.info("当前异常：【{}】不满足您设置的重试机制：【{}】",e.toString());

                HttpClientContext clientContext = HttpClientContext .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    log.info("当前请求是幂等请求，正在重试：【{}】",e.toString());
                    return true;
                }
                return false;
            }
        };
        this.setRetryHandler(httpRequestRetryHandler);
        return this;
    }
}
