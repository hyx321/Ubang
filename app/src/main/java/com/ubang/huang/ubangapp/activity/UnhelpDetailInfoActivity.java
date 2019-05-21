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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.NetPicAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.DeleteHelpInfo;
import com.ubang.huang.ubangapp.async.GetHelpDetailInfo;
import com.ubang.huang.ubangapp.async.GetHelpPic;
import com.ubang.huang.ubangapp.async.GetUnHelpDetailInfo;
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


/**
 * Created by huang on 2019/5/2.
 * @author huang
 */

public class UnhelpDetailInfoActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout panelLayout;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.cancel_help)
    TextView cancel;
    @BindView(R.id.modify_help)
    TextView modify;

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
    Dialog loading;
    List<LatLng> mLatLngList = new ArrayList<>();
    private List<PictureURL> picList = new ArrayList<>();
    NetPicAdapter picAdapter;

    public static void open(Context context) {
        context.startActivity(new Intent(context, UnhelpDetailInfoActivity.class));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_unhelp_detail_layout);
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
       GetUnHelpDetailInfo getHelpPic = new GetUnHelpDetailInfo( CP.HelpInfoHandler,CP.help_id);
       getHelpPic.execute("");
    }

    private void setInfo() {
        label.setText(BeanParam.HelpInfo.getType());
        content.setText(BeanParam.HelpInfo.getContent());
        String time = BeanParam.HelpInfo.getPublish_time();
        date.setText(time.substring(0,time.length()-2));
        addRecycle();
    }

    private void addBackListener() {
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        modify.setOnClickListener(this);
    }

    private void addRecycle() {
        BaseRecycleView baseRecycleView = new BaseRecycleView(this);
        recyclerView = baseRecycleView.addRecycle(recyclerView, CP.Grid,false,true);
        if(BeanParam.HelpInfo.getHas_picture() == 1) {
            picAdapter = new NetPicAdapter(picList, this);
            recyclerView.setAdapter(picAdapter);

            GetHelpPic getHelpPic = new GetHelpPic( CP.HelpInfoHandler,BeanParam.HelpInfo.getId(),picList);
            getHelpPic.execute("");
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void addHandle() {
        handlerThread = new HandlerThread("PublishHelpActivity");
        handlerThread.start();
        UIHandler = new Handler();
        CP.HelpInfoHandler = new Handler(handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case Signal.GetUnHelpDetailInfo:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                initOverlay();
                                addPanelListener();
                                setInfo();
                            }
                        });
                        break;
                    case Signal.DeleteHelpFail:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Dialog dialog = new Dialog(DialogModel.DeleHelpFail,UnhelpDetailInfoActivity.this);
                                dialog.createDialog();
                                dialog.show();
                            }
                        });
                        break;
                    case Signal.DeleteHelpSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                Dialog dialog = new Dialog(DialogModel.DeleHelpSuc,UnhelpDetailInfoActivity.this);
                                dialog.createDialog();
                                dialog.show();
                                UnhelpDetailInfoActivity.this.finish();
                            }
                        });
                        break;
                    case Signal.ModifyHelpSuc:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                content.setText(BeanParam.HelpInfo.getContent());
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
    }

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {

        LatLng start = new LatLng(BeanParam.HelpInfo.getStart_position_lat(),BeanParam.HelpInfo.getStart_position_lng());
        LatLng end = new LatLng(BeanParam.HelpInfo.getEnd_position_lat(),BeanParam.HelpInfo.getEnd_position_lng());

        mLatLngList.add(start);
        mLatLngList.add(end);

        addMarker(start,end);

        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);

        routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));

        zoomToSpan();
    }

    private void addMarker(LatLng start, LatLng end) {
        startOp = new MarkerOptions().position(start).icon(bdStart)
                .zIndex(9);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(startOp));

        endOp = new MarkerOptions().position(end).icon(bdEnd)
                .zIndex(9);
        mUserMarker = (Marker) (mBaiduMap.addOverlay(endOp));
    }

    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     */
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
                UnhelpDetailInfoActivity.this.finish();
                break;
            case R.id.cancel_help:
                DeleteHelpInfo deleteHelpInfo = new DeleteHelpInfo(CP.HelpInfoHandler,BeanParam.HelpInfo.getId());
                deleteHelpInfo.execute();
                loading = new Dialog(DialogModel.loading,UnhelpDetailInfoActivity.this);
                loading.createDialog();
                loading.show();
                break;
            case R.id.modify_help:
                ModifyHelpContentActivity.open(UnhelpDetailInfoActivity.this);
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
