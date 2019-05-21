package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.async.GetOthersSeekHelp;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.PersonSeekHelp;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.util.GetParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Float.parseFloat;

/**
 * Created by huang on 2019/4/14.
 * @author huang
 */

public class RoundHelps extends Activity{

    @BindView(R.id.mapview)
    MapView mapView;

    BaiduMap mBaiduMap;
    private MapStatus.Builder builder = new MapStatus.Builder();
    private Handler UIHandler;
    private HandlerThread handlerThread;
    private Handler DataHandler;
    List<SeekHelpInfo> seekHelpInfos = new ArrayList<>();

    public static void open(Context context) {
        context.startActivity(new Intent(context, RoundHelps.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpothers_round_help_map);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        addHandler();
        dealDate();
        initMapStatus();


    }

    private void initMapStatus() {
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();

        builder.target(CP.CurrentLatLng).zoom(19);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    public void initOverlay() {
        List<LatLng> list = new ArrayList<>();
        LatLng latLng1 = new LatLng(CP.CurrentLatLng.latitude-0.002,CP.CurrentLatLng.longitude-0.001);
        LatLng latLng2 = new LatLng(CP.CurrentLatLng.latitude-0.003,CP.CurrentLatLng.longitude-0.001);
        LatLng latLng3 = new LatLng(CP.CurrentLatLng.latitude-0.001,CP.CurrentLatLng.longitude-0.001);
        list.add(latLng1);
        list.add(latLng2);
        list.add(latLng3);

        Map<LatLng,SeekHelpInfo> listInfo = new HashMap<>();

        LatLng helpLat ;
        for(int i=0;i<seekHelpInfos.size();i++){
            //LatLng latLng = GetParam.StringToLatlng(CP.HelpInfoList.get(i).getHelp_startPosition());
            BitmapDescriptor bdStart;
            switch (seekHelpInfos.get(i).getType()){
                case "失物":
                    bdStart = BitmapDescriptorFactory.fromResource(R.drawable.lost);
                    break;
                case "陪同":
                    bdStart = BitmapDescriptorFactory.fromResource(R.drawable.friend);
                    break;
                case "代取":
                    bdStart = BitmapDescriptorFactory.fromResource(R.drawable.replace);
                    break;
                default:
                    bdStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_point);
                    break;
            }



            helpLat = new LatLng(seekHelpInfos.get(i).getStart_position_lat(),seekHelpInfos.get(i).getStart_position_lng());
            MarkerOptions markerOptions = new MarkerOptions().position(helpLat).icon(bdStart)
                    .animateType(MarkerOptions.MarkerAnimateType.jump);
           mBaiduMap.addOverlay(markerOptions);

            listInfo.put(helpLat,seekHelpInfos.get(i));

        }


        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        BitmapDescriptor bdUser = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
        MarkerOptions markerOptions = new MarkerOptions().position(CP.CurrentLatLng).icon(bdUser)
                .zIndex(9).animateType(MarkerOptions.MarkerAnimateType.jump);
       mBaiduMap.addOverlay(markerOptions);

       mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener(){
           @Override
           public boolean onMarkerClick(Marker marker) {
               if( marker.getPosition() != CP.CurrentLatLng){
                   CP.seekHelpInfo = listInfo.get(marker.getPosition());
                   if(CP.seekHelpInfo.getType().equals("学习")){
                       GetHelpLearn.open(RoundHelps.this);
                   }else{
                       GetHelpActivity.open(RoundHelps.this);
                   }
                   TextView textView = new TextView(getApplicationContext());
                   textView.setText(CP.helpInfo.getHelp_content());
                   textView.setBackgroundColor(Color.WHITE);
                   textView.setTextSize(15);
               }


               return false;
           }
       });

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

                            }
                        });
                        break;
                    case Signal.GetOtherSeekHelp:
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                initOverlay();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void dealDate() {

        PersonSeekHelp personSeekHelp = new PersonSeekHelp();
        personSeekHelp.setUserID(CP.user.getId());
        personSeekHelp.setStatus("未开始");

        GetOthersSeekHelp getPersonSeekHelp = new GetOthersSeekHelp(DataHandler,personSeekHelp,seekHelpInfos);
        getPersonSeekHelp.execute("");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
}
