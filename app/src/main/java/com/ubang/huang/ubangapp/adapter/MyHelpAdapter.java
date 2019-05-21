package com.ubang.huang.ubangapp.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.SetTabText;

/**
 * Created by huang on 2019/1/28.
 *
 * @author = huangyouxin
 * 指示器
 */

public class MyHelpAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter{

    private Activity activity;
    public MyHelpAdapter(Activity activity) {
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return CP.Help_Status.length;
    }

    @Override
    @TargetApi(11)
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.comment_tab_top, container, false);
        }
        return SetTabText.setTextVIew((TextView) convertView,position,CP.MyHelpList,activity);
    }

    @Override
    public int getItemPosition(Object object) {
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_UNCHANGED;
    }


    @Override
    public View getViewForPage(int position, View convertView, ViewGroup container) {
        if(convertView == null){
            switch (position){
                case 0:
                    convertView = activity.getLayoutInflater().inflate(R.layout.helpothers_helping_framelayout, container, false);
                    break;
                case 1:
                    convertView = activity.getLayoutInflater().inflate(R.layout.helpothers_helped_framelayout, container, false);
                    break;
            }
        }
        return convertView;
    }


}
