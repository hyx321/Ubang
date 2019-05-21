package com.ubang.huang.ubangapp.async;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ubang.huang.ubangapp.bean.GetNews;
import com.ubang.huang.ubangapp.bean.Person;
import com.ubang.huang.ubangapp.bean.SendNews;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 *
 * Created by huang on 2019/3/2.
 * @author = huangyouxin
 * 萌新模块
 * 获取校园图片
 */

public  class AsyncGetPerson extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private int phone;
    private User user;

    public AsyncGetPerson(Handler handler,int phone,User user){
        this.handler = handler;
        this.phone = phone;
        this.user = user;
    }

    @Override
    @TargetApi(26)
    protected String doInBackground(String... params) {

        OkHttpUtils
                .post()
                .url(WebSite.PersonInfo)
                .addParams("id",phone+"")
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        user = JSON.parseObject(string, new TypeReference<User>(){});
                        GetParam.SendMessage(handler, Signal.GetPseron);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
        return null;
    }
}

