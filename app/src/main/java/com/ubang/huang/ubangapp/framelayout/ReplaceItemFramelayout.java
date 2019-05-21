package com.ubang.huang.ubangapp.framelayout;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.suke.widget.SwitchButton;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.InputHelpContentActivity;
import com.ubang.huang.ubangapp.activity.InputPlaceActivity;
import com.ubang.huang.ubangapp.activity.PublishHelpActivity;
import com.ubang.huang.ubangapp.adapter.PicAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.AsyncPostHelp;
import com.ubang.huang.ubangapp.async.AsyncPostPic;
import com.ubang.huang.ubangapp.async.PublishHelpUpdate;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PictureURL;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.GetParam;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by huang on 2019/1/22.
 *
 * @author = huangyouxin
 * 无人接受的帮助
 */

public class ReplaceItemFramelayout extends FrameLayout implements View.OnClickListener {
    @BindView(R.id.position)
    TextView position;
    @BindView(R.id.detail_position)
    TextView detail_position;
    @BindView(R.id.block)
    RelativeLayout block;
    TextView end_position;
    TextView end_detail_position;
    @BindView(R.id.help_content)
    TextView help_content;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.seekhelp)
    Button seekhelp;
    @BindView(R.id.stub)
    ViewStub stub;
    RelativeLayout end_block;


    private Context context;
    Handler UIHandler;
    PicAdapter picAdapter;
    View endPositionView;
    Dialog dialog;

    public ReplaceItemFramelayout(final Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        init();
    }

    public void init(){
        LayoutInflater.from(context).inflate(R.layout.seekhelp_publish_accompanying_panel,this);
        ButterKnife.bind(this);
        addHandler();
        initEndPostionView();
        setPanelOption();
        addRecycle();
    }

    private void initEndPostionView() {
        if(endPositionView == null){
            endPositionView = stub.inflate();
            end_block = findViewById(R.id.end_block);
            end_detail_position = findViewById(R.id.end_detail_position);
            end_position = findViewById(R.id.end_position);
            end_block.setOnClickListener(this);
        }
    }

    private void addHandler() {
        CP.ReplaceHandlerThread = new HandlerThread("learn");
        CP.ReplaceHandlerThread.start();
        UIHandler = new Handler();
        CP.ReplaceHandler = new Handler(CP.ReplaceHandlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CP.GetLocalPosition:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setPosition();
                            }
                        });
                        break;
                    case CP.GetEndpostion:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setEndPostion();
                            }
                        });
                        break;
                    case CP.GetURLPic:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                help_content.setText(CP.HelpContent);
                                picAdapter.notifyDataSetChanged();
                                recyclerView.refreshComplete();
                            }
                        });
                        break;
                    case Signal.CuurentPageName:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if("学习".equals(CP.currentPageType)){
                                    endPositionView.setVisibility(View.GONE);
                                }else{
                                    endPositionView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        break;
                    case Signal.ChangeHelpSort:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(CP.isEmegency){
                                    endPositionView.setVisibility(View.VISIBLE);
                                }else{
                                    endPositionView.setVisibility(View.GONE);
                                }
                            }
                        });
                        break;
                    case Signal.PublishHelp_Suc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
//                                Dialog publish = new Dialog(DialogModel.SendFeedBackSuc,getContext());
//                                publish.createDialog();
//                                publish.show();
                                GlobalHandler.Activities.get("PublishHelpActivity").finish();
                                GlobalHandler.Activities.remove("PublishHelpActivity");
                            }
                        });
                        break;
                    case Signal.PublishHelp_Fail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Dialog publish = new Dialog(DialogModel.AnalyzeQRCodeFail,getContext());
                                publish.createDialog();
                                publish.show();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void addRecycle() {
        BaseRecycleView baseRecycleView = new BaseRecycleView(getContext());
        recyclerView = baseRecycleView.addRecycle(recyclerView, CP.Grid,false,true);
        picAdapter = new PicAdapter(CP.picList,getContext());
        recyclerView.setAdapter(picAdapter);
    }

    public void setPosition(){
        position.setText(CP.currentPosition);
        detail_position.setText(CP.currentDetailPosition);
    }
    public void setEndPostion(){
        if( end_position != null){
            end_position.setText(CP.endCurrentPosition);
        }
        if(end_detail_position != null){
            end_detail_position.setText(CP.endCurrentDetailPosition);
        }
    }

    public void setPanelOption(){
        seekhelp.setOnClickListener(this);
        help_content.setOnClickListener(this);
        block.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.block:
                InputPlaceActivity.open(getContext());
                break;
            case R.id.help_content:
                CP.currentPage = CP.ReplacePage;
                InputHelpContentActivity.open(getContext());
                break;
            case R.id.seekhelp:
                if(!(help_content.getText().toString().equals("")) || CP.picList.size()>0) {
                    HelpInfoUpdate help = new HelpInfoUpdate();
                    CP.currentLatLng.toString();
                    help.setStart_position_lat(CP.currentLatLng.latitude);
                    help.setStart_position_lng(CP.currentLatLng.longitude);
                    help.setEnd_position_lat(CP.endCurrentLatLng.latitude);
                    help.setEnd_position_lng(CP.endCurrentLatLng.longitude);
                    help.setContent(help_content.getText().toString());
                    help.setCreate_time(GetParam.getCurrentTiem().toString());
                    help.setRecourse(CP.user.getId());
                    help.setIs_urgent(CP.isUrgent);
                    help.setHas_rule(0);
                    help.setStatus("未开始");
                    help.setId((int) System.currentTimeMillis()*-1);
                    help.setType(CP.currentPageType);
                    if (CP.picList.size() > 0) {
                        help.setHas_picture(1);
                    } else {
                        help.setHas_picture(0);
                    }

                    dialog = new Dialog(DialogModel.loading, getContext());
                    dialog.createDialog();
                    dialog.show();
                    List<PictureURL> pictureURLS = new ArrayList<>();
                    PictureURL pictureURL;
                    for (int i = 0; i < CP.picList.size(); i++) {
                        pictureURL = new PictureURL();
                        pictureURL.setHelp_id(help.getId());
                        pictureURL.setPicture(CP.picList.get(i));
                        pictureURLS.add(pictureURL);
                    }
                    PublishHelpUpdate publishHelpUpdate = new PublishHelpUpdate(CP.ReplaceHandler,pictureURLS,help);
                    publishHelpUpdate.execute("");
                }
                break;
            case R.id.end_block:
                CP.getEndPosition = true;
                InputPlaceActivity.open(getContext());
                break;
        }
    }
}
