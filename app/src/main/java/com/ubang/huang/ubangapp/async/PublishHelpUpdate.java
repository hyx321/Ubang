package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PictureURL;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.GetParam;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

/**
 * Created by huang on 2019/3/3.
 * @author  huangyouxin
 *
 * 萌新模块
 * 获取助手回答
 */

public class PublishHelpUpdate extends AsyncTask<String, Void, String> {
    private Handler handler;
    private Message message;
    private HelpInfoUpdate help;
    private List<PictureURL> pictureURLS;
    public PublishHelpUpdate(Handler handler,  List<PictureURL> pictureURLS, HelpInfoUpdate help){
        this.handler = handler;
        this.help = help;
        this.pictureURLS = pictureURLS;
    }
    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient okHttpClient = GetParam.setOkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();

        if(help.getHas_picture() == 1) {
            for (int i = 0; i < pictureURLS.size(); i++) {
                builder.addFormDataPart("image", pictureURLS.get(i).getPicture(),
                        RequestBody.create(MediaType.parse("image/jpeg/png"), new File(pictureURLS.get(i).getPicture())));
            }
        }

        builder.addFormDataPart("getHelp", help.toString());
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(WebSite.PublishHelp)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                String content = string.split(":")[0];
                Message message = new Message();
                if("1".equals(content)){
                    message.what = Signal.PublishHelp_Suc;
                }else{
                    message.what = Signal.PublishHelp_Fail;
                }
                handler.sendMessage(message);
            }
        });

        return null;
    }

}
