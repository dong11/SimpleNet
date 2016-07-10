package com.rex.hwong.simplenet.core;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/9
 * @time 下午11:51
 */

import android.util.Log;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.httpstacks.HttpStack;
import com.rex.hwong.simplenet.httpstacks.HttpStackFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求队列，使用优先队列，使得请求可以按照优先级进行处理
 */
public final class RequestQueue {
    //线程安全的请求队列
    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<>();
    //请求的序列化生成器
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    //默认的核心数为CPU格式加1
    public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
    //CPU核心数 + 1个分发线程数
    private int mDispatcherNums = DEFAULT_CORE_NUMS;
    //NetworkExecutor,执行网络请求的线程
    private NetworkExecutor[] mDispatchers = null;
    //HTTP请求的真正执行者
    private HttpStack mHttpStack;

    protected RequestQueue(int coreNums, HttpStack httpStack){
        mDispatcherNums = coreNums;
        mHttpStack = httpStack != null? httpStack : HttpStackFactory.createHttpStack();
    }

    /**
     * 启动NetworkExecutor
     */
    private final void startNetworkExecutors(){
        mDispatchers = new NetworkExecutor[mDispatcherNums];
        for(int i = 0;i < mDispatcherNums;i++){
            mDispatchers[i] = new NetworkExecutor(mRequestQueue, mHttpStack);
            mDispatchers[i].start(); //开启线程，执行网络请求
        }
    }

    public void start(){
        stop();
        startNetworkExecutors();
    }

    /**
     * 停止NetworkExecutor
     */
    public void stop() {
        if(mDispatchers != null && mDispatchers.length > 0){
            for(int i = 0;i < mDispatchers.length;i++){
                mDispatchers[i].quit();
            }
        }
    }

    /**
     * 添加请求到队列中
     * @param request
     */
    public void addRequest(Request<?> request){
        if(!mRequestQueue.contains(request)){
            //为请求设置序列号
            request.setSerialNum(this.generateSerialNumber());
            mRequestQueue.add(request);
        } else {
            Log.d("RequestQueue", "请求队列中已经含有该请求");
        }
    }

    /**
     * 清空请求队列
     */
    public void clear(){
        mRequestQueue.clear();
    }

    public BlockingQueue<Request<?>> getAllRequests(){
        return mRequestQueue;
    }

    /**
     * 为每个请求生成序列号
     * @return
     */
    private int generateSerialNumber(){
        return mSerialNumGenerator.incrementAndGet();
    }
}
