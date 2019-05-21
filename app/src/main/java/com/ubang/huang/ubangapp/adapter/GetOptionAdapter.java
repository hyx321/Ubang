package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.PersonInfoActivity;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.AsyncPostChatPic;
import com.ubang.huang.ubangapp.async.PostCharMessage;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.GetParam;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/2/24.
 *
 */

public class GetOptionAdapter extends BaseAdapter{

    private Context context;
    private String title;
    private String avatar;
    private String content;
    private String url;
    private Handler handler;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatMessage chatMessage;
    private Activity activity;

    public GetOptionAdapter(Activity activity,Context context,String title,String avatar,String content,Handler handler,List<ChatMessage> chatMessages,ChatMessage chatMessage){
        this.context = context;
        this.content = content;
        this.avatar = avatar;
        this.title = title;
        this.chatMessage = chatMessage;
        this.chatMessages = chatMessages;
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.seekhelp_qrcode_item, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        AsyncGetPic getPic = new AsyncGetPic();
        Bitmap mBitmap;
        if("取消帮助".equals(title)){
            mBitmap = CodeUtils.createImage(content, 300, 300,BitmapFactory.decodeResource(activity.getResources(), R.mipmap.cancel_help));
        }else{
            mBitmap = CodeUtils.createImage(content, 300, 300,BitmapFactory.decodeResource(activity.getResources(), R.mipmap.finish_help));
        }
        viewHolder.qrcode.setImageBitmap(mBitmap);
        url = getPic.saveBitmap(mBitmap);
        chatMessage.setPic_url(url);
        viewHolder.qrcode_title.setText(title);
        viewHolder.qrcode_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncPostChatPic postCharMessage = new AsyncPostChatPic(chatMessage,handler,chatMessages);
                postCharMessage.execute("");
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
        @BindView(R.id.qrcode_title)
        TextView qrcode_title;
        @BindView(R.id.qrcode)
        ImageView qrcode;
        @BindView(R.id.qrcode_option)
        TextView qrcode_option;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
