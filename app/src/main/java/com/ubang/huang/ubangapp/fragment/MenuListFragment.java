
package com.ubang.huang.ubangapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.LoginActivity;
import com.ubang.huang.ubangapp.activity.MainActivity;
import com.ubang.huang.ubangapp.activity.UbangInfoActivity;
import com.ubang.huang.ubangapp.activity.FeedBackActivity;
import com.ubang.huang.ubangapp.activity.PersonInfoActivity;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.common.BeanParam;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;


/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment {

    ImageView head_pic;
    NavigationView vNavigation;
    TextView name;


    View view;
    Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container,false);
        init();
        return  view ;
    }

    private void init() {
        bindView();
        addNavigation();
        setupHeader();
    }

    private void bindView() {
        context = getContext();
        vNavigation = view.findViewById(R.id.vNavigation);
        View headView = vNavigation.getHeaderView(0);
        head_pic = headView.findViewById(R.id.menu_head_pic);
        name = headView.findViewById(R.id.menu_name);
    }

    private void addNavigation() {
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.feedback:
                        FeedBackActivity.open(getContext());
                        break;
                    case R.id.about:
                        UbangInfoActivity.open(getContext());
                        break;
                    case R.id.info:
                        PersonInfoActivity.open(getContext());
                        break;
                    case R.id.setting:
                        break;
                    case R.id.quit:
                        try {
                            LoginActivity.open(GlobalHandler.Activities.get("MainActivity"));
                            GlobalHandler.Activities.get("MainActivity").finish();
                            GlobalHandler.Activities.clear();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
                Toast.makeText(getActivity(),menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }) ;
    }

    private void setupHeader() {
        name.setText(CP.user.getNickname());
        Glide.with(this).load(CP.user.getAvatar()).into(head_pic);
    }

}
