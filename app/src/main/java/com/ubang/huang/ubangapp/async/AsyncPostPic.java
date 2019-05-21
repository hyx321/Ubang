package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ubang.huang.ubangapp.bean.GetNews;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 * Created by huang on 2019/3/3.
 * @author  huangyouxin
 *
 * 萌新模块
 * 获取助手回答
 */

public class AsyncPostPic extends AsyncTask<String, Void, String> {

    private int helpId;
    private String imagePath;
    public AsyncPostPic(String imagePath,int helpId){
        this.imagePath = imagePath;
        this.helpId = helpId;
    }
    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg/png"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(WebSite.PostPic)
                .post(requestBody)
                .addHeader("helpId", helpId+"")
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
            }
        });
        return null;
    }
}
