package com.ubang.huang.ubangapp.socket;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.MainActivity;
import com.ubang.huang.ubangapp.async.GetChatMessage;
import com.ubang.huang.ubangapp.base.BaseObservable;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.bean.RoughlyHelpInfo;
import com.ubang.huang.ubangapp.common.BeanParam;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.WebSocketMsg;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by huang on 2019/4/29.
 * @author huang
 */

public class RemindOther extends WebSocketClient {

    private static volatile RemindOther socketClient = null;

    public RemindOther(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    public static RemindOther getWebSocketClient(URI serverUri) {
        if (socketClient == null) {
            synchronized (RemindOther.class) {
                if (socketClient == null) {
                    socketClient = new RemindOther(serverUri);
                }
            }
        }
        return socketClient;
    }

    public static RemindOther getWebSocketClient() {
        synchronized (RemindOther.class) {
            return socketClient;
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("RemindOther", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Message mes = new Message();
        try{
            CP.chatMessage = JSON.parseObject(message, ChatMessage.class);
            GetChatMessage getChatMessage = new GetChatMessage(CP.chatMessage.getMessage_id(), CP.chatMessage.getSend());
            getChatMessage.execute("");
        }catch (Exception e){
            int type = Integer.parseInt(message.split(":")[0]);
            int recieve = Integer.parseInt(message.split(":")[1]);
            switch (type){
                case CP.Service_Notice:
                    mes.what = Signal.GetNotification;
                    CP.help_id = recieve;
                    GlobalHandler.NotiHanler.sendMessage(mes);
                    break;
                case CP.Service_Online:
                    mes.what = Signal.GetOnlineStatus;
                    GlobalHandler.ChatHanlers.get(recieve+"").sendMessage(mes);
                    break;
                case CP.Service_Offline:
                    mes.what = Signal.GetOfflineStatus;
                    GlobalHandler.ChatHanlers.get(recieve+"").sendMessage(mes);
                    break;
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("RemindOther", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.e("RemindOther", "onError()");
        ex.printStackTrace();
    }


}
