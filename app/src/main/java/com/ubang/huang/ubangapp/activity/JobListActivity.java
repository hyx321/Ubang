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
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.adapter.JobListAdapter;
import com.ubang.huang.ubangapp.async.AsyncGetJobList;
import com.ubang.huang.ubangapp.async.AsyncGetTag;
import com.ubang.huang.ubangapp.bean.JobInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.GetParam;
import com.ubang.huang.ubangapp.util.HotTagView;
import com.ubang.huang.ubangapp.util.NeverCarshXRecyclerView;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/11.
 * @author huang
 */

public class JobListActivity extends Activity{

    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    @BindView(R.id.back)
    ImageView back;

    View citys;
    View company;
    View require;
    HandlerThread handlerThread;
    Handler UIHandler;
    Handler handler;
    HotTagView expCloudView;
    HotTagView cityCloudView;
    HotTagView peopleCloudView;
    HotTagView degreeCloudView;
    HotTagView typeCloudView;
    BaseRecycleView baseRecycleView;
    NeverCarshXRecyclerView mRecyclerView;
    Button submit;
    int start = 0; //开始位置
    int space = 10;//间隔位置
    List<View> popupViews = new ArrayList<>();
    JobListAdapter jobListAdapter;
    ArrayList<HotTagView.TagView> cityTagViews;
    String cityList = "";
    String companyNumList = "";
    String companyTypeList = "";
    String expList = "";
    String degreeList = "";
    AsyncGetJobList asyncJobInfo;
    AsyncGetTag asyncTag;
    Map<Integer,HotTagView> tags = new HashMap<>();

    public static void open(Context context){
        context.startActivity(new Intent(context,JobListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_service_job);
        ButterKnife.bind(this);
        init();
    }
    void init(){
        addHandle();
        addBackListener();
        View contentView = getLayoutInflater().inflate(R.layout.comment_job_cycler, null);
        getTagData();
        addViewToList();
        initTag();
        addRecyclerView(contentView);
        dropDownMenu.setDropDownMenu(Arrays.asList(CP.header), popupViews, contentView);
        cityList = "'深圳',";
        CP.jobInfoList.clear();
        AsyncTask(true);
    }

    private void addBackListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobListActivity.this.finish();
            }
        });
    }

    public void getTagData(){
        asyncTag = new AsyncGetTag(handler);
        asyncTag.execute("");
    }
    public void addViewToList(){
        citys  = getLayoutInflater().inflate(R.layout.news_service_job_city_view, null);
        company = getLayoutInflater().inflate(R.layout.news_service_job_company_view, null);
        require = getLayoutInflater().inflate(R.layout.news_service_job_require_view, null);
        popupViews.add(citys);
        popupViews.add(company);
        popupViews.add(require);
    }


    private void addHandle() {
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        UIHandler = new Handler();
        handler = new Handler(handlerThread.getLooper()){
            @Override
            //消息处理的操作
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CP.GetTag:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                expCloudView.setTags(CP.MOUDLE_JOB_EXP_TAG);
                                cityCloudView.setTags(CP.MOUDLE_JOB_CITY_TAG);
                                peopleCloudView.setTags(CP.MOUDLE_JOB_PEOPLE_TAG);
                                degreeCloudView.setTags(CP.MOUDLE_JOB_DEGREE_TAG);
                                typeCloudView.setTags(CP.MOUDLE_JOB_COMPANY_TYPE_TAG);
                                setTagColor(cityCloudView,CP.CITY_INFO);
                                setTagColor(peopleCloudView,CP.COMPANY_SUM_INFO);
                                setTagColor(typeCloudView,CP.COMPANY_TYPE_INFO);
                                setTagColor(degreeCloudView,CP.DEGREE_INFO);
                                setTagColor(expCloudView,CP.EXP_INFO);
                            }
                        });
                        break;
                    case CP.GetJobInfo:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mRecyclerView.loadMoreComplete();
                                    jobListAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void initTag(){
        for(int i=0;i<popupViews.size();i++) {
            switch (i){
                case 0:
                    cityCloudView = popupViews.get(i).findViewById(R.id.city_tag);
                    setCloseMenu(popupViews.get(i),cityCloudView,CP.CITY_INFO);
                    tags.put(CP.CITY_INFO,cityCloudView);
                    break;
                case 1:
                    peopleCloudView = popupViews.get(i).findViewById(R.id.people_tag);
                    setCloseMenu(popupViews.get(i),peopleCloudView,CP.COMPANY_SUM_INFO);
                    tags.put(CP.COMPANY_SUM_INFO,peopleCloudView);
                    typeCloudView = popupViews.get(i).findViewById(R.id.company_tag);
                    setCloseMenu(popupViews.get(i),typeCloudView,CP.COMPANY_TYPE_INFO);
                    tags.put(CP.COMPANY_TYPE_INFO,typeCloudView);
                    break;
                case 2:
                    degreeCloudView = popupViews.get(i).findViewById(R.id.degree_tag);
                    setCloseMenu(popupViews.get(i),degreeCloudView,CP.DEGREE_INFO);
                    tags.put(CP.DEGREE_INFO,degreeCloudView);
                    expCloudView = popupViews.get(i).findViewById(R.id.experience_tag);
                    setCloseMenu(popupViews.get(i),expCloudView,CP.EXP_INFO);
                    tags.put(CP.EXP_INFO,expCloudView);
                    break;
            }
        }
    }

    private void setCloseMenu(View view,final HotTagView tagCloudView,final int index) {
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityTagViews = (ArrayList<HotTagView.TagView>)tagCloudView.getTagViews();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        addTag(index);
                        start = 0;
                        dropDownMenu.closeMenu();
                        AsyncTask(true);
                    }
                });

            }
        });
    }
    public void clearTag(int index) {
        switch (index){
            case CP.CITY_INFO:
                cityList = "";
                break;
            case CP.COMPANY_SUM_INFO:
                companyNumList = "";
                break;
            case CP.COMPANY_TYPE_INFO:
                companyTypeList = "";
                break;
            case CP.EXP_INFO:
                expList = "";
                break;
            case CP.DEGREE_INFO:
                degreeList = "";
                break;
        }
    }
    public void addTag(int index){
        switch (index){
            case 0:
                clearTag(index);
                addTagData(index);
                break;
            case 2:
                clearTag(index-1);
                addTagData(index-1);
                clearTag(index);
                addTagData(index);
                break;
            case 3:
                clearTag(index);
                addTagData(index);
                clearTag(index+1);
                addTagData(index+1);
                break;
        }
    }
    public void addTagData(int index){
        ArrayList<HotTagView.TagView> tagViews = (ArrayList<HotTagView.TagView>)tags.get(index).getTagViews();
        for(int i=0;i<tagViews.size();i++){
            if(!isClick(tagViews.get(i))) {
                String text = tagViews.get(i).getText().toString();
                switch (index){
                    case CP.CITY_INFO:
                        cityList += "'"+text+"'"+",";
                        break;
                    case CP.COMPANY_SUM_INFO:
                        companyNumList += "'"+text+"'"+",";
                        break;
                    case CP.COMPANY_TYPE_INFO:
                        companyTypeList += "'"+text+"'"+",";
                        break;
                    case CP.EXP_INFO:
                        expList += "'"+text+"'"+",";
                        break;
                    case CP.DEGREE_INFO:
                        degreeList += "'"+text+"'"+",";
                        break;
                }
            }
        }
    }

    public Boolean isClick(TextView textView){
        return textView.getCurrentTextColor() != getResources().getColor(R.color.text_color);
    }
    public void AsyncTask(Boolean isCleanData){
        JobInfo jobInfo = new JobInfo();
        jobInfo.setCityList(GetParam.adjustListToString(cityList));
        jobInfo.setCompanyNumList(GetParam.adjustListToString(companyNumList));
        jobInfo.setCompanyTypeList(GetParam.adjustListToString(companyTypeList));
        jobInfo.setDegreeList(GetParam.adjustListToString(degreeList));
        jobInfo.setExpList(GetParam.adjustListToString(expList));
        jobInfo.setSpace(space);
        jobInfo.setStart(start);
        asyncJobInfo = new AsyncGetJobList(handler,jobInfo,isCleanData);
        asyncJobInfo.execute("");

    }

    public void setTagColor(final HotTagView tagCloudView, final int index){
        final ArrayList<HotTagView.TagView> tagViews = (ArrayList<HotTagView.TagView>)tagCloudView.getTagViews();
        for(int i=0;i<tagViews.size();i++){
            tagViews.get(i).setTextSize(20);
            tagViews.get(i).setTextColor(Color.WHITE);
            tagViews.get(i).setmTextColor(Color.GRAY);
            tagViews.get(i).setmLinePaintColor(Color.GRAY);
        }

        tagCloudView.setOnTagClickListener(new HotTagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(tagViews.get(position).getCurrentTextColor() == Color.WHITE){
                            tagViews.get(position).setTextColor(getResources().getColor(R.color.text_color));
                            tagViews.get(position).setmLinePaintColor(R.color.line_color);
                        }else {
                            tagViews.get(position).setTextColor(Color.WHITE);
                            tagViews.get(position).setmTextColor(Color.GRAY);
                            tagViews.get(position).setmLinePaintColor(Color.GRAY);
                        }
                    }
                });
            }
        });
    }

    public void addRecyclerView(View view){
        mRecyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingListener(new NeverCarshXRecyclerView.LoadingListener() {

            public void onRefresh() {
                mRecyclerView.refreshComplete();
            }

            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecyclerView != null) {
                            start+=space;
                            AsyncTask(false);
                        }
                    }
                }, 1000);
            }
        });
        jobListAdapter = new JobListAdapter(CP.jobInfoList,this);
        mRecyclerView.setAdapter(jobListAdapter);
    }
}

