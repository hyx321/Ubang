package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.AlarmContent;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huang on 2019/3/3.
 * @author  huangyouxin
 *
 * 萌新模块
 * 获取助手回答
 */

public class GetAlarmContent extends AsyncTask<String, Void, String> {
    private String user_phone;
    private Handler handler;

    public GetAlarmContent(Handler handler,String user_phone){
        this.user_phone = user_phone;
        this.handler = handler;
    }
    @Override
    protected String doInBackground(String... strings) {

        OkHttpUtils
                .post()
                .url(WebSite.GetAlarmContent)
                .addParams("user_phone", user_phone)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if(!"1".equals(string)) {
                            CP.contentList.clear();
                            CP.contentList.addAll(JSON.parseArray(string, AlarmContent.class));

                            Message message = new Message();
                            message.what = Signal.GetAlarmContent;
                            handler.sendMessage(message);
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
