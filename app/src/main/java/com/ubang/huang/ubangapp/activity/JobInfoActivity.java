package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.bean.Job;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.HotTagView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/20.
 *
 * @author = huangyouxin
 */

public class JobInfoActivity extends Activity{

    private Job jobInfo;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.position)
    TextView position;
    @BindView(R.id.salary)
    TextView salary;
    @BindView(R.id.tag_info)
    HotTagView tag_info;
    @BindView(R.id.require_info)
    TextView require_info;
    @BindView(R.id.company_pic)
    ImageView company_pic;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.company_field)
    TextView company_field;

    public static void open(Context context) {
        context.startActivity(new Intent(context, JobInfoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_service_job_info_activity);
        init();
    }

    private void init(){
        ButterKnife.bind(this);
        jobInfo = CP.jobInfo;
        addBack();
        addInfo();
    }

    private void addInfo() {
        position.setText(jobInfo.getJob_position());
        salary.setText(jobInfo.getJob_salary());
        ArrayList<String> tag = new ArrayList<>();
        tag.add(jobInfo.getJob_place());
        tag.add(jobInfo.getJob_require_exp());
        tag.add(jobInfo.getJob_require_degree());
        tag_info.setTags(tag);
        company_name.setText(jobInfo.getJob_company());
        company_field.setText(jobInfo.getCompany_field());
        require_info.setText(jobInfo.getJob_require());
    }

    private void addBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobInfoActivity.this.finish();
            }
        });
    }

}
