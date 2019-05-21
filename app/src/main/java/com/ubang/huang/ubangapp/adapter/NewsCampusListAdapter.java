package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.NewsCampusContentActivity;
import com.ubang.huang.ubangapp.activity.NewsCampusContentUpdate;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.bean.CampusSafe;
import com.ubang.huang.ubangapp.bean.GetNews;
import com.ubang.huang.ubangapp.common.CP;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 报警求助中求助内容的适配器
 */
public class NewsCampusListAdapter extends RecyclerView.Adapter<NewsCampusListAdapter.ViewHolder> {


    private List<CampusSafe> campusSafes = new ArrayList<>();
    private Context context;
    private String content;
    CampusSafe campusSafe;

    public NewsCampusListAdapter(List<CampusSafe> campusSafes, Context context) {
        this.campusSafes = campusSafes;
        this.context = context;
    }

    /**
     * 创建新View，被LayoutManager所调用
     * @param viewGroup viewGroup
     * @param viewType viewType
     * @return viewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_campus_recycle_content,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        campusSafe = campusSafes.get(position);
        viewHolder.title.setText(campusSafe.getTitle());
        viewHolder.content.setText(campusSafe.getAbstract_content());
        viewHolder.web_src.setText(campusSafe.getSource());
        String time = campusSafe.getDatetime();
        viewHolder.date.setText(time.substring(0,time.length()-2));
        if(campusSafe.getHas_image().equals("False")){
            viewHolder.pic.setVisibility(View.GONE);
        }else{
            getUrlPic(viewHolder.pic,campusSafe.getImage_url());
        }
        getUrlPic(viewHolder.news_pic,campusSafe.getMedia_avatar_url());

        viewHolder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.campusSafe = campusSafes.get(position);
                NewsCampusContentUpdate.open(context);
            }
        });
    }

    private void getUrlPic(ImageView imageView,String url) {
        Glide.with(context).load(url).into(imageView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     *  获取数据的数量
     */

    @Override
    public int getItemCount() {
        return campusSafes.size();
    }


    /**
     *自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.web_src)
        TextView web_src;
        @BindView(R.id.pic)
        ImageView pic;
        @BindView(R.id.contain)
        CardView contain;
        @BindView(R.id.news_pic)
        ImageView news_pic;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















