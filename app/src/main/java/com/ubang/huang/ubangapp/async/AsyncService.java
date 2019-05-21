package com.ubang.huang.ubangapp.async;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.ServiceMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;

import java.io.IOException;
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

public  class AsyncService extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private String question;

    public AsyncService(Handler handler,String question){
        this.handler = handler;
        this.question = question;
    }

    @Override
    @TargetApi(26)
    protected String doInBackground(String... params) {


        String  json = JSON.toJSONString(GetParam.getSendMessage(question));

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        Request request = new Request.Builder()
                .url(WebSite.Service_Url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string().split("text")[2];

                ServiceMessage serviceMessage = new ServiceMessage();
                serviceMessage.setContent(string.substring(3,string.length()-5));
                serviceMessage.setWhoSend(CP.Service);
                serviceMessage.setTime(GetParam.getCurrentTiem().toString());

                CP.ServiceList.add(serviceMessage);

                Message message = new Message();
                message.what = CP.Send_Message;
                handler.sendMessage(message);
            }
        });
        return null;
    }
}

