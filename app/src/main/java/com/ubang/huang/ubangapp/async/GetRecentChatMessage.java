package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class GetRecentChatMessage extends AsyncTask<String, Void, String> {
    private int id;
    private int start;
    List<ChatMessage> chatMessages = new ArrayList<>();
    public GetRecentChatMessage(int id, int start, List<ChatMessage> chatMessages){
        this.id = id;
        this.start = start;
        this.chatMessages = chatMessages;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetRecentChatMessage)
                .addParams("id",id+"")
                .addParams("start",start+"")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if(!"1".equals(string)){
                            List<ChatMessage> mes = new ArrayList<>();
                            mes.addAll(JSON.parseArray(string, ChatMessage.class));
                            Collections.reverse(mes);
                            mes.addAll(chatMessages);
                            chatMessages.clear();
                            chatMessages.addAll(mes);

                            Message message = new Message();
                            message.what = Signal.GetRecentMessage;
                            GlobalHandler.ChatMessageHanler.sendMessage(message);
                        }

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

