package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.ChatAdapter;
import com.ubang.huang.ubangapp.adapter.GetOptionAdapter;
import com.ubang.huang.ubangapp.adapter.GetPicUrlAdapter;
import com.ubang.huang.ubangapp.async.AsyncPostChatPic;
import com.ubang.huang.ubangapp.async.GetRecentChatMessage;
import com.ubang.huang.ubangapp.async.PostCharMessage;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.bean.RoughlyHelpInfo;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.BeanParam;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.socket.RemindOther;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.GetParam;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomActivity extends Activity implements View.OnClickListener,TextWatcher{
    private ChatAdapter chatAdapter;
    BaseRecycleView baseRecycleView;
    Boolean sendStatus = false;
    Handler UIHandler;
    HandlerThread handlerThread;
    Handler DataHandler;
    List<ChatMessage> chatMessages = new ArrayList<>();
    ChatMessage chatMessage;
    private int recieve;
    SeekHelpInfo help;
    private ChatMessage NoticeMessage;
    private int start = 0;
    private int reciever = 0;
    RelativeLayout cancel_block;
    RelativeLayout finish_block;
    RelativeLayout camera_block;
    RelativeLayout album_block;
    View OptionView;
    Boolean open = false;
    GetOptionAdapter getPicUrlAdapter;
    DialogPlus dialog;
    private int type;

    @BindView(R.id.sub)
    ViewStub sub;
    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    @BindView(R.id.send_meg)
    ImageView sendKey;
    @BindView(R.id.send_content)
    EditText sendContent;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.send_photo)
    ImageView sendPhoto;
    @BindView(R.id.gallery_beauty)
    TextView title;

    public static void open(Context context) {
        context.startActivity(new Intent(context, ChatRoomActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_service);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        addHandle();
        addRecyclerView();
        getStub();
        judgeChatWay();
        addListener();
    }

    private void  getStub(){
        if (OptionView == null) {
            OptionView = sub.inflate();
            cancel_block = findViewById(R.id.cancel_block);
            finish_block = findViewById(R.id.finish_block);
            camera_block = findViewById(R.id.camera_block);
            album_block = findViewById(R.id.album_block);

            cancel_block.setOnClickListener(this);
            finish_block.setOnClickListener(this);
            camera_block.setOnClickListener(this);
            album_block.setOnClickListener(this);
        }
        OptionView.setVisibility(View.GONE);
    }

    private void judgeChatWay(){
        type = CP.ChatByWay;
        switch (type){
            case CP.ChatByHelp:
                help = CP.seekHelpInfo;
                reciever = help.getRecourse();
                watchStatus();
                break;
            case CP.ChatByNotice:
                help = new SeekHelpInfo();
                help.setId(CP.chatMessage.getHelp_id());
                help.setRecourse(CP.chatMessage.getResourse());
                NoticeMessage = CP.chatMessage;
                watchStatusNotice();
                reciever = NoticeMessage.getSend();
                break;
            case CP.ChatBySeekHelp:
                help = new SeekHelpInfo();
                help.setId(BeanParam.HelpInfo.getId());
                help.setNickname(BeanParam.HelpInfo.getName());

                help.setRecourse(BeanParam.HelpInfo.getHelper_id());
                reciever = BeanParam.HelpInfo.getHelper_id();
                watchStatus();
                break;
        }
        GlobalHandler.ChatHanlers.put(reciever+"",DataHandler);
        getRecentMes();
    }

    private void getRecentMes() {
        GetRecentChatMessage getRecentChatMessage = new GetRecentChatMessage(help.getId(),start,chatMessages);
        getRecentChatMessage.execute("");
    }

    private void watchStatus() {
        RoughlyHelpInfo info = new RoughlyHelpInfo();
        info.setResourse(help.getRecourse());
        info.setHelper(CP.user.getId());
        info.setHelp_id(help.getId());
        info.setType(CP.Service_Online);
        RemindOther.getWebSocketClient().send(info.toString());

        title.setText(help.getNickname());
    }

    private void watchStatusNotice() {
        RoughlyHelpInfo info = new RoughlyHelpInfo();
        info.setResourse(CP.chatMessage.getSend());
        info.setHelper(CP.user.getId());
        info.setHelp_id(CP.chatMessage.getHelp_id());
        info.setType(CP.Service_Online);
        RemindOther.getWebSocketClient().send(info.toString());

        String name = "";
        if(CP.chatMessage.getResourse() == CP.user.getId()){
            name = CP.chatMessage.getHelper_name();
        }else{
            name = CP.chatMessage.getResourse_name();
        }
        title.setText(name);
    }

    private void addHandle() {
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()){
            @Override
            //消息处理的操作
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.SendMessage:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                                try {
                                    RemindOther.getWebSocketClient().send(chatMessage.toString());
                                    dialog.dismiss();
                                }catch (Exception e){
                                    Toast.makeText(ChatRoomActivity.this,"与服务器断开连接",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;

                    case Signal.GetAnwser:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(CP.chatMessage.getSend() != CP.user.getId()){
                                    recieve = CP.chatMessage.getSend();
                                }else{
                                    recieve = CP.chatMessage.getRecieve();
                                }
                                chatMessages.add(CP.chatMessage);
                                refresh();
                            }
                        });
                        break;
                    case Signal.GetAnwserFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendCenterMes(CP.SendChatFail);
                            }
                        });
                        break;
                    case Signal.GetOnlineStatus:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendCenterMes(CP.ChatOnlineStatus);
                            }
                        });
                        break;
                    case Signal.GetOfflineStatus:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendCenterMes(CP.ChatOfflineStatus);
                            }
                        });
                        break;
                    case Signal.CancelHelp:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendCenterMes(CP.CancelHelp);
                            }
                        });
                        break;
                    case Signal.FinishHelp:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendCenterMes(CP.FinshHelp);
                            }
                        });
                        break;
                    case Signal.GetRecentMessage:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                                start+=10;
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void addListener() {
        back.setOnClickListener(this);
        sendPhoto.setOnClickListener(this);
        sendContent.addTextChangedListener(this);
        sendKey.setOnClickListener(this);
    }

    private void addRecyclerView(){
        baseRecycleView = new BaseRecycleView(this);
        mRecyclerView = baseRecycleView.addRecycle(mRecyclerView,"Linear",true,true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.refreshComplete();
                getRecentMes();
            }

            @Override
            public void onLoadMore() {
                    mRecyclerView.loadMoreComplete();
            }
        });

        chatAdapter = new ChatAdapter(chatMessages,this);
        mRecyclerView.setAdapter(chatAdapter);
        mRecyclerView.scrollToPosition(chatMessages.size());
    }

    private void PostMessage() {
        PostCharMessage postCharMessage = new PostCharMessage(chatMessage,DataHandler,chatMessages);
        postCharMessage.execute("");
        sendContent.setText("");
    }
    private void refresh(){
        chatAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(chatMessages.size());
    }

    private void setMessageInfoByNotice() {
        chatMessage = new ChatMessage();
        chatMessage.setHelp_id(help.getId());
        chatMessage.setResourse(help.getRecourse());
        chatMessage.setContent(sendContent.getText().toString());
        String id = GetParam.getRandomId();
        Random random = new Random();
        int num = random.nextInt(9)+1;
        chatMessage.setMessage_id(Integer.parseInt(id)*num);
        chatMessage.setRecieve(NoticeMessage.getSend());
        chatMessage.setSend(NoticeMessage.getRecieve());
    }

    private void setMessageInfo() {
        chatMessage = new ChatMessage();
        chatMessage.setHelp_id(help.getId());
        chatMessage.setResourse(help.getRecourse());
        chatMessage.setContent(sendContent.getText().toString());
        String id = GetParam.getRandomId();
        Random random = new Random();
        int num = random.nextInt(9)+1;
        chatMessage.setMessage_id(Integer.parseInt(id)*num);

        if(chatMessages.size() > 1){
            chatMessage.setSend(CP.user.getId());
            if(help.getRecourse() != CP.user.getId()){
                chatMessage.setRecieve(help.getRecourse());
            }else{
                chatMessage.setRecieve(recieve);
            }
        }else{
            chatMessage.setSend(CP.user.getId());
            chatMessage.setRecieve(help.getRecourse());
        }
    }

    private void sendCenterMes(String result){
        chatMessage = new ChatMessage();
        chatMessage.setHelp_id(CP.Center);
        chatMessage.setContent(result);
        chatMessages.add(chatMessage);
        refresh();
    }

    /**
     * 图片信息的处理
     * 添加到发送信息的list中
     * @param uri
     */
    private void SendPhoto(String uri){
        chatMessage = new ChatMessage();
        chatMessage.setResourse(help.getRecourse());
        chatMessage.setHelp_id(help.getId());
        chatMessage.setPic_url(uri);

        String id = GetParam.getRandomId();
        Random random = new Random();
        int num = random.nextInt(9)+1;
        chatMessage.setMessage_id(Integer.parseInt(id)*num);

        if(chatMessages.size() > 1){
            chatMessage.setSend(CP.user.getId());
            chatMessage.setRecieve(recieve);
        }else{
            chatMessage.setSend(CP.user.getId());
            chatMessage.setRecieve(help.getRecourse());
        }
        AsyncPostChatPic postCharMessage = new AsyncPostChatPic(chatMessage,DataHandler,chatMessages);
        postCharMessage.execute("");
    }

    /**
     * 关于图片发送的相关操作
     * @param requestCode 请求代码 判定是拍照还是相册选图
     * @param resultCode 结果代码
     * @param intent intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CP.CAMERA:
                    openAlbum();
                    break;
                case CP.ALBUM:
                    try {
                        //获取选中文件的定位符
                        Uri uri = intent.getData();
                        String url = GetParam.getRealFilePath(ChatRoomActivity.this,uri);
                        SendPhoto(url);
                    } catch (NullPointerException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                    break;
                default:
                    break;
            }
        }else{
            //操作错误或没有选择图片
            Toast.makeText(this, "operation error", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back:
                GlobalHandler.ChatHanlers.remove(reciever+"");
                ChatRoomActivity.this.finish();
                break;
            case R.id.send_meg:
                if (sendStatus) {
                    PostMessage();
                    break;
                }
                break;
            case R.id.send_photo:
                if(!open){
                    OptionView.setVisibility(View.VISIBLE);
                }else{
                    OptionView.setVisibility(View.GONE);
                }
                open = !open;
                break;
            case R.id.cancel_block:
                addQRCode("取消帮助");
                break;
            case R.id.finish_block:
                addQRCode("结束帮助");
                break;
            case R.id.camera_block:
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CP.CAMERA);
                break;
            case R.id.album_block:
                openAlbum();
                break;
        }
    }

    private void addQRCode(String QRCodeTitle) {
        if(type == CP.ChatByNotice){
            setMessageInfoByNotice();
        }else{
            setMessageInfo();
        }
        chatMessage.setOption(QRCodeTitle);
        getPicUrlAdapter = new GetOptionAdapter(this,this,QRCodeTitle,CP.user.getAvatar(),chatMessage.toString(),DataHandler,chatMessages,chatMessage);
        dialog = DialogPlus.newDialog(this)
                .setAdapter(getPicUrlAdapter)
                .setGravity(Gravity.CENTER)
                .create();
        dialog.show();
    }

    private void openAlbum(){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, CP.ALBUM);
        }catch (Throwable t) {
            Toast.makeText(this, "很抱歉，当前您的手机不支持相册选择功能，请安装相册软件", Toast.LENGTH_SHORT).show();
        }
    }

    public void onTextChanged (CharSequence charSequence,int i, int i1, int i2){
        if (!(sendContent.getText().toString().equals(""))) {
            sendKey.setImageResource(R.drawable.ic_send_true);
            sendKey.setBackground(getResources().getDrawable(R.drawable.button_status_true));
            sendStatus = true;
            if(CP.ChatByWay == CP.ChatByNotice){
                setMessageInfoByNotice();
            }else{
                setMessageInfo();
            }
        } else {
            sendKey.setImageResource(R.drawable.ic_send);
            sendKey.setBackground(getResources().getDrawable(R.drawable.button_status_false));
            sendStatus = false;
        }
    }

    public void afterTextChanged (Editable editable){ }

    public void beforeTextChanged (CharSequence charSequence,int i, int i1, int i2){ }
}

