package com.ubang.huang.ubangapp.async;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.GetParam;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by huang on 2019/3/1.
 * @author = huangyouxin
 */
public class AsyncGetPic {

    private Bitmap map;
    /**
     * id 0 显示图片
     * id 1 画廊模式的背景
     * @param url
     */
    public  void displayPhoto(final ImageView imageView, String url,int id){
        try {
            OkHttpUtils.get()
                    .id(id)
                    .url(url)
                    .tag(this)
                    .build()
                    .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("onError", e.getMessage());
                        }

                        @Override
                        public void onResponse(Bitmap bitmap, int id) {
                            switch (id) {
                                case CP.GetURLPic:
                                    imageView.setImageBitmap(bitmap);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void getBitmap(Handler handler, String url){
        OkHttpUtils.get()
                .url(url)
                .tag(this)
                .build()
                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("onError",e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        CP.bitmap = bitmap;
                        Message message = new Message();
                        message.what =  Signal.SendMessage;
                        handler.sendMessage(message);
                    }
                });
    }

    public String saveBitmap(Bitmap bitmap)
    {
        String PathUrl = Environment.getExternalStorageDirectory().toString()+"/ubang/QRCode/";
        String urlTest = PathUrl + "/"+GetParam.getRandomId()+".png";
        File filePath = new File(PathUrl);
        if(!filePath.exists()){
            filePath.mkdirs();
        }

        File file = new File(urlTest);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return urlTest;
    }

}
