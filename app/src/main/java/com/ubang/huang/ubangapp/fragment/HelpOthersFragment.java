package com.ubang.huang.ubangapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.viewpager.SViewPager;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.MyHelpLIst;
import com.ubang.huang.ubangapp.activity.RoundHelps;
import com.ubang.huang.ubangapp.adapter.GetHeplSortAdapter;
import com.ubang.huang.ubangapp.adapter.HelpOthersListAdapter;
import com.ubang.huang.ubangapp.adapter.MyHelpListAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetHelpList;
import com.ubang.huang.ubangapp.async.GetOthersSeekHelp;
import com.ubang.huang.ubangapp.async.GetPersonSeekHelp;
import com.ubang.huang.ubangapp.base.BaseMainFragment;
import com.ubang.huang.ubangapp.bean.HelpInfoFilter;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PersonSeekHelp;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.IndicatorAndViewPager;
import com.ubang.huang.ubangapp.util.NeverCarshXRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 帮助大厅
 */

public class HelpOthersFragment extends BaseMainFragment implements View.OnClickListener{

    XRecyclerView mRecyclerView;
    NeverCarshXRecyclerView recyclerView;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.round_map)
    TextView round_map;
    @BindView(R.id.helpother_viewstub)
    ViewStub helpother_viewstub;
    ImageView myhelp;

    View helpContentView;
    BaseRecycleView baseRecycleView;
    HelpOthersListAdapter helpOthersListAdapter;
    GetHeplSortAdapter getHeplSortAdapter;
    DialogPlus dialogPlus;
    private Handler UIHandler;
    private HandlerThread handlerThread;
    private Handler DataHandler;
    View view;
    List<SeekHelpInfo> helpInfoUpdateList = new ArrayList<>();
    int start = 0;

    public static HelpOthersFragment newInstance() {
        Bundle args = new Bundle();
        HelpOthersFragment fragment = new HelpOthersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.helpothers_comment_cycler,container,false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void init(){
        addHandler();
        dealDate();
        addStubView();
    }

    private void addStubView() {
        if(helpContentView == null){
            helpContentView = helpother_viewstub.inflate();
            recyclerView = view.findViewById(R.id.neverRecyclerview);
            myhelp = view.findViewById(R.id.myhelp);
            myhelp.setOnClickListener(this);
        }
        helpContentView.setVisibility(View.VISIBLE);
        addRecyclerView1();
        addFloatButton();
        addLabelListener();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    private void dealDate() {
        PersonSeekHelp  personSeekHelp = new PersonSeekHelp();
        personSeekHelp.setUserID(CP.user.getId());
        personSeekHelp.setStatus("未开始");

        GetOthersSeekHelp getPersonSeekHelp = new GetOthersSeekHelp(DataHandler,personSeekHelp,helpInfoUpdateList);
        getPersonSeekHelp.execute("");
    }

    private void addLabelListener() {
        label.setOnClickListener(this);
        round_map.setOnClickListener(this);
    }

    private void addFloatButton() {
        getHeplSortAdapter = new GetHeplSortAdapter(getActivity(),getContext(),DataHandler,helpInfoUpdateList,start);
        dialogPlus = DialogPlus.newDialog(getContext())
                .setAdapter(getHeplSortAdapter)
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

    public void addRecyclerView1(){
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setLoadingListener(new NeverCarshXRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                helpInfoUpdateList.clear();
                dealDate();
            }

            @Override
            public void onLoadMore() {
                recyclerView.loadMoreComplete();
                helpOthersListAdapter.notifyDataSetChanged();
            }
        });
        CP.DisPlay = 0;
        helpOthersListAdapter = new HelpOthersListAdapter(helpInfoUpdateList,getContext());
        recyclerView.setAdapter(helpOthersListAdapter);
    }

    private void addHandler() {
        handlerThread = new HandlerThread("");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()){
            @Override
            //消息处理的操作
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CP.GetHelpListFilter:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    recyclerView.refreshComplete();
                                    helpOthersListAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                        break;
                    case Signal.GetPersonSeekHelp:
                        new Handler().post(new Runnable() {
                            @Override
                            public void run()
                            {
                                try{
                                    recyclerView.refreshComplete();
                                    helpOthersListAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.myhelp:
                CP.DisPlay = 1;
                MyHelpLIst.open(getContext());
                break;
            case R.id.label:
                dialogPlus.show();
                break;
            case R.id.round_map:
                RoundHelps.open(getContext());
                break;
        }
    }
}
