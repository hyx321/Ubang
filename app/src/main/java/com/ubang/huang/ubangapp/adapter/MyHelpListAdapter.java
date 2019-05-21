package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.HelpDetailInfoActivity;
import com.ubang.huang.ubangapp.activity.HelpingDetailInfoActivity;
import com.ubang.huang.ubangapp.activity.UnhelpDetailInfoActivity;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.common.CP;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 我发出求助的列表适配器
 */
public class MyHelpListAdapter extends RecyclerView.Adapter<MyHelpListAdapter.ViewHolder> {


    private JSONArray datas = null;
    private List<HelpInfoUpdate> helpInfoList = new ArrayList<>();
    private Context context;
    private HelpInfoUpdate helpInfo;

    public MyHelpListAdapter(List<HelpInfoUpdate> helpInfoList, Context context) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seekhelp_myhelp_content,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        helpInfo = new HelpInfoUpdate();
        helpInfo = helpInfoList.get(position);
        viewHolder.help_type.setText(helpInfo.getType());
        String date = helpInfo.getCreate_time();
        viewHolder.help_time.setText(date.substring(0,date.length()-2));
        viewHolder.help_content.setText(helpInfo.getContent());
        judgeInfoStaus(viewHolder,helpInfoList.get(position));
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (helpInfoList.get(position).getStatus()){
                    case "结束":
                        CP.help_id = helpInfoList.get(position).getId();
                        HelpDetailInfoActivity.open(context);
                        break;
                    case "进行中":
                        CP.help_id = helpInfoList.get(position).getId();
                        HelpingDetailInfoActivity.open(context);
                        break;
                    case "未开始":
                        CP.help_id = helpInfoList.get(position).getId();
                        UnhelpDetailInfoActivity.open(context);
                        break;
                }
            }
        });
    }

    /**
     * 判断帮助状态
     * @param viewHolder
     * @param helpInfo
     */
    private void judgeInfoStaus(ViewHolder viewHolder,HelpInfoUpdate helpInfo) {
        switch (helpInfo.getStatus()){
            case "进行中":
                viewHolder.help_status_info.setText(CP.HelpingStatus);
                viewHolder.help_status.setBackgroundResource(R.drawable.textview_background_helping);
                break;
            case "未开始":
                viewHolder.help_status_info.setText(CP.UnHelpStatus);
                viewHolder.help_status.setBackgroundResource(R.drawable.textview_background_unhelp);
                break;
            case "结束":
                viewHolder.help_status_info.setText(CP.HelpedStatus);
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
        @BindView(R.id.help_type)
        TextView help_type;
        @BindView(R.id.help_time)
        TextView help_time;
        @BindView(R.id.help_status)
        TextView help_status;
        @BindView(R.id.help_status_info)
        TextView help_status_info;
        @BindView(R.id.help_content)
        TextView help_content;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















