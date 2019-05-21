package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.async.PostFeedBack;
import com.ubang.huang.ubangapp.bean.FeedBack;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.HotTagView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBackActivity extends Activity implements View.OnClickListener, TextWatcher {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.idea)
    EditText idea;
    @BindView(R.id.feedback_type)
    HotTagView feedback_type;
    @BindView(R.id.limit)
    TextView limit;

    private String type;
    private Boolean send_bo = false;
    private Boolean type_bo = false;
    private Boolean idea_bo = false;
    Dialog loading;
    private Handler UIHandler;
    private Handler handler;
    HandlerThread handlerThread;
    ArrayList<HotTagView.TagView> tagViews;

    public static void open(Context context){
        context.startActivity(new Intent(context,FeedBackActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_feedback_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandle();
        addListener();
        setTagColor(feedback_type,CP.feedBack_type);
    }

    private void addListener() {
        idea.addTextChangedListener(this);
    }

    private void addHandle() {
        handlerThread = new HandlerThread("FeedBackActivity");
        handlerThread.start();
        UIHandler = new Handler();
        handler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.SendFeedBackFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Dialog dialog = new Dialog(DialogModel.SendFeedBackFail,FeedBackActivity.this);
                                dialog.createDialog();
                                dialog.show();
                            }
                        });
                        break;
                    case Signal.SendFeedBackSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Dialog dialog = new Dialog(DialogModel.SendFeedBackSuc,FeedBackActivity.this);
                                dialog.createDialog();
                                dialog.show();
                                FeedBackActivity.this.finish();
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.submit:
                sendFeedBack();
                break;
            case R.id.back:
                FeedBackActivity.this.finish();
                break;
        }
    }

    private void sendFeedBack() {
        if(send_bo){
            FeedBack feedBack = new FeedBack();
            feedBack.setType(type);
            feedBack.setContent(idea.getText().toString());
            feedBack.setName(CP.user.getId());
            feedBack.setStatus("未采纳");

            PostFeedBack postFeedBack = new PostFeedBack(handler,feedBack);
            postFeedBack.execute("");

            loading = new Dialog(DialogModel.loading,FeedBackActivity.this);
            loading.createDialog();
            loading.show();
        }
    }

    private void buttonStatus(){
        if(type_bo && idea_bo){
            send_bo = true;
            submit.setBackground(getResources().getDrawable(R.drawable.buttonstyle_normal));
        }else{
            send_bo = false;
            submit.setBackground(getResources().getDrawable(R.drawable.buttonstyle_gray));
        }
    }

    private void setTagColor(final HotTagView tagCloudView, ArrayList<String> arrayList) {
        tagCloudView.setTags(arrayList);
        tagViews = (ArrayList<HotTagView.TagView>) tagCloudView.getTagViews();
        clearOthers(tagViews.size()+1);
        tagCloudView.setOnTagClickListener(new HotTagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(tagViews.get(position).getCurrentTextColor() == getResources().getColor(R.color.text_color)) {
                            tagViews.get(position).setTextColor(getResources().getColor(R.color.main_blue_stroke_color));
                            clearOthers(position);

                            type = tagViews.get(position).getText().toString();
                            type_bo = true;
                            buttonStatus();
                        }
                    }
                });
            }
        });
    }

    private void clearOthers(int position){
        for (int i = 0; i < tagViews.size(); i++) {
            if(i != position) {
                tagViews.get(i).setTextColor(getResources().getColor(R.color.text_color));
                tagViews.get(i).setmTextColor(Color.WHITE);
                tagViews.get(i).setmLinePaintColor(Color.WHITE);
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = idea.getText().toString();
        String result = idea.getText().toString().length()+"/200";
        limit.setText(result);
        if(!content.equals("")){
            idea_bo = true;
            buttonStatus();
        }else{
            idea_bo = false;
            buttonStatus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
