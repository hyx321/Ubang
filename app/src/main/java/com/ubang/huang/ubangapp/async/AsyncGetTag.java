package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huang on 2019/3/3.
 * @author  huangyouxin
 *
 * 萌新模块
 * 获取助手回答
 */

public class AsyncGetTag extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    public AsyncGetTag(Handler handler){
        this.handler = handler;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.Get_TAG)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        String tag[] = string.split(":");

                        CP.MOUDLE_JOB_CITY_TAG.clear();
                        CP.MOUDLE_JOB_PEOPLE_TAG.clear();
                        CP.MOUDLE_JOB_COMPANY_TYPE_TAG.clear();
                        CP.MOUDLE_JOB_DEGREE_TAG.clear();
                        CP.MOUDLE_JOB_EXP_TAG.clear();

                        CP.MOUDLE_JOB_CITY_TAG.addAll(Arrays.asList(tag[0].split(",")));
                        CP.MOUDLE_JOB_PEOPLE_TAG.addAll(Arrays.asList(tag[1].split(",")));
                        CP.MOUDLE_JOB_COMPANY_TYPE_TAG.addAll(Arrays.asList(tag[2].split(",")));
                        CP.MOUDLE_JOB_DEGREE_TAG.addAll(Arrays.asList(tag[3].split(",")));
                        CP.MOUDLE_JOB_EXP_TAG.addAll(Arrays.asList(tag[4].split(",")));

                        Message message = new Message();
                        message.what = CP.GetTag;
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
