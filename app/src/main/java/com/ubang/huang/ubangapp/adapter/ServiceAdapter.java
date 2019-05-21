package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.bean.ServiceMessage;
import com.ubang.huang.ubangapp.common.CP;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 */
public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickCallBack{
        void onItemClick(int pos);
    }

    private ItemClickCallBack clickCallBack;
    private Context context;
    private List<ServiceMessage> serviceMessages = new ArrayList<>();

    public ServiceAdapter(List<ServiceMessage> serviceMessages, Context context) {
        this.serviceMessages = serviceMessages;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == CP.Service){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_service_left,viewGroup,false);
            return new LeftViewHolder(view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_service_right,viewGroup,false);
            return new RightViewHolder(view);
        }

    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder holder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            ServiceMessage serviceMessage = serviceMessages.get(position);
            if (viewHolder instanceof LeftViewHolder) {
                setLeftView(((LeftViewHolder) viewHolder), serviceMessage);
            } else if (viewHolder instanceof RightViewHolder) {
                setRightView(((RightViewHolder) viewHolder), serviceMessage);
            }
    }

    private void setLeftView(LeftViewHolder view,ServiceMessage serviceMessage) {
        view.time.setText(serviceMessage.getTime());
        view.message.setText(serviceMessage.getContent());
    }

    private void setRightView(RightViewHolder view,ServiceMessage serviceMessage) {
        view.time.setText(serviceMessage.getTime());
        Glide.with(context).load(CP.user.getAvatar()).into(view.avatar);
        view.message.setText(serviceMessage.getContent());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     *  获取数据的数量
     */

    @Override
    public int getItemCount() {
        return serviceMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return serviceMessages.get(position).getWhoSend();
    }

    public static class LeftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_time)
        TextView time;
        @BindView(R.id.tv_msg_left)
        TextView message;

        public LeftViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public static class RightViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_right_time)
        TextView time;
        @BindView(R.id.tv_msg_right)
        TextView message;
        @BindView(R.id.img_phone)
        ImageView avatar;

        public RightViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}





















