package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

public class PostCharMessage  extends AsyncTask<String, Void, String> {

    private List<ChatMessage> chatMessages;
    private ChatMessage chatMessage;
    Handler handler;

    public PostCharMessage(ChatMessage chatMessage,Handler handler,List<ChatMessage> chatMessages){
        this.chatMessage = chatMessage;
        this.handler = handler;
        this.chatMessages = chatMessages;
    }

    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody;
        if( chatMessage.getPic_url()!=null){
            builder.addFormDataPart("image", chatMessage.getPic_url(),
                    RequestBody.create(MediaType.parse("image/jpeg/png"), new File(chatMessage.getPic_url())));
            requestBody = builder.build();
        }else{
            requestBody = new FormBody.Builder()
                    .add("chatMessage", chatMessage.toString())
                    .build();
        }

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(WebSite.PostCharMessage)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                String a = e.getMessage();
                Message message = new Message();
                message.what = Signal.GetAnwserFail;
                handler.sendMessage(message);
            }
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();

                if(!"0".equals(string)) {
                    ChatMessage chatMessage = JSON.parseObject(string, ChatMessage.class);
                    chatMessages.add(chatMessage);

                    Message message = new Message();
                    message.what = Signal.SendMessage;
                    handler.sendMessage(message);
                }
            }
        });
        return null;
    }
}
