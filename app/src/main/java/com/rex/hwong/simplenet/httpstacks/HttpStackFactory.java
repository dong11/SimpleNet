package com.rex.hwong.simplenet.httpstacks;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 上午12:04
 */

import android.os.Build;

/**
 * 根据API版本选择HttpClient或者HttpURLConnection
 */
public class HttpStackFactory {
    public static final int GINERBREAD_SDK_NUM = 9;

    /**
     * 根据SDK版本号来创建不同的HTTP执行器，即SDK 9之前使用HttpClient，之后则只用HttpURLConnection
     * @return 返回具体的HttpStack
     */
    public static HttpStack createHttpStack() {
        int runtimeSDKApi = Build.VERSION.SDK_INT;
        if(runtimeSDKApi >= GINERBREAD_SDK_NUM){
           return new HttpURLConnectionStack();
        }
        return new HttpClientStack();
    }
}
