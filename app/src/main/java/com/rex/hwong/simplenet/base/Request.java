package com.rex.hwong.simplenet.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/9
 * @time 下午4:00
 */

/**
 * 网络请求类
 * @param <T> T为请求返回的数据类型
 */
public abstract class Request<T> implements Comparable<Request<T>> {

    //默认编码方式
    private static final String DEFAULT_PARAMS_ENCODING = "utf-8";
    // Default Content-type
    public final static String HEADER_CONTENT_TYPE = "Content-Type";
    //请求序列号
    protected int mSerialNum = 0;
    //优先级默认设置为Normal
    protected Priority mPriority = Priority.NORMAL;
    //是否取消该请求
    protected boolean isCancel = false;
    //请求是否应该缓存
    private boolean mShouldCache = true;
    //请求Listener
    protected RequestListener<T> mRequestListener;
    //请求的URL
    private String mURL = "";
    //请求的方法
    HttpMethod mHttpMethod = HttpMethod.GET;
    //请求的header
    private Map<String, String> mHeaders = new HashMap<>();
    //请求参数
    private Map<String, String> mBodyParams = new HashMap<>();

    /**
     *
     * @param method 请求方式
     * @param url 请求URL
     * @param listener 请求回调
     */
    public Request(HttpMethod method, String url, RequestListener<T> listener) {
        mHttpMethod = method;
        mURL = url;
        mRequestListener = listener;
    }

    /**
     * 从原生的网络请求中解析结果，自雷必须覆写
     * @param response
     * @return
     */
    public abstract T parseResponse(Response response);

    /**
     * 处理Response，该方法需要运行在UI线程
     * @param response
     */
    public final void deliveryResponse(Response response) {
        T result = parseResponse(response);
        if(mRequestListener != null){
            int stCode = response != null? response.getStatusCode() : -1;
            String errMsg = response != null? response.getMessage() : "unknown error";
            mRequestListener.onComplete(stCode, result, errMsg);
        }
    }

    protected String getParamsEncoding(){
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType(){
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public Map<String, String> getParams(){
        return mBodyParams;
    }

    /**
     * 返回POST或者PUT请求时的Body参数字节数组
     * @return
     */
    public byte[] getBody(){
        Map<String, String> params = getBodyParams();
        if(params != null && params.size() > 0){
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding){
        StringBuffer encodedParams = new StringBuffer();
        try {
            for(Map.Entry<String, String> entry : params.entrySet()){
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append("=");
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append("&");
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, e);
        }
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isShouldCache() {
        return mShouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        mShouldCache = shouldCache;
    }

    public Map<String, String> getBodyParams() {
        return mBodyParams;
    }

    public void setBodyParams(Map<String, String> bodyParams) {
        mBodyParams = bodyParams;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public void setSerialNum(int serialNum) {
        mSerialNum = serialNum;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        mURL = URL;
    }

    public static String getDefaultParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public RequestListener<T> getRequestListener() {
        return mRequestListener;
    }

    public void setRequestListener(RequestListener<T> requestListener) {
        mRequestListener = requestListener;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        mHttpMethod = httpMethod;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public boolean isHttps() {
        return mURL.startsWith("https");
    }

    /**
     * 用于对请求排序处理，根据优先级和加入到队列的序号进行排序
     * @param another
     * @return
     */
    @Override
    public int compareTo(Request<T> another) {
        Priority myPriority = this.mPriority;
        Priority anotherPriority = another.getPriority();
        return myPriority.equals(another)? this.getSerialNum() - another.getSerialNum()
                : myPriority.ordinal() - anotherPriority.ordinal();
    }

    /**
     * HTTP请求方法枚举
     */
    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private String mHttpMethod;

        private HttpMethod(String httpMethod){
            mHttpMethod = httpMethod;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    /**
     * HTTP优先级枚举
     */
    public static enum Priority{
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public static interface RequestListener<T> {
        void onComplete(int stCode, T response, String errMsg);
    }
}
