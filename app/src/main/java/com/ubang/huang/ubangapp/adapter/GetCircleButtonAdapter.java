package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.PublishHelpActivity;
import com.ubang.huang.ubangapp.common.CP;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/2/24.
 *  弹出紧急求助和普通求助的按钮适配器
 */

public class GetCircleButtonAdapter extends BaseAdapter{

    private Context context;
    private Activity activity;

    public GetCircleButtonAdapter(Activity activity, Context context){
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.seekhelp_button_item_normal, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.dialogPlus.dismiss();
            }
        });

        viewHolder.emergency_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.alarmLocationService.unregisterListener(CP.bdaListener); //注销掉监听
                CP.alarmLocationService.stop(); //停止定位服务
                PublishHelpActivity.open(context);

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
        @BindView(R.id.contain)
        LinearLayout contain;
        @BindView(R.id.emergency_help)
        Button emergency_help;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
