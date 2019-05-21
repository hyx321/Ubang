package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PersonSeekHelp;
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

public class GetPersonSeekHelp extends AsyncTask<String, Void, String> {

    private PersonSeekHelp user;
    private List<HelpInfoUpdate> helpInfoUpdates;
    private Handler handler;
    Boolean clean;
    public GetPersonSeekHelp(Handler handler,PersonSeekHelp user,List<HelpInfoUpdate> helpInfoUpdates,Boolean clean){
        this.user = user;
        this.helpInfoUpdates = helpInfoUpdates;
        this.handler = handler;
        this.clean = clean;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetMySeekHelp)
                .addParams("getHelp",user.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        if(clean){
                            helpInfoUpdates.clear();
                        }
                        helpInfoUpdates.addAll(JSONObject.parseArray(string, HelpInfoUpdate.class));

                        Message message = new Message();
                        message.what = Signal.GetPersonSeekHelp;
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
