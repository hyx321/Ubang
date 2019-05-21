package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfoFilter;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;
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

public class AsyncJudgeLogin extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private HelpInfoFilter helpInfoFilter;
    private Boolean isCleanDate;
    public AsyncJudgeLogin(Handler handler, HelpInfoFilter helpInfoFilter, Boolean isCleanDate){
        this.handler = handler;
        this.helpInfoFilter = helpInfoFilter;
        this.isCleanDate = isCleanDate;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetHelpList)
                .addParams("getHelpList",helpInfoFilter.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        if(isCleanDate){
                            CP.HelpInfoList.clear();
                        }
                        CP.HelpInfoList.addAll(JSON.parseArray(string, Help.class));


                        GetParam.SendMessage(handler,CP.GetHelpListFilter);
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
