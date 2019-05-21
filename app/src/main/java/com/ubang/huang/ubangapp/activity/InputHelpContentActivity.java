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
import com.ubang.huang.ubangapp.bean.User;
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

public class InputHelpContentActivity extends Activity{

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
    List<String> list = new ArrayList<>();

    public static void open(Context context) {
        context.startActivity(new Intent(context, InputHelpContentActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_publish_content_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        CP.picList.clear();
        addTextViewOption();
        addRecycle();
        addAlbumOption();
    }



    private void addAlbumOption() {
        getPicUrlAdapter = new GetPicUrlAdapter(InputHelpContentActivity.this,this);
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
        picAdapter = new PicAdapter(CP.picList,this);
        recyclerview.setAdapter(picAdapter);
    }

    private void addTextViewOption() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputHelpContentActivity.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.HelpContent = edit_help.getText().toString();

                if(CP.ReplaceHandler != null) {
                    GetParam.SendMessage(CP.ReplaceHandler, CP.GetURLPic);
                }

                InputHelpContentActivity.this.finish();
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
                        Uri uri = intent.getData();
                        list.add(uri.toString());
                        CP.picList.add(GetParam.getRealFilePath(this,uri));;
                        dialog.dismiss();
                        recyclerview.loadMoreComplete();
                        picAdapter.notifyDataSetChanged();

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
