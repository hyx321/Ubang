package com.ubang.huang.ubangapp.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.ubang.huang.ubangapp.adapter.HelpSortAdapter;
import com.ubang.huang.ubangapp.adapter.MyHelpAdapter;
import com.ubang.huang.ubangapp.adapter.SeekHelpAdapter;
import com.ubang.huang.ubangapp.common.CP;

/**
 * Created by huang on 2019/2/5.
 *
 * @author = huangyouxin
 */

public class IndicatorAndViewPager {

    private IndicatorViewPager indicator;
    private ScrollIndicatorView scrollIndicator;
    private int type;

    public IndicatorAndViewPager(IndicatorViewPager in, ScrollIndicatorView sc, int type){
        this.indicator = in;
        this.scrollIndicator = sc;
        this.type = type;
    }

    public IndicatorViewPager addIndicatorAndViewPager(Activity activity, ViewPager viewPager){

        viewPager.setOffscreenPageLimit(2);
        scrollIndicator.setOnTransitionListener(new OnTransitionTextListener()
                .setColor(0xFF2196F3, Color.GRAY).setSize(CP.SELECTSIZE, CP.UNSELECTSIZE));
        scrollIndicator.setScrollBar(new ColorBar(activity, 0xFF2196F3, 4));

        indicator = new IndicatorViewPager(scrollIndicator, viewPager);
        switch (type){
            case CP.SEEKHELP:
                indicator.setAdapter(new SeekHelpAdapter(activity));
                indicator.setCurrentItem(CP.SeekHelpListPageNUM,true);
                return indicator;
            case CP.HelpSort:
                indicator.setAdapter(new HelpSortAdapter(activity));
                return indicator;
            case CP.MyHelpList:
                indicator.setAdapter(new MyHelpAdapter(activity));
                return indicator;
            default:
                return indicator;
        }
    }

}
