package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.GetForgetPwdlAdapter;
import com.ubang.huang.ubangapp.async.RegiestUser;
import com.ubang.huang.ubangapp.async.SendVertifyMessage;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.common.WebSite;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.GetParam;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by huang on 2019/4/19.
 * @author huang
 */

public class Regiest extends Activity implements View.OnClickListener,TextWatcher {
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.pwd_again)
    EditText pwd_again;
    TextView pwd_again_status;
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.alarm_point)
    ViewStub stub;
    @BindView(R.id.send_vertify)
    TextView send_vertify;
    @BindView(R.id.vertify)
    EditText vertify;

    Boolean phone_bo = false;
    Boolean pwd_bo = false;
    Boolean pwd_again_bo = false;
    Boolean name_bo = false;
    Boolean vertify_bo = false;
    Boolean nickname_no = false;
    Boolean ButtonCanClick = false;
    Handler UIHandler;
    Boolean isVerifyValue = true;
    Boolean canSendVer = true;
    HandlerThread handlerThread;
    String vertify_code;
    String randomVertify = "";
    Handler DataHandler;
    Dialog dialog;
    DialogPlus dialogPlus;


    View redPointView;


    public static void open(Context context) {
        context.startActivity(new Intent(context, Regiest.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_regist_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandler();
        addStubView();
        addListener();
        addForgetPwd();
    }

    private void addStubView() {
        if(redPointView == null){
            redPointView = stub.inflate();
            pwd_again_status = findViewById(R.id.pwd_again_status);
            redPointView.setVisibility(View.GONE);
        }
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
                    case Signal.Regiest_Succ:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                                Dialog regiestSuc = new Dialog(DialogModel.Regiest_Suc,Regiest.this);
                                regiestSuc.createDialog();
                                regiestSuc.show();

                                LoginActivity.open(Regiest.this);
                                Regiest.this.finish();
                            }
                        });
                        break;
                    case Signal.Regiest_Fail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Dialog regiestSuc = new Dialog(DialogModel.Regiest_Fail,Regiest.this);
                                regiestSuc.createDialog();
                                regiestSuc.show();
                            }
                        });
                        break;
                    case CP.VeritifuSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialogPlus.dismiss();
                                sendVertify();
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
        regist.setOnClickListener(this);
        name.addTextChangedListener(this);
        pwd.addTextChangedListener(this);
        pwd_again.addTextChangedListener(this);
        phone.addTextChangedListener(this);
        nickname.addTextChangedListener(this);
        back.setOnClickListener(this);
        send_vertify.setOnClickListener(this);
    }

    private void judgeChangeBack() {
        if (phone_bo && pwd_bo && pwd_again_bo && name_bo && nickname_no && vertify_bo) {
            setLoginBut(R.drawable.buttonstyle_normal);
            ButtonCanClick = true;
        } else {
            setLoginBut(R.drawable.buttonstyle_gray);
            ButtonCanClick = false;
        }

    }

    private void isTheSamePwd(){
        String pwd_text = pwd_again.getText().toString();
        String pwd_again = pwd.getText().toString();
        if(pwd_text.equals(pwd_again)){
            pwd_again_bo = false;
            pwd_bo = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    redPointView.setVisibility(View.GONE);
                }
            });
        }else{
            pwd_again_bo = true;
            pwd_bo = true;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    redPointView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setLoginBut(int id){
        regist.setBackground(getResources().getDrawable(id));
    }

    private void editIsNull(EditText editText){
        if(!editText.getText().toString().equals("")){
            switch (editText.getId()){
                case R.id.phone:
                    phone_bo = true;
                    break;
                case R.id.pwd:
                    pwd_bo = true;
                    break;
                case R.id.nickname:
                    nickname_no = true;
                    break;
                case R.id.name:
                    name_bo = true;
                    break;
                case R.id.pwd_again:
                    pwd_again_bo = true;
                    break;
                case R.id.vertify:
                    vertify_bo = true;
                    break;

            }
        }else{
            switch (editText.getId()){
                case R.id.phone:
                    phone_bo = false;
                    break;
                case R.id.pwd:
                    pwd_bo = false;
                    break;
                case R.id.name:
                    name_bo = false;
                    break;
                case R.id.nickname:
                    nickname_no = false;
                    break;
                case R.id.pwd_again:
                    pwd_again_bo = false;
                    break;
                case R.id.vertify:
                    vertify_bo = false;
                    break;
            }
        }
    }

    private void CanSendVertify() {
        if(canSendVer) {
            dialogPlus.show();
            canSendVer = false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.regist:
                if(ButtonCanClick){
                    if(!vertify.getText().toString().toLowerCase().equals(randomVertify.toLowerCase())){
                        Dialog vertify = new Dialog(DialogModel.Vertify_Fail,Regiest.this);
                        vertify.createDialog();
                        vertify.show();
                    }else {
                        User user = new User();
                        user.setPassword(pwd.getText().toString());
                        user.setNickname(nickname.getText().toString());
                        user.setPhone(phone.getText().toString());
                        user.setName(name.getText().toString());
                        user.setSex("男");
                        user.setAvatar(WebSite.Avatar);

                        dialog = new Dialog(DialogModel.loading, this);
                        dialog.createDialog();
                        dialog.show();

                        RegiestUser regiestUser = new RegiestUser(DataHandler, user);
                        regiestUser.execute("");
                    }
                }
                break;
            case R.id.back:
                LoginActivity.open(Regiest.this);
                Regiest.this.finish();
                break;
            case R.id.send_vertify:
                CanSendVertify();
                break;
        };
    }

    private void addForgetPwd(){
        GetForgetPwdlAdapter getForgetPwdlAdapter = new GetForgetPwdlAdapter(this,this,DataHandler);
        dialogPlus = DialogPlus.newDialog(this)
                .setAdapter(getForgetPwdlAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.color.alpha)
                .setOverlayBackgroundResource(R.color.alpha)
                .create();
    }

    private void sendVertify(){
        randomVertify =  GetParam.getVertifyCode();
        vertify_code = CP.vertify_code_fist+randomVertify+CP.vertify_code_latter;
        String phone = "15880272131";
        try{
            String url = WebSite.Message_URL + "?mobile=" + phone + "&content=" +
                    CP.Message_content_first+randomVertify+CP.Message_content_latter
                    + "&appkey="
                    + CP.Message_APPKEY;

            SendVertifyMessage sendVertifyMessage = new SendVertifyMessage(DataHandler,url);
            sendVertifyMessage.execute("");
            vertifyValue.start();
            sendVertifyTimer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    CountDownTimer vertifyValue = new CountDownTimer(300 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {}

        @Override
        public void onFinish() {
            isVerifyValue = false;
        }
    };

    CountDownTimer sendVertifyTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            String vertify_content = "重新发送（" + millisUntilFinished / 1000 + "秒）";
            send_vertify.setText(vertify_content);
        }

        @Override
        public void onFinish() {
            send_vertify.setText("发送验证码");
            canSendVer = true;
        }
    };

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        editIsNull(phone);
        editIsNull(pwd);
        editIsNull(nickname);
        editIsNull(name);
        editIsNull(pwd_again);
        editIsNull(vertify);
        judgeChangeBack();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(pwd_again_status != null){
            isTheSamePwd();
        }

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

}
