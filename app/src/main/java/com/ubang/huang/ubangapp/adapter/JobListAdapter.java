package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.JobInfoActivity;
import com.ubang.huang.ubangapp.bean.Job;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.HotTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 */
public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {

    public interface ItemClickCallBack{
        void onItemClick(int pos);
    }

    private ItemClickCallBack clickCallBack;
    private Context context;
    private List<Job> jobInfoList = new ArrayList<>();
    private  ArrayList<String> tag_info;

    public JobListAdapter(List<Job> jobInfoList, Context context) {
        this.jobInfoList = jobInfoList;
        this.context = context;
    }

    /**
     * 创建新View，被LayoutManager所调用
     * @param viewGroup viewGroup
     * @param viewType viewType
     * @return view view
     */
    @NonNull
    @Override
    public JobListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_service_job_recycle_info,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder holder
     * @param position 位置
     */
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Job job = jobInfoList.get(position);
        viewHolder.position.setText(job.getJob_position());
        viewHolder.company.setText(job.getJob_company());
        viewHolder.salary.setText(job.getJob_salary());

        tag_info = new ArrayList<>();
        tag_info.add(job.getJob_place());
        tag_info.add(job.getJob_require_exp());
        tag_info.add(job.getJob_require_degree());

        viewHolder.info_tag.clear();
        viewHolder.info_tag.setTags(tag_info);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.jobInfo = job;
                JobInfoActivity.open(context);
            }
        });
    }


    @Override
    public void onBindViewHolder(JobListAdapter.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     *  获取数据的数量
     */

    @Override
    public int getItemCount() {
        return jobInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.position)
        TextView position;
        @BindView(R.id.company)
        TextView company;
        @BindView(R.id.salary)
        TextView salary;
        @BindView(R.id.info_tag)
        HotTagView info_tag;
        @BindView(R.id.contain)
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}





















