package com.xd.executor.http.beans;

/**
 * @ClassName: TimeOutBean
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/28 14:50
 */
public class ClientMeta
{
    private int connectTimeout;//连接超时时间
    private int socketTimeout;//读取超时时间
    private int connectRequestTimeout;//获取连接时间
    private int matTotal;//总连接数限制为100个
    private int defaultMaxPerRoute;//每个路由最大创建10个链接
    private int retryTimes;//重试次数
    private String proxyIP;//代理IP
    private int port;//代理端口

    public ClientMeta() {
        this.connectTimeout = 1000;
        this.socketTimeout = 1000;
        this.matTotal = 100;
        this.defaultMaxPerRoute = 10;
        this.retryTimes = 3;
    }

    public ClientMeta(int connectTimeout, int socketTimeout, int matTotal, int defaultMaxPerRoute, int retryTimes)
    {
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.matTotal = matTotal;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        this.retryTimes = retryTimes;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getMatTotal() {
        return matTotal;
    }

    public void setMatTotal(int matTotal) {
        this.matTotal = matTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getConnectRequestTimeout() {
        return connectRequestTimeout;
    }

    public void setConnectRequestTimeout(int connectRequestTimeout) {
        this.connectRequestTimeout = connectRequestTimeout;
    }

    public String getProxyIP() {
        return proxyIP;
    }

    public void setProxyIP(String proxyIP) {
        this.proxyIP = proxyIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
