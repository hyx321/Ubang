package com.ubang.huang.ubangapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.base.BaseMainFragment;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.service.LocationApplication;

import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 求助
 */

public class SeekHelpFragment extends BaseMainFragment {

    public static SeekHelpFragment newInstance() {
        Bundle args = new Bundle();
        SeekHelpFragment fragment = new SeekHelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.seekhelp_help_framelayout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void onStart() {
        super.onStart();
        CP.alarmLocationService = ((LocationApplication) getActivity().getApplication()).locationService;
        CP.alarmLocationService.registerListener(CP.bdaListener);
        CP.alarmLocationService.setLocationOption(CP.alarmLocationService.getDefaultLocationClientOption());
        CP.alarmLocationService.start();
        CP.alarmLocationService.stop();
    }

}
