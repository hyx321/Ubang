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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.PlaceListAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetPositionInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.BaseRecycleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/6.
 * @author huang
 * 输入求助位置Activity
 */

public class InputPlaceActivity extends Activity{

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.position)
    EditText position;
    @BindView(R.id.recyclerview)
    XRecyclerView xRecyclerView;
    @BindView(R.id.city)
    TextView city;

    private SuggestionSearch suggestionSearch;
    private PlaceListAdapter placeListAdapter;
    Handler UIHandler;
    HandlerThread handlerThread;
    Handler DataHandler;
    private GeoCoder geoCoder;

    public static void open(Context context) {
        context.startActivity(new Intent(context, InputPlaceActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_publish_position_activity);
        ButterKnife.bind(this);
        CP.inputPlaceActivity = this;
        init();
    }

    private void init() {
        addHandle();
        addText();
        addSug();
        addGeo();
        addRecycle();
        addEditListener();
        addCancelListenter();
    }

    private void addGeo() {
        geoCoder = GeoCoder.newInstance();
    }

    private void addText() {
        city.setText(CP.currentCity);
        cancel.setText("取消");
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
                    case CP.Get_Position:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                xRecyclerView.loadMoreComplete();
                                placeListAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void addSug() {
        suggestionSearch = SuggestionSearch.newInstance();
    }

    private void addEditListener() {

        position.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addAsyncTask(position.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addAsyncTask(String position){
        AsyncGetPositionInfo getPositionInfo = new AsyncGetPositionInfo(position,DataHandler,suggestionSearch,geoCoder);
        getPositionInfo.execute("");
    }

    private void addRecycle() {
        BaseRecycleView baseRecycleView = new BaseRecycleView(this);
        xRecyclerView = baseRecycleView.addRecycle(xRecyclerView, CP.Linear,false,true);

        placeListAdapter = new PlaceListAdapter(CP.suggestList,geoCoder,CP.suggestInfoList);
        xRecyclerView.setAdapter(placeListAdapter);
    }

    private void addCancelListenter() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputPlaceActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        suggestionSearch.destroy();
        geoCoder.destroy();
    }
}
