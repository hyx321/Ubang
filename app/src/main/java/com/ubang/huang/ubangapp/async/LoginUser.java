package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ubang.huang.ubangapp.bean.HelpInfoFilter;
import com.ubang.huang.ubangapp.bean.User;
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

public class LoginUser extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private User user;

    public LoginUser(Handler handler, User user){
        this.handler = handler;
        this.user = user;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.User_Login)
                .addParams("getUser",user.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if("true".equals(string)){
                            message = new Message();
                            message.what = Signal.Login_Succ;
                            handler.sendMessage(message);
                        }else{
                            message = new Message();
                            message.what = Signal.Login_Fail;
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
