package com.ubang.huang.ubangapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.viewpager.SViewPager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.suke.widget.SwitchButton;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.service.LocationApplication;
import com.ubang.huang.ubangapp.service.LocationService;
import com.ubang.huang.ubangapp.util.GetParam;
import com.ubang.huang.ubangapp.util.HotTagView;
import com.ubang.huang.ubangapp.util.IndicatorAndViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/5.
 * @author huang
 * 发布求助Activity
 */

public class PublishHelpActivity extends Activity{

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout panelLayout;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help_type)
    HotTagView help_type;
    @BindView(R.id.emergency_switch)
    SwitchButton emergency_switch;

    private LocationService locationService;
    private BaiduMap mBaiduMap;
    private Marker mEndMarker;
    private Marker mStartMarker;
    private BitmapDescriptor bdStart;
    private BitmapDescriptor bdEnd;
    private LatLng latLng;
    MarkerOptions ooA;
    MarkerOptions ooB;
    private int firstLocal = 0;
    private GeoCoder geoCoder;
    private Handler UIHandler;
    private MapStatus.Builder builder = new MapStatus.Builder();
    ArrayList<HotTagView.TagView> tagViews;

    public static void open(Context context) {
        context.startActivity(new Intent(context, PublishHelpActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_publish_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        GlobalHandler.Activities.put("PublishHelpActivity",PublishHelpActivity.this);
        addHandle();
        addPanelListener();
        setIcon();
        addBackListener();
        setTagColor(help_type,CP.help_sort);
        addSwitch();

    }

    private void addSwitch() {
        emergency_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    CP.isUrgent = 1;
                    GetParam.SendMessage(CP.dataHandler,Signal.ChangeHelpEmegency);
                }else{
                    CP.isUrgent = 2;
                    GetParam.SendMessage(CP.dataHandler,Signal.ChangeHelpNormal);
                }
            }
        });
    }

    private void addBackListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishHelpActivity.this.finish();
            }
        });
    }

    private void addHandle() {
        CP.handlerThread = new HandlerThread("PublishHelpActivity");
        CP.handlerThread.start();
        UIHandler = new Handler();
        CP.dataHandler = new Handler(CP.handlerThread.getLooper()){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CP.GetLocalPosition:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mStartMarker.setPosition(CP.currentLatLng);
                                builder.target(CP.currentLatLng).zoom(19);
                                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                            }
                        });
                        break;
                    case CP.GetEndpostion:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mEndMarker.setPosition(CP.endCurrentLatLng);

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(CP.currentLatLng);
                                builder.include(CP.endCurrentLatLng);
                                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(builder.build());
                                mBaiduMap.setMapStatus(u);
                            }
                        });
                        break;
                    case Signal.ChangeHelpEmegency:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                help_type.setTags(CP.emegency_help_sort);
                                addColor(help_type);
                            }
                        });
                        break;

                    case Signal.ChangeHelpNormal:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                help_type.setTags(CP.help_sort);
                                addColor(help_type);
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                bdStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
                bdEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
            }
        });
    }

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {
        ooA = new MarkerOptions().position(latLng).icon(bdStart)
                .zIndex(9).draggable(true);
        mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        mStartMarker.setDraggable(true);

        ooB = new MarkerOptions().position(CP.endCurrentLatLng).icon(bdEnd)
                .zIndex(9);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(marker.getPosition()));
                    }
                });

            }
            public void onMarkerDragEnd(Marker marker) {

            }
            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 初始化地图状态
     */
    private void initMapStatus() {
        geoCoder = GeoCoder.newInstance();
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();

        geoCoder.setOnGetGeoCodeResultListener(geoListener);
        GetParam.SendMessage(CP.ReplaceHandler,CP.GetLocalPosition);
        builder.target(latLng).zoom(19);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    private void addPanelListener() {
        panelLayout.setAnchorPoint(0.35f);
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }


    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((LocationApplication) getApplication()).locationService;
        locationService.registerListener(bdaListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        bdStart.recycle();
        geoCoder.destroy();
        locationService.unregisterListener(bdaListener); //注销掉监听
        locationService.stop(); //停止定位服务
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
    private BDAbstractLocationListener bdaListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError && firstLocal == 0) {
                latLng = new LatLng(location.getLatitude(),location.getLongitude());
                CP.currentPosition = location.getAddrStr();
                CP.currentDetailPosition = location.getLocationDescribe();
                CP.currentCity = location.getCity();
                CP.currentLatLng = latLng;
                firstLocal++;
                initMapStatus();
                initOverlay();
            }
        }
    };

    OnGetGeoCoderResultListener geoListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            } else {
                CP.currentPosition = reverseGeoCodeResult.getAddress();
                CP.currentDetailPosition  = reverseGeoCodeResult.getSematicDescription();

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        GetParam.SendMessage(CP.ReplaceHandler,CP.GetLocalPosition);
                    }
                });

            }
        }
    };


    private void setTagColor(final HotTagView tagCloudView, ArrayList<String> arrayList) {
        tagCloudView.setTags(arrayList);
        addColor(tagCloudView);

        tagCloudView.setOnTagClickListener(new HotTagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(tagViews.get(position).getCurrentTextColor() == Color.GRAY){
                            tagViews.get(position).setTextColor(Color.BLACK);
                            clearOthersTag(position);
                            CP.currentPage = position;
                            CP.currentPageType = tagViews.get(position).getText().toString();

                            GetParam.SendMessage(CP.ReplaceHandler, Signal.CuurentPageName);
                        }
                    }
                });
            }
        });
    }

    private void addColor(final HotTagView tagCloudView){
        tagViews = (ArrayList<HotTagView.TagView>) tagCloudView.getTagViews();
        for (int i = 0; i < tagViews.size(); i++) {
            if(i == 1){
                tagViews.get(i).setTextColor(Color.BLACK);
            }else{
                tagViews.get(i).setTextColor(Color.GRAY);
            }
            tagViews.get(i).setTextSize(20);
            tagViews.get(i).setmBackgroudPaintColor(Color.WHITE);
            tagViews.get(i).setmStrokeWidth(0);
            tagViews.get(i).setmLinePaintColor(Color.GRAY);
        }
    }

    public void clearOthersTag(int position){
        for (int i = 0; i < tagViews.size(); i++) {
            if (i != position){
                tagViews.get(i).setTextColor(Color.GRAY);
                tagViews.get(i).setmBackgroudPaintColor(Color.WHITE);
                tagViews.get(i).setmLinePaintColor(Color.GRAY);
            }
        }
    }

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
