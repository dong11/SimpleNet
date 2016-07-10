package com.rex.hwong.simplenet.core;

import android.util.Log;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.base.Response;
import com.rex.hwong.simplenet.cache.Cache;
import com.rex.hwong.simplenet.cache.LruMemCache;
import com.rex.hwong.simplenet.httpstacks.HttpStack;

import java.util.concurrent.BlockingQueue;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 上午12:01
 */

/**
 * 网络请求Executor，继承自Thread，从网络请求队列中循环读取请求并执行
 */
public final class NetworkExecutor extends Thread {

    //网络请求队列
    private BlockingQueue<Request<?>> mRequestQueue;
    //网络请求栈
    private HttpStack mHttpStack;
    //结果分发器，将结果投递到主线程
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();
    //请求缓存
    private static Cache<String, Response> mReqCache = new LruMemCache();
    //是否停止
    private boolean isStop = false;

    public NetworkExecutor(BlockingQueue<Request<?>> requestQueue, HttpStack httpStack) {
        mRequestQueue = requestQueue;
        mHttpStack = httpStack;
    }

    @Override
    public void run() {
        super.run();
        try {
            while(!isStop){
                final Request<?> request = mRequestQueue.take();

                if(request.isCancel()){
                    Log.i("123", "该请求被取消执行了");
                    continue;
                }
                Response response = null;
                if(isUseCache(request)){
                    //从缓存中取出
                    response = mReqCache.get(request.getURL());
                } else {
                    //从网络获取数据
                    response = mHttpStack.performRequest(request);

                    //如果该请求需要缓存，那么请求成功则缓存到mReqCache中
                    if(request.isShouldCache() && isSuccess(response)){
                        mReqCache.put(request.getURL(), response);
                    }
                }

                //分发请求结果
                mResponseDelivery.deliveryResponse(request, response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("123", "请求分发器退出");
        }
    }

    private boolean isSuccess(Response response) {
        return response != null && response.getStatusCode() == 200;
    }

    private boolean isUseCache(Request<?> request) {
        return request.isShouldCache() && mReqCache.get(request.getURL()) != null;
    }

    public void quit() {
        isStop = true;
        interrupt();
    }
}
