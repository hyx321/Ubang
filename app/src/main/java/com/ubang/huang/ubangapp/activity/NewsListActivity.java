package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.show.api.ShowApiRequest;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.NewsCampusListAdapter;
import com.ubang.huang.ubangapp.adapter.PicAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetNews;
import com.ubang.huang.ubangapp.async.GetCampusSafeList;
import com.ubang.huang.ubangapp.bean.CampusSafe;
import com.ubang.huang.ubangapp.bean.SendNews;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/10.
 * @author huang
 */

public class NewsListActivity extends Activity{

    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.back)
    ImageView back;

    Handler UIHandler;
    HandlerThread handlerThread;
    Handler DataHandler;
    NewsCampusListAdapter newsCampusListAdapter;
    List<CampusSafe> campusSafeList = new ArrayList<>();
    int start = 0;

    public static void open(Context context) {
        context.startActivity(new Intent(context, NewsListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_service_campus);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandle();
        addRecycle();
        getNewsList();
        addBackListener();
    }

    private void addBackListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsListActivity.this.finish();
            }
        });
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
                    case Signal.GetCampusList:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                start += 10;
                                recyclerView.refreshComplete();
                                newsCampusListAdapter.notifyDataSetChanged();
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
        BaseRecycleView baseRecycleView = new BaseRecycleView(this);
        recyclerView = baseRecycleView.addRecycle(recyclerView, CP.Linear,false,true);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                getNewsList();
            }
        });

        newsCampusListAdapter = new NewsCampusListAdapter(campusSafeList,this);
        recyclerView.setAdapter(newsCampusListAdapter);
    }

    private void getNewsList(){
        GetCampusSafeList getCampusSafeList =new GetCampusSafeList(start,DataHandler,campusSafeList);
        getCampusSafeList.execute("");
    }

}
