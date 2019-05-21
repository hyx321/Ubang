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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.geocode.GeoCoder;
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
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.NetPicAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.GetHelpDetailInfo;
import com.ubang.huang.ubangapp.async.GetHelpPic;
import com.ubang.huang.ubangapp.async.GetHelpingDetailInfo;
import com.ubang.huang.ubangapp.async.PostRating;
import com.ubang.huang.ubangapp.bean.HelpDetailInfo;
import com.ubang.huang.ubangapp.bean.PictureURL;
import com.ubang.huang.ubangapp.common.BeanParam;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.service.LocationService;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.Dialog;
import com.ubang.huang.ubangapp.util.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * Created by huang on 2019/5/2.
 * @author huang
 */

public class HelpingDetailInfoActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout panelLayout;
   @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.head_pic)
    RoundImageView head_pic;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.message_resourse)
    TextView message_resourse;

    private LocationService locationService;
    private BaiduMap mBaiduMap;
    private Marker mEndMarker;
    private Marker mStartMarker;
    private Marker mUserMarker;
    private BitmapDescriptor bdStart;
    private BitmapDescriptor bdEnd;
    private BitmapDescriptor bdUser;
    private LatLng latLng;
    MarkerOptions startOp;
    MarkerOptions endOp;
    MarkerOptions userOp;
    private GeoCoder geoCoder;
    private Handler UIHandler;
    private Handler DataHandler;
    HandlerThread handlerThread;
    private MapStatus.Builder builder = new MapStatus.Builder();
    RoutePlanSearch routePlanSearch;
    HelpDetailInfo helpDetailInfo = new HelpDetailInfo();
    private int distant;
    Dialog dialog;
    List<LatLng> mLatLngList = new ArrayList<>();
    private List<PictureURL> picList = new ArrayList<>();
    NetPicAdapter picAdapter;

    public static void open(Context context) {
        context.startActivity(new Intent(context, HelpingDetailInfoActivity.class));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_helping_detail_layout);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandle();
        getInfo();
        initMapStatus();
        setIcon();
        addBackListener();
    }

   private void getInfo() {
        GetHelpingDetailInfo getHelpDetailInfo = new GetHelpingDetailInfo(DataHandler,CP.help_id);
        getHelpDetailInfo.execute("");
    }

    private void setInfo() {
        label.setText(BeanParam.HelpInfo.getType());
        content.setText(BeanParam.HelpInfo.getContent());
        String time = BeanParam.HelpInfo.getCreate_time();

        date.setText(time.substring(0,time.length()-2));
        name.setText(BeanParam.HelpInfo.getName());
        setAvatar();
        addRecycle();
    }

    private void addRecycle() {
        BaseRecycleView baseRecycleView = new BaseRecycleView(this);
        recyclerView = baseRecycleView.addRecycle(recyclerView, CP.Grid,false,true);
        if(BeanParam.HelpInfo.getHas_picture() == 1) {
            picAdapter = new NetPicAdapter(picList, this);
            recyclerView.setAdapter(picAdapter);

            GetHelpPic getHelpPic = new GetHelpPic(DataHandler,BeanParam.HelpInfo.getId(),picList);
            getHelpPic.execute("");
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setAvatar() {
        if(BeanParam.HelpInfo.getAvatar() != null) {
            Glide.with(this).load(BeanParam.HelpInfo.getAvatar()).into(head_pic);
        }
    }

    private void addBackListener() {
        back.setOnClickListener(this);
        message_resourse.setOnClickListener(this);
    }

    private void addHandle() {
        handlerThread = new HandlerThread("PublishHelpActivity");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.GetHelpingDetailInfo:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                initOverlay();
                                addPanelListener();
                                setInfo();
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

    private void setIcon() {
        bdStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
        bdEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
        bdUser = BitmapDescriptorFactory.fromResource(R.drawable.icon_point);
    }

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {

        LatLng start = new LatLng(BeanParam.HelpInfo.getStart_position_lat(),BeanParam.HelpInfo.getStart_position_lng());
        LatLng end = new LatLng(BeanParam.HelpInfo.getEnd_position_lat(),BeanParam.HelpInfo.getEnd_position_lng());
        LatLng user = new LatLng(BeanParam.HelpInfo.getHelper_position_lat(),BeanParam.HelpInfo.getHelper_position_lng());

        mLatLngList.add(start);
        mLatLngList.add(end);
        mLatLngList.add(user);

        addMarker(start,end,user);

        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);

        routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));

        zoomToSpan(start,end);

    }

    private void addMarker(LatLng start, LatLng end, LatLng user) {
        startOp = new MarkerOptions().position(start).icon(bdStart)
                .zIndex(9);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(startOp));

        endOp = new MarkerOptions().position(end).icon(bdEnd)
                .zIndex(9);
        mUserMarker = (Marker) (mBaiduMap.addOverlay(endOp));

        userOp = new MarkerOptions().position(user).icon(bdUser)
                .zIndex(9);
        mUserMarker = (Marker) (mBaiduMap.addOverlay(userOp));
    }

    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     */
    public void zoomToSpan(LatLng latLng,LatLng latLng1) {
        List<LatLng> mLatLngList = new ArrayList<>();
        mLatLngList.add(latLng);
        mLatLngList.add(latLng1);
        if (mLatLngList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng lat : mLatLngList) {
                builder.include(lat);
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }


    /**
     * 初始化地图状态
     */
    private void initMapStatus() {
        geoCoder = GeoCoder.newInstance();
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

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.back:
                HelpingDetailInfoActivity.this.finish();
                break;
            case R.id.message_resourse:
                CP.ChatByWay = CP.ChatBySeekHelp;
                ChatRoomActivity.open(this);
                break;
        }
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
