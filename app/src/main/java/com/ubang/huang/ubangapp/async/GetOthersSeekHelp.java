package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PersonSeekHelp;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
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

public class GetOthersSeekHelp extends AsyncTask<String, Void, String> {

    private PersonSeekHelp user;
    private List<SeekHelpInfo> helpInfoUpdates;
    private Handler handler;
    public GetOthersSeekHelp(Handler handler, PersonSeekHelp user, List<SeekHelpInfo> helpInfoUpdates){
        this.user = user;
        this.helpInfoUpdates = helpInfoUpdates;
        this.handler = handler;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.GetSeekHelp)
                .addParams("getHelp",user.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        helpInfoUpdates.clear();
                        try {
                            helpInfoUpdates.addAll(JSON.parseArray(string, SeekHelpInfo.class));
                        }catch (Exception e){
                            e.getMessage();
                        }

                        Message message = new Message();
                        message.what = Signal.GetOtherSeekHelp;
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
