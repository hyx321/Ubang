package com.ubang.huang.ubangapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.AsyncPostPic;
import com.ubang.huang.ubangapp.async.CancelHelp;
import com.ubang.huang.ubangapp.async.FinishHelp;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.socket.RemindOther;
import com.ubang.huang.ubangapp.util.CodeUtils;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.GetParam;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickCallBack{
        void onItemClick(int pos);
    }

    private ItemClickCallBack clickCallBack;
    private Context context;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private Handler UIHandler;
    private HandlerThread handlerThread;
    private Handler DataHandler;
    Dialog dialog;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    /**
     * 创建新View，被LayoutManager所调用
     * @param viewGroup viewGroup
     * @param viewType viewType
     * @return view view
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case CP.You:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_chat_message_left, viewGroup, false);
                return new LeftViewHolder(view);
            case CP.Me:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_chat_message_right, viewGroup, false);
                return new RightViewHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_chat_message_center, viewGroup, false);
                return new CenterViewHolder(view);
        }
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder holder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        addHandle();
        ChatMessage chatMessage = chatMessages.get(position);
        if (viewHolder instanceof LeftViewHolder) {
            setLeftView(((LeftViewHolder) viewHolder),chatMessage);
        } else if (viewHolder instanceof RightViewHolder) {
            setRightView(((RightViewHolder) viewHolder),chatMessage);
        }else{
            setCenterView(((CenterViewHolder) viewHolder),chatMessage);
        }
    }

    private void setCenterView(CenterViewHolder viewHolder, ChatMessage chatMessage) {
        viewHolder.time.setText(chatMessage.getTime());
        viewHolder.title.setText(chatMessage.getContent());
    }

    private void setLeftView(LeftViewHolder view,ChatMessage chatMessage) {
        String time = chatMessage.getTime();
        view.time.setText(time.substring(0,time.length()-2));
        if(chatMessage.getHelper_avatar().equals(CP.user.getAvatar())){
            Glide.with(context).load(chatMessage.getResourse_avatar()).into(view.avatar);
        }else{
            Glide.with(context).load(chatMessage.getHelper_avatar()).into(view.avatar);
        }
        if (!chatMessage.getPic_url().equals("null")){
            Glide.with(context).load(chatMessage.getPic_url()).into(view.photo);
            view.message.setVisibility(View.GONE);
            view.photo.setVisibility(View.VISIBLE);
            view.photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AsyncGetPic getPic = new AsyncGetPic();
                    getPic.getBitmap(DataHandler,chatMessage.getPic_url());
                    return false;
                }
            });
        } else {
            view.message.setText(chatMessage.getContent());
            view.message.setVisibility(View.VISIBLE);
            view.photo.setVisibility(View.GONE);
        }
    }

    private void setRightView(RightViewHolder view,ChatMessage chatMessage) {
        String time = chatMessage.getTime();
        view.time.setText(time.substring(0,time.length()-2));
        Glide.with(context).load(CP.user.getAvatar()).into(view.avatar);
        if (!chatMessage.getPic_url().equals("null")) {
            Glide.with(context).load(chatMessage.getPic_url()).into(view.photo);
            view.message.setVisibility(View.GONE);
            view.photo.setVisibility(View.VISIBLE);
            view.photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AsyncGetPic getPic = new AsyncGetPic();
                    getPic.getBitmap(DataHandler,chatMessage.getPic_url());
                    return false;
                }
            });
        } else {
            view.message.setText(chatMessage.getContent());
            view.message.setVisibility(View.VISIBLE);
            view.photo.setVisibility(View.GONE);
        }
    }
    private void analyzeBitmap(){
        CodeUtils.analyzeBitmap(CP.bitmap,analyzeCallback);
    }


    private CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            ChatMessage chatMessage = JSON.parseObject(result, ChatMessage.class);
            if(chatMessage.getSend() != CP.user.getId()) {
                if (chatMessage.getOption().equals("结束帮助")) {
                    FinishHelp finishHelp = new FinishHelp(DataHandler, chatMessage.getHelp_id());
                    finishHelp.execute("");
                } else {
                    CancelHelp cancelHelp = new CancelHelp(DataHandler, chatMessage.getHelp_id());
                    cancelHelp.execute("");
                }
            }else{
                Dialog fail = new Dialog(DialogModel.GiveQRCode,context);
                fail.createDialog();
                fail.show();
            }
        }

        @Override
        public void onAnalyzeFailed() {
            Dialog fail = new Dialog(DialogModel.AnalyzeQRCodeFail,context);
            fail.createDialog();
            fail.show();
        }
    };

    private void addHandle() {
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()) {
            @Override
            //消息处理的操作
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Signal.SendMessage:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                analyzeBitmap();
                            }
                        });
                        break;
                    case Signal.FinishHelp:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Dialog finsh = new Dialog(DialogModel.FinishHelp,context);
                                finsh.createDialog();
                                finsh.show();
                                int i = 0;
                                int recieve=0;
                                while(i<chatMessages.size()){
                                    if(chatMessages.get(i).getSend() == CP.user.getId()){
                                        recieve = chatMessages.get(i).getRecieve();
                                        break;
                                    }else if(chatMessages.get(i).getRecieve() == CP.user.getId()){
                                        recieve = chatMessages.get(i).getSend();
                                        break;
                                    }
                                }
                                Message message = new Message();
                                message.what = Signal.FinishHelp;
                                GlobalHandler.ChatHanlers.get(recieve+"").sendMessage(message);
                            }
                        });
                        break;
                    case Signal.CancelHelp:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                Dialog cancel = new Dialog(DialogModel.CancelHelp,context);
                                cancel.createDialog();
                                cancel.show();

                                int i = 0;
                                int recieve=0;
                                while(i<chatMessages.size()){
                                    if(chatMessages.get(i).getSend() == CP.user.getId()){
                                        recieve = chatMessages.get(i).getRecieve();
                                        break;
                                    }else if(chatMessages.get(i).getRecieve() == CP.user.getId()){
                                        recieve = chatMessages.get(i).getSend();
                                        break;
                                    }
                                }
                                Message message = new Message();
                                message.what = Signal.CancelHelp;
                                GlobalHandler.ChatHanlers.get(recieve+"").sendMessage(message);
                            }
                        });
                        break;
                    case Signal.CancelHelpFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Dialog cancel = new Dialog(DialogModel.CancelHelpFail,context);
                                cancel.createDialog();
                                cancel.show();
                            }
                        });
                        break;
                    case Signal.FinishHelpFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Dialog cancel = new Dialog(DialogModel.CancelHelpFail,context);
                                cancel.createDialog();
                                cancel.show();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     *  获取数据的数量
     */
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).getSend() == CP.user.getId()){
            return CP.Me;
        }else if(chatMessages.get(position).getRecieve() == CP.user.getId()){
            return CP.You;
        }else{
            return CP.Center;
        }
    }

    public static class LeftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_time)
        TextView time;
        @BindView(R.id.tv_msg_left)
        TextView message;
        @BindView(R.id.tv_photo_left)
        ImageView photo;
        @BindView(R.id.avatar)
        ImageView avatar;

        public LeftViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public static class RightViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_right_time)
        TextView time;
        @BindView(R.id.tv_msg_right)
        TextView message;
        @BindView(R.id.tv_photo_right)
        ImageView photo;
        @BindView(R.id.avatar)
        ImageView avatar;

        public RightViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public static class CenterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_center_time)
        TextView time;
        @BindView(R.id.tv_center_title)
        TextView title;

        public CenterViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















