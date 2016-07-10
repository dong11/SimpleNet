package com.rex.hwong.simplenet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rex.hwong.simplenet.base.Request;
import com.rex.hwong.simplenet.core.RequestQueue;
import com.rex.hwong.simplenet.core.SimpleNet;
import com.rex.hwong.simplenet.request.JsonResquest;
import com.rex.hwong.simplenet.request.StringRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view){
        RequestQueue mQueue = SimpleNet.newRequestQueue();

        Request request = new StringRequest(Request.HttpMethod.GET, "http://www.baidu.com", new Request.RequestListener<String>() {
            @Override
            public void onComplete(int stCode, String response, String errMsg) {
                Log.i("123", ":result:" + response);
            }
        });
        mQueue.addRequest(request);
    }
}
