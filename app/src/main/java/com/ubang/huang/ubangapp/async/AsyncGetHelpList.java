package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfoFilter;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.Job;
import com.ubang.huang.ubangapp.bean.JobInfo;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;
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

public class AsyncGetHelpList extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private HelpInfoFilter helpInfoFilter;
    private Boolean isCleanDate;
    private List<SeekHelpInfo> helpInfoUpdates;
    public AsyncGetHelpList(Handler handler, HelpInfoFilter helpInfoFilter, Boolean isCleanDate,List<SeekHelpInfo> helpInfoUpdates){
        this.handler = handler;
        this.helpInfoFilter = helpInfoFilter;
        this.isCleanDate = isCleanDate;
        this.helpInfoUpdates = helpInfoUpdates;
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
                        try {
                            helpInfoUpdates.clear();
                            helpInfoUpdates.addAll(JSON.parseArray(string, SeekHelpInfo.class));
                            Message message = new Message();
                            message.what = CP.GetHelpListFilter;
                            handler.sendMessage(message);
                        }catch (Exception e){
                            e.printStackTrace();
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
