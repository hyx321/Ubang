package com.ubang.huang.ubangapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ubang.huang.ubangapp.bean.RoughlyHelpInfo;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.socket.RemindOther;

import org.java_websocket.enums.ReadyState;

import java.net.ContentHandler;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by huang on 2019/4/29.
 * @author huang
 */

public class BindServer extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            //String WS_URL = "ws://10.0.2.2:8080/ubang/RemindOthers/";
            User user = new User();
            user.setId(CP.user.getId());
            URI uri = new URI(WebSite.RemindOthers + CP.user.getId());
            RemindOther client = RemindOther.getWebSocketClient(uri);
            client.connect();
            while (!client.getReadyState().equals(ReadyState.OPEN)){}
            Log.i( "BindServer", "连接服务器成功" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
