package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.PublishHelpActivity;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/2/24.
 *  弹出紧急求助和普通求助的按钮适配器
 */

public class ChooseSexAdapter extends BaseAdapter{

    private Context context;
    private Handler handler;
    public ChooseSexAdapter(Handler handler, Context context){
        this.context = context;
        this.handler = handler;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.common_person_info_sex, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.user.setSex("男");
                sendMessage();
            }
        });

        viewHolder.woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.user.setSex("女");
                sendMessage();
            }
        });
        return view;
    }

    public void sendMessage(){
        Message message = new Message();
        message.what = Signal.SetSex;
        handler.sendMessage(message);
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
        @BindView(R.id.man)
        ImageView man;
        @BindView(R.id.woman)
        ImageView woman;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
