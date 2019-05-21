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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.viewpager.SViewPager;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.DangerListAdapter;
import com.ubang.huang.ubangapp.adapter.PersonInfoAdapter;
import com.ubang.huang.ubangapp.async.GetAlarmContent;
import com.ubang.huang.ubangapp.bean.PersonInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.GetParam;
import com.ubang.huang.ubangapp.util.IndicatorAndViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/22.
 *
 * @author = huangyouxin
 *
 */

public class SeekHelpFrameLayout extends FrameLayout {

    @BindView(R.id.unhelp_block)
    RelativeLayout unhelp_block;
    @BindView(R.id.helped_block)
    RelativeLayout helped_block;
    @BindView(R.id.helping_block)
    RelativeLayout helping_block;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.recyclerview_detail)
    XRecyclerView recyclerview_detail;
    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.helplist_stub)
    ViewStub helplist_stub;

    ScrollIndicatorView scrollIndicatorView;
    SViewPager viewPager;
    TextView cancel;
    IndicatorAndViewPager in;
    IndicatorViewPager indicatorViewPager;
    BaseRecycleView baseRecycleView;
    BaseRecycleView baseRecycleView_detail;
    DangerListAdapter dangerListAdapter;
    Handler UIHandler;
    PersonInfoAdapter personInfoAdapter;
    private Context context;
    PersonInfo personInfo;
    View helplistView;

    public SeekHelpFrameLayout(final Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        init();
    }

    public void init(){
        LayoutInflater.from(context).inflate(R.layout.seekhelp_help_content_update,this);
        ButterKnife.bind(this);
        setBlockListen(unhelp_block,1);
        setBlockListen(helping_block,2);
        setBlockListen(helped_block,3);

        addData();
        addHandle();
        initHelpListBlock();
        addRecycleDetail();
        addRecycle();
        getAlarm();
        initViewBlock();
    }

    private void initHelpListBlock() {
        if(helplistView == null){
            helplistView = helplist_stub.inflate();
            viewPager = findViewById(R.id.moretab_viewPager);
            scrollIndicatorView = findViewById(R.id.moretab_indicator);
            cancel = findViewById(R.id.cancel);
        }
        helplistView.setVisibility(View.VISIBLE);
        addIndicatorAndViewPager();
    }

    private void initViewBlock() {
        viewSwitcher.setDisplayedChild(2);
    }

    private void addRecycleDetail() {
        baseRecycleView_detail = new BaseRecycleView(context);
        recyclerview_detail = baseRecycleView_detail.addRecycle(recyclerview_detail, CP.Linear,true,true);
        recyclerview_detail.setLoadingMoreEnabled(false);
        recyclerview_detail.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CP.firstLocal = 0;
            }

            @Override
            public void onLoadMore() {
                recyclerView.loadMoreComplete();
            }
        });
        personInfoAdapter  = new PersonInfoAdapter(CP.personInfo,context);
        recyclerview_detail.setAdapter(personInfoAdapter);
    }

    private void addRecycle() {
        baseRecycleView = new BaseRecycleView(context);
        recyclerView = baseRecycleView.addRecycle(recyclerView, CP.Linear,true,true);
        recyclerView.setLoadingMoreEnabled(false);
        dangerListAdapter  = new DangerListAdapter(CP.contentList,context);
        recyclerView.setAdapter(dangerListAdapter);
    }


    private void addHandle() {
        CP.alarmHeplHandlerThread = new HandlerThread("AlarmHelpActivity");
        CP.alarmHeplHandlerThread.start();
        UIHandler = new Handler();
        CP.alarmHeplHandler = new Handler(CP.alarmHeplHandlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CP.setAlarmInfo:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                personInfoAdapter.notifyDataSetChanged();
                                dangerListAdapter.notifyDataSetChanged();
                                recyclerview_detail.refreshComplete();
                            }
                        });
                        break;
                    case Signal.Change_Alarm_content:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                getAlarm();
                            }
                        });
                        break;
                    case Signal.GetAlarmContent:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dangerListAdapter.notifyDataSetChanged();
                                recyclerView.refreshComplete();
                            }
                        });
                        break;
                    case Signal.Refresh_Alarm_Info:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                personInfoAdapter.notifyDataSetChanged();
                                recyclerview_detail.refreshComplete();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void addData() {
        personInfo = new PersonInfo();
        personInfo.setAddress("厦门理工学院");
        personInfo.setName(CP.user.getName());
        personInfo.setCity("北京");
        personInfo.setSex(CP.user.getSex());
        personInfo.setPhone(CP.user.getPhone());
        personInfo.setLatLon("111，120");
        CP.personInfo = personInfo;

    }
    private void getAlarm(){
        GetAlarmContent alarmContent = new GetAlarmContent(CP.alarmHeplHandler,CP.user.getPhone());
        alarmContent.execute("");
        viewSwitcher.setDisplayedChild(1);
    }

    private void setBlockListen(RelativeLayout block,final int index) {
        block.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.SeekHelpListPageNUM = index;
                viewSwitcher.setDisplayedChild(1);
                indicatorViewPager.setCurrentItem(index,true);
            }
        });
    }

    public void addIndicatorAndViewPager(){
        viewPager.setCanScroll(true);
        in = new IndicatorAndViewPager(indicatorViewPager,scrollIndicatorView, CP.SEEKHELP);
        indicatorViewPager = in.addIndicatorAndViewPager((Activity) context,viewPager);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.setDisplayedChild(2);
            }
        });
    }

}
