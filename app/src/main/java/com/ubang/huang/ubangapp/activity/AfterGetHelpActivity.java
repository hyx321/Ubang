package com.ubang.huang.ubangapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.service.LocationApplication;
import com.ubang.huang.ubangapp.service.LocationService;
import com.ubang.huang.ubangapp.util.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/14.
 * @author huang
 */

public class AfterGetHelpActivity extends Activity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.arrive_start)
    Button arrive_start;
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

    private LatLng latLng;
    private int firstLocal = 0;
    private BitmapDescriptor bdStart;
    private LocationService locationService;
    BaiduMap mBaiduMap;
    private MapStatus.Builder builder = new MapStatus.Builder();
    WalkNavigateHelper  walkNavigateHelper ;
    WalkNaviLaunchParam walkParam;

    public static void open(Context context) {
        context.startActivity(new Intent(context, AfterGetHelpActivity.class));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpothers_after_get_help);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        initMapStatus();
        initOverlay();
        addBackListener();
        setInfo();

    }

    private void setInfo() {
            content.setText(CP.helpInfo.getHelp_content());
            date.setText(CP.helpInfo.getHelp_date());
            name.setText(CP.helpInfo.getHelp_name());
    }

    private void addBackListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AfterGetHelpActivity.this.finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {

    }

    /**
     * 初始化地图状态
     */
    private void initMapStatus() {
        LatLng latLng = CP.CurrentLatLng;
        LatLng latLng1 = new LatLng(latLng.latitude-0.01,latLng.longitude-0.01);
        walkParam = new WalkNaviLaunchParam().stPt(latLng).endPt(latLng1);

        arrive_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WalkNavigateHelper.getInstance().initNaviEngine(AfterGetHelpActivity.this, new IWEngineInitListener() {
                        @Override
                        public void engineInitSuccess() {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    routePlanWithWalkParam();
                                }
                            });

                        }

                        @Override
                        public void engineInitFail() {
                            String a = "";
                        }
                    });
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void routePlanWithWalkParam(){
        WalkNavigateHelper.getInstance().routePlanWithParams(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                String a = "";
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View", "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(AfterGetHelpActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                String a = "";
            }

        });
    }

}
