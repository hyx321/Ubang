package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.HelpDetailInfo;
import com.ubang.huang.ubangapp.common.BeanParam;
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
 *获取求助详细信息
 */

public class DeleteHelpInfo extends AsyncTask<String, Void, String> {
    private int help_id;
    private Handler handler;
    public DeleteHelpInfo(Handler handler, int help_id){
        this.handler = handler;
        this.help_id = help_id;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.DeleteHelp)
                .addParams("id",help_id+"")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        Message message = new Message();
                        if("1".equals(string)){
                            message.what = Signal.DeleteHelpSuc;
                        }else{
                            message.what = Signal.DeleteHelpFail;
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
