package com.ubang.huang.ubangapp.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.GetParam;

/**
 *
 * Created by huang on 2019/3/2.
 * @author = huangyouxin
 * 萌新模块
 * 获取校园图片
 */

public  class AsyncGetPositionInfo extends AsyncTask<String, Void, String> {
    private String position;
    private Handler handler;
    private Message message;
    private int size;
    private int time;
    private SuggestionSearch suggestionSearch;
    private GeoCoder geoCoder;

    public AsyncGetPositionInfo(String position, Handler handler,SuggestionSearch suggestionSearch,GeoCoder geoCoder){
        this.position = position;
        this.handler = handler;
        this.geoCoder = geoCoder;
        this.suggestionSearch = suggestionSearch;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            geoCoder.setOnGetGeoCodeResultListener(geoListener);
            suggestionSearch.setOnGetSuggestionResultListener(sugListener);
            suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .city(CP.currentCity)
                    .keyword(position));
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private OnGetSuggestionResultListener sugListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            CP.suggestList.clear();
            if(suggestionResult.getAllSuggestions() != null) {
                size = suggestionResult.getAllSuggestions().size();
                if (size > 10) {
                    CP.suggestList.addAll(suggestionResult.getAllSuggestions().subList(0, 10));
                } else {
                    CP.suggestList.addAll(suggestionResult.getAllSuggestions().subList(0, size));
                }
                CP.suggestInfoList.clear();
                for(int i=0;i<size;i++){
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(CP.suggestList.get(i).getPt()));
                }
                time = 0;
            }
        }
    };

    private OnGetGeoCoderResultListener geoListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            } else {
                CP.suggestInfoList.add(reverseGeoCodeResult.getAddress());
                time++;
                if(time == size-1){
                    GetParam.SendMessage(handler,CP.Get_Position);
                }
            }
        }
    };

}

