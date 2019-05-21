package com.ubang.huang.ubangapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.JobListActivity;
import com.ubang.huang.ubangapp.activity.NewsListActivity;
import com.ubang.huang.ubangapp.activity.RobotActivity;
import com.ubang.huang.ubangapp.base.BaseMainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 咨询
 */

public class NewsFragment extends BaseMainFragment {

    @BindView(R.id.service_contain)
    CardView service_contain;
    @BindView(R.id.security_contain)
    CardView security_contain;
    @BindView(R.id.job_contain)
    CardView job_contain;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.moudle_news,container,false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    @Override
    public boolean getUserVisibleHint() {
        String a = "";
        return super.getUserVisibleHint();
    }


    public void init(){
        addListener();
    }

    private void addListener() {
        job_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobListActivity.open(getContext());
            }
        });
        service_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotActivity.open(getContext());
            }
        });


        security_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsListActivity.open(getContext());
            }
        });
    }

}
