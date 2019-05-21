package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.GetHelpActivity;
import com.ubang.huang.ubangapp.activity.GetHelpLearn;
import com.ubang.huang.ubangapp.activity.HelpDetailInfoActivity;
import com.ubang.huang.ubangapp.async.AsyncGetPerson;
import com.ubang.huang.ubangapp.async.AsyncGetPic;
import com.ubang.huang.ubangapp.async.GetHelpOtherListPic;
import com.ubang.huang.ubangapp.async.GetHelpPic;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfo;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.PictureURL;
import com.ubang.huang.ubangapp.bean.RoughlyHelpInfo;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.common.GlobalHandler;
import com.ubang.huang.ubangapp.common.Signal;
import com.ubang.huang.ubangapp.socket.RemindOther;
import com.ubang.huang.ubangapp.util.BaseRecycleView;
import com.ubang.huang.ubangapp.util.NeverCarshXRecyclerView;
import com.ubang.huang.ubangapp.util.RoundImageView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 我发出求助的列表适配器
 */
public class HelpOthersListAdapter extends RecyclerView.Adapter<HelpOthersListAdapter.ViewHolder> {


    private JSONArray datas = null;
    private List<SeekHelpInfo> helpInfoList = new ArrayList<>();
    private Context context;
    private SeekHelpInfo help;
    private AsyncGetPic asyncGetPic;
    private AsyncGetPerson asyncGetPerson;
    private Handler UIHandler;
    private HandlerThread handlerThread;
    private Handler DataHandler;
    private User user;
    private List<PictureURL> picList = new ArrayList<>();
    private NetPicAdapter picAdapter;

    public HelpOthersListAdapter(List<SeekHelpInfo> helpInfoList, Context context) {
        this.helpInfoList = helpInfoList;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.helpothers_recycle_content,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        help = new SeekHelpInfo();
        help = helpInfoList.get(position);
        addHandle(viewHolder);
        viewHolder.name.setText(help.getNickname());
        Glide.with(context).load(help.getAvatar()).into(viewHolder.head_pic);
        viewHolder.content.setText(help.getContent());
        judgeInfoStaus(viewHolder,help.getIs_urgent());
        viewHolder.label.setText(help.getType());
        addRecycle1(viewHolder,position);
        String date = help.getCreate_time();
        viewHolder.date.setText(date.substring(0,date.length()-2));

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.seekHelpInfo = helpInfoList.get(position);
                if(helpInfoList.get(position).getType().equals("学习")){
                    GetHelpLearn.open(context);
                }else{
                    GetHelpActivity.open(context);
                }
            }
        });
    }

    private void addRecycle1(ViewHolder viewHolder,int position) {
         LinearLayoutManager layoutManager;
         layoutManager = new GridLayoutManager(context,3);
         viewHolder.recyclerView.setLayoutManager(layoutManager);
         viewHolder.recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
         viewHolder.recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        if(helpInfoList.get(position).getHas_picture() == 1) {
            picAdapter = new NetPicAdapter(helpInfoList.get(position).getPisList(), context);
            viewHolder.recyclerView.setAdapter(picAdapter);
        }else{
            viewHolder.recyclerView.setVisibility(View.GONE);
        }
    }

    private void addHandle(ViewHolder viewHolder) {
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        UIHandler = new Handler();
        DataHandler = new Handler(handlerThread.getLooper()){
            @Override
            //消息处理的操作
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case Signal.GetHelpPic:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    viewHolder.recyclerView.removeAllViews();
                                    viewHolder.recyclerView.refreshComplete();
                                    picAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                }
            }
        };
    }

    /**
     * 判断帮助状态
     * @param viewHolder
     * @param isNormal
     */
    private void judgeInfoStaus(ViewHolder viewHolder,int isNormal) {
        switch (isNormal){
            case 0:
                viewHolder.help_status.setBackgroundResource(R.drawable.textview_background_unhelp);
                break;
            case 1:
                viewHolder.help_status.setBackgroundResource(R.drawable.textview_background_helped);
                break;

        }
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
        return helpInfoList.size();
    }

    /**
     *自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contain)
        CardView cardView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.head_pic)
        RoundImageView head_pic;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.help_status)
        TextView help_status;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.label)
        TextView label;
        @BindView(R.id.neverRecyclerview)
        NeverCarshXRecyclerView recyclerView;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}