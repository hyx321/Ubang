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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.GetPicUrlAdapter;
import com.ubang.huang.ubangapp.adapter.PicAdapter;
import com.ubang.huang.ubangapp.async.ModifyHelpInfo;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.common.BeanParam;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.GetParam;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/7.
 * @author huang
 * 输入求助内容Activity
 */

public class ModifyHelpContentActivity extends Activity{

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerview;
    @BindView(R.id.ablum_block)
    RelativeLayout ablum_block;
    @BindView(R.id.edit_help)
    EditText edit_help;

    BaseRecycleView baseRecycleView;
    PicAdapter picAdapter;
    GetPicUrlAdapter getPicUrlAdapter;
    DialogPlus dialog;
    List<String> picList = new ArrayList<>();
    private Handler UIHandler;
    private Handler DataHandler;
    HandlerThread handlerThread;
    Dialog loading;

    public static void open(Context context) {
        context.startActivity(new Intent(context, ModifyHelpContentActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_publish_content_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandle();
        addTextViewOption();
        addRecycle();
        addAlbumOption();
    }

    private void addHandle() {
        handlerThread = new HandlerThread("PublishHelpActivity");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.ModifyHelpSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Message message = new Message();
                                message.what = Signal.ModifyHelpSuc;
                                CP.HelpInfoHandler.sendMessage(message);
                                BeanParam.HelpInfo.setContent(edit_help.getText().toString());
                                ModifyResult(DialogModel.ModefyHelpSuc);

                            }
                        });
                        break;
                    case Signal.ModifyHelpFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                ModifyResult(DialogModel.ModefyHelpFail);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void ModifyResult(int resultCode){
        Dialog dialog = new Dialog(resultCode,ModifyHelpContentActivity.this);
        dialog.createDialog();
        dialog.show();
        ModifyHelpContentActivity.this.finish();
    }

    private void addAlbumOption() {
        getPicUrlAdapter = new GetPicUrlAdapter(ModifyHelpContentActivity.this,this);
        dialog = DialogPlus.newDialog(this)
                .setAdapter(getPicUrlAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setGravity(Gravity.CENTER)
                .create();
    }

    private void addRecycle() {
        baseRecycleView = new BaseRecycleView(this);
        recyclerview = baseRecycleView.addRecycle(recyclerview, CP.Grid,false,true);
        picAdapter = new PicAdapter(picList,this);
        recyclerview.setAdapter(picAdapter);
    }

    private void addTextViewOption() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyHelpContentActivity.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpInfoUpdate help = new HelpInfoUpdate();
                help.setId(CP.help_id);
                help.setContent(edit_help.getText().toString());
                if(picList.size() > 0){
                    help.setHas_picture(1);
                }
                ModifyHelpInfo modifyHelpInfo = new ModifyHelpInfo(DataHandler,help);
                modifyHelpInfo.execute("");
                loading = new Dialog(DialogModel.loading,ModifyHelpContentActivity.this);
                loading.createDialog();
                loading.show();

                ModifyHelpContentActivity.this.finish();
            }
        });
        ablum_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
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
                        picList.add(uri.toString());
                        dialog.dismiss();
                        picAdapter.notifyDataSetChanged();
                        recyclerview.loadMoreComplete();
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
