package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.CP;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/2/24.
 * 本地相册和拍照适配器
 */

public class GetPicUrlAdapter extends BaseAdapter{

    private Context context;
    private Activity activity;

    public GetPicUrlAdapter(Activity activity, Context context){
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.seekhelp_input_content_list_item, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        if(position == 0){
            viewHolder.text.setText("拍照选择");
            viewHolder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CP.CAMERA);
                }
            });
        }else {
            viewHolder.text.setText("从本地相册选取");
            viewHolder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(intent, CP.ALBUM);
                    }catch (Throwable t) {
                        Toast.makeText(context, "很抱歉，当前您的手机不支持相册选择功能，请安装相册软件", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

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
        return 2;
    }

    public static class  ViewHolder {
        @BindView(R.id.text_view)
        TextView text;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
