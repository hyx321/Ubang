package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.bean.HelpInfomation;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public class GetChatMessage extends AsyncTask<String, Void, String> {
    private int id;
    private int recieve;
    public GetChatMessage(int id,int recieve){
        this.id = id;
        this.recieve = recieve;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetChatMessage)
                .addParams("id",id+"")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if(!"1".equals(string)){
                            CP.chatMessage = JSON.parseObject(string, ChatMessage.class);
                            Message mes = new Message();
                            mes.what = Signal.GetAnwser;
                            try {
                                CP.ChatByWay = CP.ChatByHelp;
                                GlobalHandler.ChatHanlers.get(recieve+"").sendMessage(mes);
                            }catch (Exception e){
                                CP.ChatByWay = CP.ChatByNotice;
                                GlobalHandler.NotiHanler.sendMessage(mes);
                            }
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

