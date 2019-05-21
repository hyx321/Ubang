package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.luozm.captcha.Captcha;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.MainActivity;
import com.ubang.huang.ubangapp.common.CP;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/2/24.
 * 本地相册和拍照适配器
 */

public class GetForgetPwdlAdapter extends BaseAdapter{

    private Context context;
    private Activity activity;
    private Message message;
    private Handler handler;

    public GetForgetPwdlAdapter(Activity activity, Context context, Handler handler){
        this.context = context;
        this.activity = activity;
        this.handler = handler;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.login_verification_code, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.vertify_code.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                message = new Message();
                message.what = CP.VeritifuSuc;
                handler.sendMessage(message);
                viewHolder.vertify_code.refreshDrawableState();
                return null;
            }

            @Override
            public String onFailed(int failCount) {
                Toast.makeText(activity,"验证失败",Toast.LENGTH_SHORT).show();
                return null;
            }

            @Override
            public String onMaxFailed() {
                Toast.makeText(activity,"验证超过次数，你的帐号被封锁",Toast.LENGTH_SHORT).show();
                return null;
            }
        });

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return 1;
    }

    public static class  ViewHolder {
        @BindView(R.id.vertify_code)
        Captcha vertify_code;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
