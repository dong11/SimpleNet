package com.rex.hwong.simplenet.httpstacks;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.base.Response;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 上午12:02
 */

/**
 * 执行网络请求的接口
 */
public interface HttpStack {
    /**
     * 执行Http请求
     * @param request 待执行的请求
     * @return 返回Response
     */
    Response performRequest(Request<?> request);
}
