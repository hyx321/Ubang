package com.ubang.huang.ubangapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.GetForgetPwdlAdapter;
import com.ubang.huang.ubangapp.async.ForgetPwd;
import com.ubang.huang.ubangapp.async.GetUser;
import com.ubang.huang.ubangapp.async.LoginUser;
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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by huang on 2019/4/16.
 * @author huang
 */

@RuntimePermissions
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{

    @BindView(R.id.phone)
    EditText name_text;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.loginbtn)
    Button loginbtn;
    EditText pwd_again;
    EditText vertify;
    TextView send_vertify;
    TextView pwd_again_status;
    @BindView(R.id.sub)
    ViewStub sub;
    ViewStub alarm_point;

    Boolean phone_bo = false;
    Boolean pwd_bo = false;
    Boolean pwd_again_bo = false;
    Boolean vertify_bo = false;
    Boolean ButtonCanClick = false;
    Boolean isVerifyValue = true;
    Boolean forget_pwd = true;
    Boolean canSendVer = true;
    DialogPlus dialogPlus;
    int status = 0;
    String randomVertify = "";
    Handler UIHandler;
    HandlerThread handlerThread;
    Handler DataHandler;
    Dialog dialog;
    Dialog modifyDialog;
    String vertify_code;
    View modifyView;
    View redPotinView;

    public static void open(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setupWindowAnimations();
        ButterKnife.bind(this);
        init();
    }

    @TargetApi(21)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    private void init() {
        addHandler();
        addListener();
        addForgetPwd();
        getGPSPermission();
    }

    private void addListener() {
        loginbtn.setOnClickListener(this);
        forget.setOnClickListener(this);
        regist.setOnClickListener(this);
        name_text.addTextChangedListener(this);
        password.addTextChangedListener(this);
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
                    case CP.VeritifuSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialogPlus.dismiss();
                                sendVertify();
                            }
                        });
                        break;
                    case Signal.Login_Succ:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                User user = new User();
                                user.setPhone(name_text.getText().toString());
                                user.setNickname(name_text.getText().toString());
                                GetUser getUser = new GetUser(DataHandler,user,CP.RefreshUser);
                                getUser.execute("");
                                MainActivity.open(LoginActivity.this);
                                dialog.dismiss();
                                LoginActivity.this.finish();
                            }
                        });
                        break;
                    case Signal.Login_Fail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Dialog loginFail = new Dialog(DialogModel.Login_Fail,LoginActivity.this);
                                loginFail.createDialog();
                                loginFail.show();

                            }
                        });
                        break;
                    case Signal.Modefy_Suc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                modifyDialog.dismiss();

                                Dialog ModifySuc = new Dialog(DialogModel.Modefy_Suc,LoginActivity.this);
                                ModifySuc.createDialog();
                                ModifySuc.show();

                                setLoginVisible();
                            }
                        });
                        break;
                    case Signal.Modefy_Fail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                modifyDialog.dismiss();

                                Dialog ModifyFail = new Dialog(DialogModel.Modefy_Fail,LoginActivity.this);
                                ModifyFail.createDialog();
                                ModifyFail.show();

                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void setLoginBut(int id){
        loginbtn.setBackground(getResources().getDrawable(id));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.forget:
                isDisplayPwdAgain();
                break;
            case R.id.loginbtn:
                CanClick();
                break;
            case R.id.send_vertify:
                CanSendVertify();
                break;
            case R.id.regist:
                Regiest.open(this);
                break;
        }
    }

    private void CanSendVertify() {
        if(canSendVer) {
            dialogPlus.show();
            canSendVer = false;
        }
    }

    private void CanClick() {
        if(ButtonCanClick){
            LoginOrForget();
        }
    }

    private void LoginOrForget() {
        if(forget_pwd){
            User user = new User();
            user.setPhone(name_text.getText().toString());
            user.setNickname(name_text.getText().toString());
            user.setPassword(password.getText().toString());

            dialog = new Dialog(DialogModel.loading,this);
            dialog.createDialog();
            dialog.show();

            LoginUser loginUser = new LoginUser(DataHandler,user);
            loginUser.execute("");
        }else{
            if(vertify.getText().toString().toLowerCase().equals(randomVertify.toLowerCase())){
                User user = new User();
                user.setPhone(name_text.getText().toString());
                user.setPassword(password.getText().toString());

                modifyDialog = new Dialog(DialogModel.loading,this);
                modifyDialog.createDialog();
                modifyDialog.show();

                ForgetPwd forgetPwd = new ForgetPwd(DataHandler,user);
                forgetPwd.execute("");
            }else{
                Dialog vertify = new Dialog(DialogModel.Vertify_Fail,LoginActivity.this);
                vertify.createDialog();
                vertify.show();
            }
        }

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
    private void isDisplayPwdAgain(){
        getStub();
    }

    private void  getStub(){
        if (modifyView == null) {
            modifyView = sub.inflate();
            pwd_again = findViewById(R.id.pwd_again);
            vertify = findViewById(R.id.vertify);
            send_vertify = findViewById(R.id.send_vertify);

            pwd_again.addTextChangedListener(this);
            vertify.addTextChangedListener(this);
            send_vertify.setOnClickListener(this);

            alarm_point = findViewById(R.id.alarm_point);
            if(redPotinView == null ){
                redPotinView = alarm_point.inflate();
                pwd_again_status = findViewById(R.id.pwd_again_status);
            }
        }
        if (forget_pwd) {
            forget_pwd = false;
            setModifyVisible();
            modifyView.setVisibility(View.VISIBLE);
        } else {
            forget_pwd = true;
            setLoginVisible();
            modifyView.setVisibility(View.GONE);
        }
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

    public void setModifyVisible(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                forget.setText("返回");
                name_text.setText("");
                password.setText("");
                loginbtn.setText("修改密码");
                status = 1;
            }
        });

    }
    private void setLoginVisible(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                forget.setText("忘记密码");
                name_text.setText("");
                password.setText("");
                pwd_again.setText("");
                vertify.setText("");
                status = 0;
            }
        });

    }

    private void editIsNull(EditText editText){
        if(!editText.getText().toString().equals("")){
            switch (editText.getId()){
                case R.id.phone:
                    phone_bo = true;
                    break;
                case R.id.password:
                    pwd_bo = true;
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
                case R.id.password:
                    pwd_bo = false;
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

    private void judgeChangeBack() {
        if(status == 0){
            if(phone_bo && pwd_bo){
                setLoginBut(R.drawable.buttonstyle_normal);
                ButtonCanClick = true;
            }else{
                setLoginBut(R.drawable.buttonstyle_gray);
                ButtonCanClick = false;
            }
        }else{
            if(phone_bo && pwd_bo && pwd_again_bo && vertify_bo){
                setLoginBut(R.drawable.buttonstyle_normal);
                ButtonCanClick = true;
            }else{
                setLoginBut(R.drawable.buttonstyle_gray);
                ButtonCanClick = false;
            }
        }

    }
    private void isTheSamePwd(){
        String pwd = pwd_again.getText().toString();
        String pwd_again = password.getText().toString();
        if(pwd.equals(pwd_again)){
            pwd_bo = false;
            pwd_again_bo = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    alarm_point.setVisibility(View.GONE);
                }
            });
        }else{
            pwd_bo = true;
            pwd_again_bo = true;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    alarm_point.setVisibility(View.VISIBLE);
                }
            });
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
            String vertify_content = "发送验证码（" + millisUntilFinished / 1000 + "秒）";
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
        editIsNull(name_text);
        editIsNull(password);
        if(pwd_again != null){
            editIsNull(pwd_again);
        }
        if(vertify != null){
            editIsNull(vertify);
        }

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

    @TargetApi(23)
    private void getGPSPermission(){
        LocationManager lm;
        String[] permisstion = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for(int i=0;i<permisstion.length;i++){
            if (ContextCompat.checkSelfPermission(this,permisstion[i]) != PackageManager.PERMISSION_GRANTED) {
                LoginActivityPermissionsDispatcher.GteOptionWithCheck(LoginActivity.this);
            }
        }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void GetGPS() {
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void GteOption() {
    }
}
