package com.ubang.huang.ubangapp.framelayout;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.MyHelpListAdapter;
import com.ubang.huang.ubangapp.async.GetPersonSeekHelp;
import com.ubang.huang.ubangapp.bean.HelpInfo;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PersonSeekHelp;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/22.
 *
 * @author = huangyouxin
 * 无人接受的帮助
 */

public class UnHelpFramelayout extends FrameLayout {

    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;

    BaseRecycleView baseRecycleView;
    MyHelpListAdapter myHelpListAdapter;
    private Context context;
    Handler UIhandler;
    Handler DataHandler;
    HandlerThread handlerThread;
    List<HelpInfoUpdate> helpInfoUpdateList = new ArrayList<>();
    PersonSeekHelp personSeekHelp;

    public UnHelpFramelayout(final Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        init();
    }

    public void init(){
        LayoutInflater.from(context).inflate(R.layout.comment_cycler,this);
        ButterKnife.bind(this);
        addHanler();
        addRecyclerView();
        addDate(true);
    }

    private void addHanler() {
        handlerThread = new HandlerThread("");
        handlerThread.start();
        UIhandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Signal.GetPersonSeekHelp:
                        GlobalHandler.Activities.get("MainActivity").runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mRecyclerView.refreshComplete();
                                    myHelpListAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                }
            }
        };
    }

    private void addDate(Boolean clean) {
        personSeekHelp = new PersonSeekHelp();
        personSeekHelp.setUserID(CP.user.getId());
        personSeekHelp.setStatus("未开始");

        GetPersonSeekHelp getPersonSeekHelp = new GetPersonSeekHelp(DataHandler,personSeekHelp,helpInfoUpdateList,clean);
        getPersonSeekHelp.execute("");
    }

    public void addRecyclerView(){
        baseRecycleView = new BaseRecycleView(context);
        mRecyclerView = baseRecycleView.addRecycle(mRecyclerView,CP.Linear,true,true);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                addDate(true);
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecyclerView != null) {
                            addDate(false);
                            mRecyclerView.loadMoreComplete();
                        }
                    }
                }, 1000);
            }
        });
        myHelpListAdapter = new MyHelpListAdapter(helpInfoUpdateList,context);
        mRecyclerView.setAdapter(myHelpListAdapter);
    }


}
