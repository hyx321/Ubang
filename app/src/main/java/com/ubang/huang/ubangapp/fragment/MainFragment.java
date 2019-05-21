package com.ubang.huang.ubangapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLngBounds;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.ChatRoomActivity;
import com.ubang.huang.ubangapp.activity.HelpingDetailInfoActivity;
import com.ubang.huang.ubangapp.activity.InputHelpContentActivity;
import com.ubang.huang.ubangapp.activity.LoginActivity;
import com.ubang.huang.ubangapp.adapter.GetCircleButtonAdapter;
import com.ubang.huang.ubangapp.adapter.GetPicUrlAdapter;
import com.ubang.huang.ubangapp.async.AsyncService;
import com.ubang.huang.ubangapp.async.GetHelpPic;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.service.RemindService;
import com.ubang.huang.ubangapp.socket.RemindOther;

import org.java_websocket.enums.ReadyState;

import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 */

public class MainFragment extends SupportFragment {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @BindView(R.id.drawerlayout)
    FlowingDrawer flowingDrawer;

    private SupportFragment[] mFragments = new SupportFragment[3];
    private int now = R.id.tab_round;
    SupportFragment firstFragment;
    HashMap<Integer,Integer> map = new HashMap<>();
    GetCircleButtonAdapter getPicUrlAdapter;
    Handler handler;
    HandlerThread handlerThread;
    Handler UIHandler;
    NotificationManager myManager;
    Notification.Builder myBuilder;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contain_fragment_main, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void bindRemindService() {
        RemindService.open(getContext());
    }

    private void init() {
        addNoti();
        addHandle();
        dealFramgment();
        initView();
        addMenu();
        addDrawer();
        addFloatButton();
        bindRemindService();
        addNoti();
    }

    @TargetApi(21)
    private void addNoti() {
        myManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        Bitmap LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.u);
        myBuilder = new Notification.Builder(getContext());
        myBuilder.setContentTitle("消息")
                .setSmallIcon(R.mipmap.u)
                .setLargeIcon(LargeBitmap)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PRIVATE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void addHandle() {
        handlerThread = new HandlerThread("PublishHelpActivity");
        handlerThread.start();
        UIHandler = new Handler();
        GlobalHandler.NotiHanler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.GetNotification:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                PendingIntent pi = PendingIntent.getActivity(
                                        getContext(),
                                        100,
                                        new Intent(getActivity(), HelpingDetailInfoActivity.class),
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                );

                                myBuilder.setContentText(CP.Notice)
                                        .setContentIntent(pi);
                                Notification myNotification = myBuilder.build();

                                myManager.notify(1, myNotification);
                            }
                        });
                        break;
                    case Signal.GetAnwser:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String name = "";
                                if(CP.chatMessage.getResourse() == CP.user.getId()){
                                    name = CP.chatMessage.getResourse_name();
                                }else{
                                    name = CP.chatMessage.getHelper_name();
                                }
                                String mes = name+" : "+CP.chatMessage.getContent();

                                PendingIntent pi = PendingIntent.getActivity(
                                        getContext(),
                                        100,
                                        new Intent(getActivity(), ChatRoomActivity.class),
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                );

                                myBuilder.setContentText(mes)
                                        .setContentIntent(pi);
                                Notification myNotification = myBuilder.build();

                                myManager.notify(1, myNotification);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initView() {
        dealTab();
        bottomBar.selectTabAtPosition(0);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                if(tabId == R.id.tab_campus){
                    CP.dialogPlus.show();
                }

            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                showHideFragment(mFragments[map.get(tabId)],mFragments[map.get(now)]);
                now = tabId;
                if(tabId != R.id.tab_campus){
                    CP.alarmLocationService.unregisterListener(CP.bdaListener); //注销掉监听
                    CP.alarmLocationService.stop(); //停止定位服务s
                }
            }
        });
    }

    private void addFloatButton() {
        getPicUrlAdapter = new GetCircleButtonAdapter(getActivity(),getContext());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CP.dialogPlus = DialogPlus.newDialog(getContext())
                        .setAdapter(getPicUrlAdapter)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                            }
                        })
                        .setGravity(Gravity.BOTTOM)
                        .setContentBackgroundResource(R.color.alpha)
                        .setOverlayBackgroundResource(R.color.alpha)
                        .create();
            }
        });
    }

    private void addDrawer() {
        flowingDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        flowingDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

    }

    private void addMenu() {
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    public void dealFramgment(){
        firstFragment = findChildFragment(SeekHelpFragment.class);
        if (firstFragment == null) {
            mFragments[CP.SEEKHELP] = SeekHelpFragment.newInstance();
            mFragments[CP.HELPOTHERS] = HelpOthersFragment.newInstance();
            mFragments[CP.NEWS] = NewsFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, CP.SEEKHELP,
                    mFragments[CP.SEEKHELP],
                    mFragments[CP.HELPOTHERS],
                    mFragments[CP.NEWS]);
        } else {
            mFragments[CP.SEEKHELP] = firstFragment;
            mFragments[CP.HELPOTHERS] = findChildFragment(HelpOthersFragment.class);
            mFragments[CP.NEWS] = findChildFragment(NewsFragment.class);
        }
        showHideFragment(firstFragment);
    }

    public void dealTab(){
        map.put(R.id.tab_campus,CP.SEEKHELP);
        map.put(R.id.tab_round,CP.HELPOTHERS);
        map.put(R.id.tab_learn,CP.NEWS);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (flowingDrawer.isMenuVisible()) {
            flowingDrawer.closeMenu();
        } else {
            super.onBackPressedSupport();
        }
        return super.onBackPressedSupport();
    }

}
