package com.ubang.huang.ubangapp.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.common.CP;

/**
 * Created by huang on 2019/2/5.
 *
 * @author = huangyouxin
 */

public class BaseRecycleView {
    private Context context;

    public BaseRecycleView(Context context){
        this.context = context;
    }

    public XRecyclerView addRecycle(XRecyclerView xRecyclerView, String layoutType, Boolean isPull,Boolean isDisplayFootView){
        LinearLayoutManager layoutManager;

        switch (layoutType){
            case CP.Grid:
                layoutManager = new GridLayoutManager(context,3);
                xRecyclerView.setLayoutManager(layoutManager);
                break;
            case CP.Gallery:
                layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                layoutManager.setReverseLayout(false);
                xRecyclerView.setLayoutManager(layoutManager);
                break;
            case CP.Linear:
                layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                xRecyclerView.setLayoutManager(layoutManager);
                break;
            default:
                break;
        }

        if(isDisplayFootView){
            xRecyclerView.getDefaultFootView().setLoadingHint("加载中");
            xRecyclerView.getDefaultFootView().setNoMoreHint("加载完毕");
        }
        xRecyclerView.setPullRefreshEnabled(isPull);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLimitNumberToCallLoadMore(2);
        return xRecyclerView;
    }
}
