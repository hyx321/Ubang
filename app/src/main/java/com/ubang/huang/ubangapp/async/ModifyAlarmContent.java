package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.ubang.huang.ubangapp.bean.AlarmContent;
import com.ubang.huang.ubangapp.common.CP;
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

public class ModifyAlarmContent extends AsyncTask<String, Void, String> {
    private AlarmContent alarmContent;
    private Handler handler;

    public ModifyAlarmContent(Handler handler, AlarmContent alarmContent){
        this.alarmContent = alarmContent;
        this.handler = handler;
    }
    @Override
    protected String doInBackground(String... strings) {
        OkHttpUtils
                .post()
                .url(WebSite.ModifyAlarm)
                .addParams("getAlarmContent",alarmContent.toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();

                        if("1".equals(string)){
                            Message message = new Message();
                            message.what = Signal.Modify_Alarm_Suc;
                            handler.sendMessage(message);
                        }else{
                            Message message = new Message();
                            message.what = Signal.Modify_Alarm_Fail;
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
