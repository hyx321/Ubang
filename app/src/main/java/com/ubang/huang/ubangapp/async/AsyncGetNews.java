package com.ubang.huang.ubangapp.async;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ubang.huang.ubangapp.bean.GetNews;
import com.ubang.huang.ubangapp.bean.SendNews;
import com.ubang.huang.ubangapp.bean.ServiceMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 *
 * Created by huang on 2019/3/2.
 * @author = huangyouxin
 * 萌新模块
 * 获取校园图片
 */

public  class AsyncGetNews extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private SendNews sendNews;

    public AsyncGetNews(Handler handler, SendNews sendNews){
        this.handler = handler;
        this.sendNews = sendNews;
    }

    @Override
    @TargetApi(26)
    protected String doInBackground(String... params) {

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();

        Request request = new Request.Builder()
                .url(WebSite.News_Url+GetParam.getSendNews(sendNews))
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                JSONObject obj =JSON.parseObject(string);
                JSONArray jsonArray =   obj.getJSONObject("result").getJSONArray("list");
                CP.getNewsList.addAll(JSON.parseArray(jsonArray.toJSONString(), GetNews.class));

                Message message = new Message();
                message.what = CP.GetNews;
                handler.sendMessage(message);

            }
        });
        return null;
    }
}

