package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.InputPlaceActivity;
import com.ubang.huang.ubangapp.bean.HelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.GetParam;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 模糊地点搜索显示的结果
 */
public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {


    private List<SuggestionResult.SuggestionInfo> suggInfoList = new ArrayList<>();
    private SuggestionResult.SuggestionInfo suggInfo;
    private GeoCoder geoCoder;
    private List<String> infoList;

    public PlaceListAdapter(List<SuggestionResult.SuggestionInfo> suggInfoList,GeoCoder geoCoder,List<String> infoList) {
        this.suggInfoList = suggInfoList;
        this.geoCoder = geoCoder;
        this.infoList = infoList;
    }

    /**
     * 创建新View，被LayoutManager所调用
     * @param viewGroup viewGroup
     * @param viewType viewType
     * @return viewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seekhelp_publish_content,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        suggInfo = new SuggestionResult.SuggestionInfo();
        suggInfo = suggInfoList.get(position);
        viewHolder.place.setText(suggInfo.getKey());
        try {
                viewHolder.address.setText(infoList.get(position));
                viewHolder.contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        suggInfo = suggInfoList.get(position);
                        if(!CP.getEndPosition) {
                            CP.currentPosition = suggInfo.getKey();
                            CP.currentDetailPosition = infoList.get(position);
                            CP.currentLatLng = suggInfo.getPt();

                            GetParam.SendMessage(CP.dataHandler, CP.GetLocalPosition);
                            GetParam.SendMessage(CP.LearnHandler, CP.GetLocalPosition);
                            GetParam.SendMessage(CP.AccompanyHandler, CP.GetLocalPosition);
                            GetParam.SendMessage(CP.LostHandler,CP.GetLocalPosition);
                            GetParam.SendMessage(CP.ReplaceHandler,CP.GetLocalPosition);
                        }else{
                            CP.endCurrentPosition = suggInfo.getKey();
                            CP.endCurrentDetailPosition = infoList.get(position);
                            CP.endCurrentLatLng = suggInfo.getPt();
                            GetParam.SendMessage(CP.dataHandler, CP.GetEndpostion);
                            GetParam.SendMessage(CP.AccompanyHandler, CP.GetEndpostion);
                            GetParam.SendMessage(CP.ReplaceHandler,CP.GetEndpostion);
                            CP.getEndPosition = false;
                        }

                        CP.inputPlaceActivity.finish();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     *  获取数据的数量
     */

    @Override
    public int getItemCount() {
        return suggInfoList.size();
    }


    /**
     *自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place)
        TextView place;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.contain)
        CardView contain;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















