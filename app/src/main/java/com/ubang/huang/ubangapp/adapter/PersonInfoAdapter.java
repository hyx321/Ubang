package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.bean.PersonInfo;
import com.ubang.huang.ubangapp.common.CP;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 报警中个人信息适配器
 */
public class PersonInfoAdapter extends RecyclerView.Adapter<PersonInfoAdapter.ViewHolder> {


    private PersonInfo personInfo = new PersonInfo();
    private Context context;

    public PersonInfoAdapter(PersonInfo personInfo, Context context) {
        this.personInfo = personInfo;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seekhelp_danger_help_detail_content,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.name_text.setText(personInfo.getName());
        viewHolder.sex_text.setText(personInfo.getSex());
        viewHolder.phone_text.setText(personInfo.getPhone());
        viewHolder.city_text.setText(personInfo.getCity());
        viewHolder.address_text.setText(personInfo.getAddress());
        viewHolder.latlng_text.setText(personInfo.getLatLon());
        viewHolder.detail_address_text.setText(personInfo.getDetailAdress());
        viewHolder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
        return 1;
    }


    /**
     *自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_text)
        TextView name_text;
        @BindView(R.id.sex_text)
        TextView sex_text;
        @BindView(R.id.phone_text)
        TextView phone_text;
        @BindView(R.id.city_text)
        TextView city_text;
        @BindView(R.id.address_text)
        TextView address_text;
        @BindView(R.id.latlng_text)
        TextView latlng_text;
        @BindView(R.id.detail_address_text)
        TextView detail_address_text;
        @BindView(R.id.contain)
        CardView contain;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















