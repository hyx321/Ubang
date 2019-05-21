package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.ServiceAdapter;
import com.ubang.huang.ubangapp.async.AsyncService;
import com.ubang.huang.ubangapp.bean.ServiceMessage;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.GetParam;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/20.
 *
 * @author = huangyouxin
 * 图灵机器人实现的客服聊天
 */

public class RobotActivity extends Activity{

    ServiceAdapter serviceAdapter;
    BaseRecycleView baseRecycleView;
    Boolean sendStatus = false;
    DialogPlus dialog;
    Handler UIHandler;
    HandlerThread handlerThread;
    Handler DataHandler;
    AsyncService asyncService;

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

    public static void open(Context context) {
        context.startActivity(new Intent(context, RobotActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_service);
        init();
    }

    private void init(){
        ButterKnife.bind(this);
        addHandle();
        addRecyclerView();
        addEdite();
        addBack();
        if(CP.ServiceList.size()==1){
            CP.ServiceList.clear();
        }
        ServiceGreeting(CP.Service,CP.Service_Greeting);
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
                    case CP.Send_Message:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void addBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotActivity.this.finish();
            }
        });
    }


    /**
     * 输入框监听
     */
    private void addEdite() {
        sendContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(sendContent.getText().toString().equals(""))){
                    sendKey.setImageResource(R.drawable.ic_send_true);
                    sendKey.setBackground(getResources().getDrawable(R.drawable.button_status_true));
                    sendStatus = true;

                }else{
                    sendKey.setImageResource(R.drawable.ic_send);
                    sendKey.setBackground(getResources().getDrawable(R.drawable.button_status_false));
                    sendStatus = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    sendMessage();
                }
                return false;
            }
        });
    }

    /**
     * 聊天内容页面
     */
    private void addRecyclerView(){
        baseRecycleView = new BaseRecycleView(this);
        mRecyclerView = baseRecycleView.addRecycle(mRecyclerView,"Linear",false,true);
        sendKey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendMessage();
            }
        });


        serviceAdapter = new ServiceAdapter(CP.ServiceList,this);
        mRecyclerView.setAdapter(serviceAdapter);
        mRecyclerView.scrollToPosition(CP.ServiceList.size());
    }


    public void sendMessage(){
        if(sendStatus) {
            String printContent = sendContent.getText().toString();
            sendContent.setText("");
            ServiceGreeting(CP.User,printContent);
            refresh();
            getQuestion(printContent);
        }
    }
    private void getQuestion(String question) {
        asyncService = new AsyncService(DataHandler,question);
        asyncService.execute("");
    }
    private void refresh(){
        serviceAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(CP.ServiceList.size());
    }

    private void ServiceGreeting(int type,String content){
        ServiceMessage message = new ServiceMessage();
        String time = GetParam.getCurrentTiem().toString();
        message.setTime(time.substring(0,time.length()-3));
        message.setWhoSend(type);
        message.setContent(content);
        CP.ServiceList.add(message);
    }


}
