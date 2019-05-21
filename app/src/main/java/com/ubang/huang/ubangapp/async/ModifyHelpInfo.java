package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.common.BeanParam;
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
 *获取求助详细信息
 */

public class ModifyHelpInfo extends AsyncTask<String, Void, String> {
    private int help_id;
    private Handler handler;
    private HelpInfoUpdate helpInfoUpdate;
    public ModifyHelpInfo(Handler handler, HelpInfoUpdate helpInfoUpdate){
        this.handler = handler;
        this.helpInfoUpdate = helpInfoUpdate;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.ModifyHelpContent)
                .addParams("helpInfo",helpInfoUpdate.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        Message message = new Message();
                        if("1".equals(string)){
                            message.what = Signal.ModifyHelpSuc;
                        }else{
                            message.what = Signal.ModifyHelpFail;
                        }
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
