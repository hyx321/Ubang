package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.viewpager.SViewPager;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.util.IndicatorAndViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyHelpLIst extends Activity {
    @BindView(R.id.moretab_viewPager)
    SViewPager moretab_viewPager;
    @BindView(R.id.moretab_indicator)
    ScrollIndicatorView moretab_indicator;
    @BindView(R.id.back)
    ImageView back;

    IndicatorViewPager indicatorViewPager;
    IndicatorAndViewPager in;

    public static void open(Context context){
        context.startActivity(new Intent(context,MyHelpLIst.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpothers_my_helplist);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        GlobalHandler.Activities.put("MyHelpLIst",MyHelpLIst.this);
        addIndicatorAndViewPager();
        addListener();
    }

    private void addListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.DisPlay = 0;
                MyHelpLIst.this.finish();
            }
        });
    }

    public void addIndicatorAndViewPager(){
        moretab_viewPager.setCanScroll(true);
        in = new IndicatorAndViewPager(indicatorViewPager,moretab_indicator, CP.MyHelpList);
        indicatorViewPager = in.addIndicatorAndViewPager(this,moretab_viewPager);
    }
}
