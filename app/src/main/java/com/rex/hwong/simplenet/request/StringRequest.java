package com.rex.hwong.simplenet.request;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.base.Response;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 下午2:07
 */

/**
 * 返回数据类型为String
 */
public class StringRequest extends Request<String>{
    /**
     * @param method   请求方式
     * @param url      请求URL
     * @param listener 请求回调
     */
    public StringRequest(HttpMethod method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }


    @Override
    public String parseResponse(Response response) {
        return new String(response.getRawData());
    }
}
