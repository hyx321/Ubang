package com.ubang.huang.ubangapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.HelpOthersListAdapter;
import com.ubang.huang.ubangapp.adapter.NetPicAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.GetHelp;
import com.ubang.huang.ubangapp.async.GetHelpPic;
import com.ubang.huang.ubangapp.bean.HelpInfomation;
import com.ubang.huang.ubangapp.bean.PictureURL;
import com.ubang.huang.ubangapp.bean.RoughlyHelpInfo;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.service.LocationApplication;
import com.ubang.huang.ubangapp.service.LocationService;
import com.ubang.huang.ubangapp.socket.RemindOther;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.GetParam;
import com.ubang.huang.ubangapp.util.HotTagView;
import com.ubang.huang.ubangapp.util.IndicatorAndViewPager;
import com.ubang.huang.ubangapp.util.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/5.
 * @author huang
 * 发布求助Activity
 */

public class GetHelpActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout panelLayout;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.contain)
    CardView contain;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.head_pic)
    RoundImageView head_pic;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.help_status)
    TextView help_status;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.get_help)
    Button get_help;
    @BindView(R.id.arrive_end)
    Button arrive_end;
    @BindView(R.id.arrive_start)
    Button arrive_start;
    @BindView(R.id.communicate_helper)
    Button communicate_helper;

    private BaiduMap mBaiduMap;
    Marker mEndMarker;
    Marker mStartMarker;
    Marker mUserMarker;
    private BitmapDescriptor bdStart;
    private BitmapDescriptor bdEnd;
    BitmapDescriptor bdUser;
    private LatLng latLng;
    MarkerOptions ooA;
    MarkerOptions ooB;
    MarkerOptions userMark;
    RoutePlanSearch userToEnd;
    RoutePlanSearch routePlanSearch;
    WalkNaviLaunchParam walkToStart;
    WalkNaviLaunchParam walkToEnd;
    List<LatLng> mLatLngList = new ArrayList<>();
    LatLng start;
    LatLng end;
    private Handler UIHandler;
    private Handler handler;
    HandlerThread handlerThread;
    Dialog loading;
    private List<PictureURL> picList = new ArrayList<>();
    NetPicAdapter picAdapter;

    public static void open(Context context) {
        context.startActivity(new Intent(context, GetHelpActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpothers_gethelp_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandle();
        setInfo();
        initMapStatus();
        initOverlay();
        addPanelListener();
        addListener();
    }

    private void addRecycle() {
        BaseRecycleView baseRecycleView = new BaseRecycleView(this);
        recyclerView = baseRecycleView.addRecycle(recyclerView, CP.Grid,false,true);
        if(CP.seekHelpInfo.getHas_picture() == 1) {
            picAdapter = new NetPicAdapter(picList, this);
            recyclerView.setAdapter(picAdapter);

            GetHelpPic getHelpPic = new GetHelpPic(handler,CP.seekHelpInfo.getId(),picList);
            getHelpPic.execute("");
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setInfo() {
        latLng = CP.CurrentLatLng;
        start = new LatLng(CP.seekHelpInfo.getStart_position_lat(),CP.seekHelpInfo.getStart_position_lng());
        end = new LatLng(CP.seekHelpInfo.getEnd_position_lat(),CP.seekHelpInfo.getEnd_position_lng());

        label.setText(CP.seekHelpInfo.getType());
        content.setText(CP.seekHelpInfo.getContent());
        String time = CP.seekHelpInfo.getCreate_time();
        date.setText(time.substring(0,time.length()-2));
        name.setText(CP.seekHelpInfo.getNickname());
        Glide.with(this).load(CP.seekHelpInfo.getAvatar()).into(head_pic);
        judgeInfoStaus(CP.seekHelpInfo.getIs_urgent());
        setIcon();
        addRecycle();

        walkToStart = new WalkNaviLaunchParam().stPt(latLng).endPt(start);
        walkToEnd = new WalkNaviLaunchParam().stPt(latLng).endPt(end);

        mLatLngList.add(start);
        mLatLngList.add(end);
        mLatLngList.add(latLng);

    }

    private void judgeInfoStaus(int isNormal) {
        switch (isNormal){
            case 0:
                help_status.setBackgroundResource(R.drawable.textview_background_unhelp);
                break;
            case 1:
                help_status.setBackgroundResource(R.drawable.textview_background_helped);
                break;
        }
    }

    private void addListener() {
        if(CP.DisPlay == 1){
            get_help.setVisibility(View.GONE);
        }else{
            communicate_helper.setVisibility(View.GONE);
        }
        back.setOnClickListener(this);
        contain.setOnClickListener(this);
        arrive_end.setOnClickListener(this);
        arrive_start.setOnClickListener(this);
        communicate_helper.setOnClickListener(this);
        get_help.setOnClickListener(this);
    }

    private void setIcon() {
        bdStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
        bdEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
        bdUser = BitmapDescriptorFactory.fromResource(R.drawable.icon_point);
    }

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {

        PlanNode startNode = PlanNode.withLocation(start);
        PlanNode endNode = PlanNode.withLocation(end);

        setMarker();
        zoomToSpan();

        routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(startNode).to(endNode));
    }

    private void setMarker() {
        ooA = new MarkerOptions().position(start).icon(bdStart)
                .zIndex(9);
        mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));

        ooB = new MarkerOptions().position(end).icon(bdEnd)
                .zIndex(9);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));

        userMark = new MarkerOptions().position(latLng).icon(bdUser)
                .zIndex(9);
        mUserMarker = (Marker) (mBaiduMap.addOverlay(userMark));
    }

    public void zoomToSpan() {
        if (mLatLngList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng lat : mLatLngList) {
                builder.include(lat);
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }

    private void addHandle() {
        handlerThread = new HandlerThread("PublishHelpActivity");
        handlerThread.start();
        UIHandler = new Handler();
        handler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.GetHelpSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Dialog dialog = new Dialog(DialogModel.GetHelpSuc,GetHelpActivity.this);
                                dialog.createDialog();
                                dialog.show();

                                RoughlyHelpInfo info = new RoughlyHelpInfo();
                                info.setResourse(CP.seekHelpInfo.getRecourse());
                                info.setHelper(CP.user.getId());
                                info.setHelp_id(CP.seekHelpInfo.getId());
                                info.setType(CP.Service_Notice);
                                RemindOther.getWebSocketClient().send(info.toString());
                            }
                        });
                        break;
                    case Signal.GetHelpFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Dialog dialog = new Dialog(DialogModel.GetHelpFail,GetHelpActivity.this);
                                dialog.createDialog();
                                dialog.show();
                            }
                        });
                        break;
                    case Signal.GetHelpPic:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.loadMoreComplete();
                                picAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 初始化地图状态
     */
    private void initMapStatus() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(listener);

        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
    }

    private void addPanelListener() {
        panelLayout.setAnchorPoint(0.35f);
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.get_help:
                getHelpOption();
                break;
            case R.id.arrive_end:
                initWalk(walkToEnd);
                break;
            case R.id.arrive_start:
                initWalk(walkToStart);
                break;
            case R.id.communicate_helper:
                CP.ChatByWay = CP.ChatByHelp;
                ChatRoomActivity.open(GetHelpActivity.this);
                break;
            case R.id.back:
                GetHelpActivity.this.finish();
                break;
        }
    }

    private void getHelpOption() {
        HelpInfomation help = new HelpInfomation();
        help.setHelp_id(CP.seekHelpInfo.getId());
        help.setHelper_id(CP.user.getId());
        help.setPosition_lat(latLng.latitude);
        help.setPosition_lng(latLng.longitude);

        GetHelp getHelp = new GetHelp(handler,help);
        getHelp.execute("");

        loading = new Dialog(DialogModel.loading,GetHelpActivity.this);
        loading.createDialog();
        loading.show();
    }

    private void initWalk(WalkNaviLaunchParam param){
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(GetHelpActivity.this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            routePlanWithWalkParam(param);
                        }
                    });
                }
                @Override
                public void engineInitFail() {}
            });
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void routePlanWithWalkParam(WalkNaviLaunchParam param){
        WalkNavigateHelper.getInstance().routePlanWithParams(param, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {}

            @Override
            public void onRoutePlanSuccess() {
                Intent intent = new Intent();
                intent.setClass(GetHelpActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error){}

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        bdStart.recycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            //创建WalkingRouteOverlay实例
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
            try {
                if (walkingRouteResult.getRouteLines().size() > 0) {
                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }
    };


    @Override
    public void onBackPressed() {
        if (panelLayout != null &&
                (panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
