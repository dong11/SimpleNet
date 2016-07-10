package com.rex.hwong.simplenet.config;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 下午2:16
 */

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * 这是针对于使用HttpUrlStack执行请求时为https请求设置的SSLSocketFactory和HostnameVerifier的配置类,
 */
public class HttpUrlConnConfig extends HttpConfig{
    private static HttpUrlConnConfig sConfig = new HttpUrlConnConfig();

    private SSLSocketFactory mSSLSocketFactory = null;
    private HostnameVerifier mHostnameVerifier = null;

    private HttpUrlConnConfig(){
    }

    public static HttpUrlConnConfig getConfig(){
        return sConfig;
    }

    /**
     * 配置https请求的SSLSocketFactory与HostnameVerifier
     *
     * @param sslSocketFactory
     * @param hostnameVerifier
     */
    public void setHttpsConfig(SSLSocketFactory sslSocketFactory,
                               HostnameVerifier hostnameVerifier) {
        mSSLSocketFactory = sslSocketFactory;
        mHostnameVerifier = hostnameVerifier;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSSLSocketFactory;
    }
}
