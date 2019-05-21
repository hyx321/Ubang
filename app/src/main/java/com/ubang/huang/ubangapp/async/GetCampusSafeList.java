package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.CampusSafe;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class GetCampusSafeList extends AsyncTask<String, Void, String> {
    private int start;
    private Handler handler;
    private List<CampusSafe> campusSafes = new ArrayList<>();
    public GetCampusSafeList(int start,Handler handler,List<CampusSafe> campusSafes){
        this.start = start;
        this.handler = handler;
        this.campusSafes = campusSafes;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetCampusSafeList)
                .addParams("start",start+"")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if(!"1".equals(string)){
                            campusSafes.addAll(JSON.parseArray(string, CampusSafe.class));

                            Message mes = new Message();
                            mes.what = Signal.GetCampusList;
                            handler.sendMessage(mes);
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

