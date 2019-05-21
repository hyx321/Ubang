package com.ubang.huang.ubangapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.async.BindServer;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.socket.RemindOther;

/**
 * Created by huang on 2019/4/29.
 * @author huang
 */

public class RemindService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void open(Context context){
        Intent intent = new Intent(context,RemindService.class);
        context.startService(intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BindServer bindServer = new BindServer();
        bindServer.execute("");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RemindOther.getWebSocketClient().close();
    }

}
