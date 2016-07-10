package com.rex.hwong.simplenet.request;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.base.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/9
 * @time 下午11:44
 */

/**
 * 返回的数据类型为Json的请求
 */
public class JsonResquest extends Request<JSONObject> {
    /**
     * @param method   请求方式
     * @param url      请求URL
     * @param listener 请求回调
     */
    public JsonResquest(HttpMethod method, String url, RequestListener<JSONObject> listener) {
        super(method, url, listener);
    }

    /**
     * 将Response的结果转化为JSONObject
     * @param response
     * @return
     */
    @Override
    public JSONObject parseResponse(Response response) {
        String jsonString = new String(response.getRawData());
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
