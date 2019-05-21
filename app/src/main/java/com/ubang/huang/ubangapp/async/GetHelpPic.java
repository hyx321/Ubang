package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.HelpInfomation;
import com.ubang.huang.ubangapp.bean.PictureURL;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huang on 2019/3/3.
 * @author  huangyouxin
 * 接收帮助
 */

public class GetHelpPic extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private List<PictureURL> picList;
    private int id;

    public GetHelpPic(Handler handler,int id,List<PictureURL> picList){
        this.handler = handler;
        this.id = id;
        this.picList = picList;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetHelpPic)
                .addParams("id",id+"")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        picList.clear();
                        picList.addAll(JSON.parseArray(string, PictureURL.class));

                        message = new Message();
                        message.what = Signal.GetHelpPic;
                        handler.sendMessage(message);
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
