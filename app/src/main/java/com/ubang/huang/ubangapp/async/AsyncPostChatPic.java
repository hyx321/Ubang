package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

public class AsyncPostChatPic extends AsyncTask<String, Void, String> {

    private List<ChatMessage> chatMessages;
    private ChatMessage chatMessage;
    Handler handler;

    public AsyncPostChatPic(ChatMessage chatMessage, Handler handler, List<ChatMessage> chatMessages){
        this.chatMessage = chatMessage;
        this.handler = handler;
        this.chatMessages = chatMessages;
    }
    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("image", chatMessage.getPic_url(),
                RequestBody.create(MediaType.parse("image/jpeg/png"), new File(chatMessage.getPic_url())));

        builder.addFormDataPart("chatMessage", chatMessage.toString());

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(WebSite.PostCharMessagePic)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                String a = e.getMessage();
                Message message = new Message();
                message.what = Signal.GetAnwserFail;
                handler.sendMessage(message);
                System.out.println("AsyncPostChatPic:连接失败");
                System.out.println(e.getMessage());
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
