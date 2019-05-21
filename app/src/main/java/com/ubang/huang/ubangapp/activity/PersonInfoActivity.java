package com.ubang.huang.ubangapp.activity;

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.ChooseSexAdapter;
import com.ubang.huang.ubangapp.adapter.GetCircleButtonAdapter;
import com.ubang.huang.ubangapp.adapter.GetPicUrlAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.AsyncPostAvatar;
import com.ubang.huang.ubangapp.async.AsyncPostPic;
import com.ubang.huang.ubangapp.async.GetUser;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.GetParam;
import com.ubang.huang.ubangapp.util.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonInfoActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.avatar_block)
    RelativeLayout avatar_block;
    @BindView(R.id.name_block)
    RelativeLayout name_block;
    @BindView(R.id.nickname_block)
    RelativeLayout nickname_block;
    @BindView(R.id.phone_block)
    RelativeLayout phone_block;
    @BindView(R.id.sex_block)
    RelativeLayout sex_block;
    @BindView(R.id.signature_block)
    RelativeLayout signature_block;
    @BindView(R.id.qq_block)
    RelativeLayout qq_block;
    @BindView(R.id.wechat_block)
    RelativeLayout wechat_block;
    @BindView(R.id.email_block)
    RelativeLayout email_block;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.qq)
    TextView qq;
    @BindView(R.id.wechat)
    TextView wechat;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.signature)
    TextView signature;

    DialogPlus dialogPlus;
    Handler handler;
    Handler UIhandler;
    HandlerThread handlerThread;
    GetPicUrlAdapter getPicUrlAdapter;
    DialogPlus avatarPlus;

    public static void open(Context context){
        context.startActivity(new Intent(context,PersonInfoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_person_info_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandle();
        setInfo();
        addListener();
        addSexLayout();
        addAlbumOption();
    }

    private void addListener() {
        avatar_block.setOnClickListener(this);
        name_block.setOnClickListener(this);
        nickname_block.setOnClickListener(this);
        phone_block.setOnClickListener(this);
        sex_block.setOnClickListener(this);
        signature_block.setOnClickListener(this);
        qq_block.setOnClickListener(this);
        wechat_block.setOnClickListener(this);
        email_block.setOnClickListener(this);
        signature_block.setOnClickListener(this);
    }

    private void setInfo() {
        Glide.with(this).load(CP.user.getAvatar()).into(avatar);
        name.setText(CP.user.getName());
        nickname.setText(CP.user.getNickname());
        phone.setText(CP.user.getPhone());
        sex.setText(CP.user.getSex());
        signature.setText(CP.user.getSignature());

        if(CP.user.getQq()!=null){
            qq.setText(CP.user.getQq());
        }
        if(CP.user.getWechat()!=null){
            wechat.setText(CP.user.getWechat());
        }
        if(CP.user.getEmail()!=null){
            email.setText(CP.user.getEmail());
        }
    }

    private void addSexLayout() {
        ChooseSexAdapter chooseSexAdapter = new ChooseSexAdapter(handler,this);
        dialogPlus = DialogPlus.newDialog(PersonInfoActivity.this)
                .setAdapter(chooseSexAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setGravity(Gravity.CENTER)
               /* .setContentBackgroundResource(R.color.alpha)
                .setOverlayBackgroundResource(R.color.alpha)*/
                .create();
    }

    private void addHandle() {
        handlerThread = new HandlerThread("PublishHelpActivity");
        handlerThread.start();
        UIhandler = new Handler();
        handler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.SetSex:
                        UIhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialogPlus.dismiss();
                                sex.setText(CP.user.getSex());
                            }
                        });
                        break;
                    case Signal.PostAvatarFail:
                        UIhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialogPlus.dismiss();
                                sex.setText(CP.user.getSex());
                            }
                        });
                        break;
                    case Signal.PostAvatarSuc:
                        UIhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                GetUser getUser = new  GetUser(handler,CP.user,CP.RefreshUserAndNotice);
                                getUser.execute();
                            }
                        });
                        break;
                    case Signal.RefreshAndNotice:
                        UIhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setInfo();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private Bitmap getBitmap(String url){
        try {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(Uri.parse(url), filePathColumns, null, null, null);
            c.moveToFirst();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            return BitmapFactory.decodeFile(c.getString(c.getColumnIndex(filePathColumns[0])));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void addAlbumOption() {
        getPicUrlAdapter = new GetPicUrlAdapter(PersonInfoActivity.this,this);
        avatarPlus = DialogPlus.newDialog(this)
                .setAdapter(getPicUrlAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setGravity(Gravity.CENTER)
                .create();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.name_block:
                break;
            case R.id.avatar_block:
                avatarPlus.show();
                break;
            case R.id.nickname_block:
                break;
            case R.id.phone_block:
                break;
            case R.id.sex_block:
                break;
            case R.id.qq_block:
                break;
            case R.id.email_block:
                break;
            case R.id.wechat_block:
                break;
            case R.id.signature_block:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CP.CAMERA:

                    break;
                case CP.ALBUM:
                    try {

                        //获取选中文件的定位符
                        Uri uri = intent.getData();
                        avatar.setImageBitmap(getBitmap(uri.toString()));
                        avatarPlus.dismiss();

                        String url = GetParam.getRealFilePath(PersonInfoActivity.this,uri);
                        AsyncPostAvatar postAvatar = new AsyncPostAvatar(handler,url,CP.user.getId());
                        postAvatar.execute("");
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
    }
}
