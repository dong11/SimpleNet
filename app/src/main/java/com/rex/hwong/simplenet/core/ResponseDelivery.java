package com.rex.hwong.simplenet.core;

import android.os.Handler;
import android.os.Looper;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.base.Response;

import java.util.concurrent.Executor;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 上午10:42
 */

/**
 * 请求结果的投递类，将请求结果投递给UI线程
 */
public class ResponseDelivery implements Executor {

    //关联主线程消息队列的handler
    Handler mResponseHandler = new Handler(Looper.getMainLooper());

    /**
     * 处理请求结果，将其执行在UI线程
     * @param request
     * @param response
     */
    public void deliveryResponse(final Request<?> request, final Response response) {

        Runnable respRunnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };
        execute(respRunnable);
    }

    @Override
    public void execute(Runnable command) {
        mResponseHandler.post(command);
    }
}
