package com.ubang.huang.ubangapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.InputAlarmHelpContent;
import com.ubang.huang.ubangapp.bean.AlarmContent;
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
public class DangerListAdapter extends RecyclerView.Adapter<DangerListAdapter.ViewHolder> {


    private List<AlarmContent> perInfoList = new ArrayList<>();
    private Context context;
    private String content;

    public DangerListAdapter(List<AlarmContent> perInfoList, Context context) {
        this.perInfoList = perInfoList;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seekhelp_danger_help_content,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.content_text.setText(perInfoList.get(position).getAlarm_content());
        getContetnt(perInfoList.get(position).getAlarm_content());
        switch (position){
            case 0:
                viewHolder.alarm_block.setBackgroundColor(context.getResources().getColor(R.color.alarm_first));
                break;
            case 1:
                viewHolder.alarm_block.setBackgroundColor(context.getResources().getColor(R.color.alarm_second));
                break;
            case 2:
                viewHolder.alarm_block.setBackgroundColor(context.getResources().getColor(R.color.alarm_third));
                break;
        }
        viewHolder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CP.alarm_content_num = position;
                InputAlarmHelpContent.open(context);
            }
        });

        viewHolder.contain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewHolder.contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //client = HttpClientUtil.getInstance();
                        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
//                        int result = client.sendMsgUtf8(CP.Uid,CP.Key,content,CP.smsMob);
//                        if(result>0){
//                            System.out.println("发送成功");
//                        }else{
//                            System.out.println(client.getErrorMsg(result));
//                        }
                    }
                });
                return false;
            }
        });
    }

    private void getContetnt(String str){
        content = "姓名：\t"+CP.personInfo.getName()+"\n"+
                    "性别：\t"+CP.personInfo.getSex()+"\n"+
                     "电话：\t"+CP.personInfo.getPhone()+"\n"+
                    "城市：\t"+CP.personInfo.getCity()+"\n"+
                    "地址：\t"+CP.personInfo.getAddress()+"\n"+
                    "详细地址：\t"+CP.personInfo.getDetailAdress()+"\n"+
                    "经纬度：\t"+CP.personInfo.getLatLon()+"\n"+
                    "发出的求助：\t"+str+"\n";
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
        return perInfoList.size();
    }


    /**
     *自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content_text)
        TextView content_text;
        @BindView(R.id.contain)
        CardView contain;
        @BindView(R.id.alarm_block)
        RelativeLayout alarm_block;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















