package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ubang.huang.ubangapp.bean.FeedBack;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huang on 2019/3/3.
 * @author  huangyouxin
 *
 * 萌新模块
 * 获取助手回答
 */

public class PostFeedBack extends AsyncTask<String, Void, String> {
    private Handler handler;
    private FeedBack feedBack;
    public PostFeedBack(Handler handler, FeedBack feedBack){
        this.handler = handler;
        this.feedBack = feedBack;
    }
    @Override
    protected String doInBackground(String... strings) {

        OkHttpUtils
                .post()
                .url(WebSite.PostFeedBack)
                .addParams("feedBack",feedBack.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        Message message = new Message();
                        message.what = Signal.SendFeedBackSuc;
                        handler.sendMessage(message);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Message message = new Message();
                        message.what = Signal.SendFeedBackFail;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
        return null;
    }

}
