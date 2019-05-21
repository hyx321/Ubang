package com.ubang.huang.ubangapp.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.baidu.mapapi.model.LatLng;
import com.ubang.huang.ubangapp.bean.InputText;
import com.ubang.huang.ubangapp.bean.Perception;
import com.ubang.huang.ubangapp.bean.SendMessage;
import com.ubang.huang.ubangapp.bean.SendNews;
import com.ubang.huang.ubangapp.bean.UserInfo;
import com.ubang.huang.ubangapp.common.CP;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static java.lang.Float.parseFloat;

/**
 * Created by huang on 2019/4/10.
 */

public class GetParam {

    //获取发送--U帮助手
    public static SendMessage getSendMessage(String question){
        UserInfo userInfo = new UserInfo();
        userInfo.setApiKey(CP.ServiceApiKey);
        userInfo.setUserId(CP.ServiceUserId);

        InputText inputText = new InputText();
        inputText.setText(question);

        Perception perception = new Perception();
        perception.setInputText(inputText);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setReqType(0);
        sendMessage.setPerception(perception);
        sendMessage.setUserInfo(userInfo);
        return  sendMessage;
    }

    public static Timestamp getCurrentTiem(){
        java.util.Date date = new java.util.Date();          // 获取一个Date对象
        return new Timestamp(date.getTime());
    }

    public static String getSendNews(SendNews sendNews){
        sendNews.setAppkey(CP.NewsApiId);
        sendNews.setNum(CP.NewsNum);
        sendNews.setKeyword(CP.NewsKey);
        return sendNews.toString();
    }

    public static OkHttpClient setOkHttpClient(){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    public static void SendMessage(Handler handler,int msg){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = msg;
                try {
                    handler.sendMessage(message);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

    }

    public static String adjustListToString(String list){
        if(list.length() > 0) {
            return list.substring(0, list.length() - 1);
        }else{
            return list;
        }
    }
    public static LatLng StringToLatlng(String latlng){
        String[] position = latlng.split(",");
        return new LatLng(parseFloat(position[0].split(":")[1]),parseFloat(position[1].split(":")[1]));
    }

    public static String getVertifyCode() {
        //abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
        String string = "0123456789";
        char[] ch = new char[4];
        for (int i = 0; i < ch.length; i++) {
            Random random = new Random();
            int index = random.nextInt(string.length());
            ch[i] = string.charAt(index);
        }
        return new String(ch);
    }

    public static String getRandomId() {
        String string = "0123456789";
        char[] ch = new char[9];
        for (int i = 0; i < ch.length; i++) {
            Random random = new Random();
            int index = random.nextInt(string.length());
            ch[i] = string.charAt(index);
        }
        return new String(ch);
    }

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null ){
            data = uri.getPath();
        }
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
         return data;
    }
}
