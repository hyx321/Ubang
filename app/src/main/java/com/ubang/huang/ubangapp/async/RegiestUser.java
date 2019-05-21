package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfoFilter;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
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

public class RegiestUser extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private User user;

    public RegiestUser(Handler handler, User user){
        this.handler = handler;
        this.user = user;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.User_Regiest)
                .addParams("getUser",user.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if("1".equals(string)){
                            message = new Message();
                            message.what = Signal.Regiest_Succ;
                            handler.sendMessage(message);
                        }else{
                            Signal.Result = string;
                            message = new Message();
                            message.what = Signal.Regiest_Fail;
                            handler.sendMessage(message);
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Object response, int id) {
                    }
                });
        return null;
    }

}
