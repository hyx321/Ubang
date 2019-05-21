package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ubang.huang.ubangapp.common.Signal;
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

public class AsyncPostAvatar extends AsyncTask<String, Void, String> {

    private int id;
    private String imagePath;
    private Handler handler;
    public AsyncPostAvatar(Handler handler,String imagePath, int id){
        this.imagePath = imagePath;
        this.id = id;
        this.handler = handler;
    }
    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg/png"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(WebSite.PostAvatar)
                .post(requestBody)
                .addHeader("id", id+"")
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
                Message message = new Message();
                if("1".equals(string)){
                    message.what = Signal.PostAvatarSuc;
                    handler.sendMessage(message);
                }else{
                    message.what = Signal.PostAvatarFail;
                    handler.sendMessage(message);
                }

            }
        });
        return null;
    }
}
