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
import com.ubang.huang.ubangapp.adapter.HelpOthersListAdapter;
import com.ubang.huang.ubangapp.adapter.MyHelpListAdapter;
import com.ubang.huang.ubangapp.adapter.MyHelpedListAdapter;
import com.ubang.huang.ubangapp.async.GetMyHelp;
import com.ubang.huang.ubangapp.async.GetOthersSeekHelp;
import com.ubang.huang.ubangapp.async.GetPersonSeekHelp;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PersonSeekHelp;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/22.
 *
 * @author = huangyouxin
 * 我已结束的帮助
 */

public class MyHelpedFramelayout extends FrameLayout {

    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;

    BaseRecycleView baseRecycleView;
    private Context context;
    Handler UIhandler;
    Handler DataHandler;
    HandlerThread handlerThread;
    MyHelpedListAdapter helpOthersListAdapter;
    List<SeekHelpInfo> helpInfoUpdateList = new ArrayList<>();

    public MyHelpedFramelayout(final Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        init();
    }

    public void init(){
        LayoutInflater.from(context).inflate(R.layout.comment_cycler,this);
        ButterKnife.bind(this);
        addHanler();
        addRecyclerView();
        addDate();
    }

    public void addRecyclerView(){
        baseRecycleView = new BaseRecycleView(context);
        mRecyclerView = baseRecycleView.addRecycle(mRecyclerView,CP.Linear,true,true);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addDate();
                        mRecyclerView.loadMoreComplete();
                        helpOthersListAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        helpOthersListAdapter = new MyHelpedListAdapter(helpInfoUpdateList,getContext());
        mRecyclerView.setAdapter(helpOthersListAdapter);
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
                    case Signal.GetMyHelp:
                        GlobalHandler.Activities.get("MyHelpLIst").runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.loadMoreComplete();
                                helpOthersListAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                }
            }
        };
    }

    private void addDate() {
        PersonSeekHelp  personSeekHelp = new PersonSeekHelp();
        personSeekHelp.setUserID(CP.user.getId());
        personSeekHelp.setStatus("结束");

        GetMyHelp getMyHelp = new GetMyHelp(DataHandler,personSeekHelp,helpInfoUpdateList);
        getMyHelp.execute("");
    }
}
