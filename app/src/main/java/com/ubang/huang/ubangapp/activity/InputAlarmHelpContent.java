package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.suke.widget.SwitchButton;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.GetPicUrlAdapter;
import com.ubang.huang.ubangapp.adapter.PicAdapter;
import com.ubang.huang.ubangapp.async.GetUser;
import com.ubang.huang.ubangapp.async.ModifyAlarmContent;
import com.ubang.huang.ubangapp.bean.AlarmContent;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.GetParam;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/7.
 * @author huang
 * 输入求助内容Activity
 */

public class InputAlarmHelpContent extends Activity{

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.edit_help)
    EditText edit_help;

    Handler UIHandler;
    HandlerThread handlerThread;
    Handler DataHandler;

    public static void open(Context context) {
        context.startActivity(new Intent(context, InputAlarmHelpContent.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_publish_alarm_contetnt);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandler();
        addTextViewOption();
        edit_help.setText(CP.contentList.get(CP.alarm_content_num).getAlarm_content());
    }

    private void addHandler() {
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()){
            @Override
            //消息处理的操作
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.Modefy_Fail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        break;
                    case Signal.Modify_Alarm_Suc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = Signal.Change_Alarm_content;
                                CP.alarmHeplHandler.sendMessage(message);
                                InputAlarmHelpContent.this.finish();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void addTextViewOption() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputAlarmHelpContent.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmContent alarmContent = new AlarmContent();
                alarmContent.setAlarm_content(edit_help.getText().toString());
                alarmContent.setNumber(CP.alarm_content_num);
                alarmContent.setUser_phone(CP.user.getPhone());
                ModifyAlarmContent modifyAlarmContent = new ModifyAlarmContent(DataHandler,alarmContent);
                modifyAlarmContent.execute("");

            }
        });

    }

}
