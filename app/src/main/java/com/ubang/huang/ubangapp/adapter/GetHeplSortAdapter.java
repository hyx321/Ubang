package com.ubang.huang.ubangapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.mapsdkplatform.comapi.map.aa;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.activity.PublishHelpActivity;
import com.ubang.huang.ubangapp.async.AsyncGetHelpList;
import com.ubang.huang.ubangapp.bean.HelpInfoFilter;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.GetParam;
import com.ubang.huang.ubangapp.util.HotTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/2/24.
 *  弹出紧急求助和普通求助的按钮适配器
 */

public class GetHeplSortAdapter extends BaseAdapter{

    private Context context;
    private Activity activity;
    private String typeList = "";
    private String sortList = "";
    private Handler handler;
    private ArrayList<HotTagView.TagView> NormalViews;
    private ArrayList<HotTagView.TagView> SortViews;
    private List<SeekHelpInfo> helpInfoUpdates;
    private int start;

    public GetHeplSortAdapter(Activity activity, Context context,Handler handler, List<SeekHelpInfo> helpInfoUpdates,int start){
        this.context = context;
        this.activity = activity;
        this.handler = handler;
        this.helpInfoUpdates = helpInfoUpdates;
        this.start = start;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.helpothers_common_sort_item, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setTagColor(viewHolder.help_type,CP.help_type,0);
        setTagColor(viewHolder.help_sort,CP.help_sort,1);
        return view;
    }
    private void setTagColor(final HotTagView tagCloudView,ArrayList<String> arrayList,int id) {
        tagCloudView.setTags(arrayList);
        ArrayList<HotTagView.TagView> tagViews = (ArrayList<HotTagView.TagView>) tagCloudView.getTagViews();
        for (int i = 0; i < tagViews.size(); i++) {
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
                            tagViews.get(position).setTextColor(context.getResources().getColor(R.color.text_color));
                            tagViews.get(position).setmLinePaintColor(R.color.line_color);
                        }else {
                            tagViews.get(position).setTextColor(Color.WHITE);
                            tagViews.get(position).setmTextColor(Color.GRAY);
                            tagViews.get(position).setmLinePaintColor(Color.GRAY);
                        }

                        if(id == 0){
                            NormalViews = tagViews;
                        }else{
                            SortViews = tagViews;
                        }
                        addTag(position,id);
                        GetHelpList(false);

                    }
                });
            }
        });
    }

    private void GetHelpList(Boolean isClean){
        HelpInfoFilter helpInfoFilter = new HelpInfoFilter();
        helpInfoFilter.setHelpSortList(GetParam.adjustListToString(sortList));
        helpInfoFilter.setHelpTypeList(GetParam.adjustListToString(typeList));
        helpInfoFilter.setSpace(CP.user.getId());
        helpInfoFilter.setStart(start);
        AsyncGetHelpList asyncGetHelpList = new AsyncGetHelpList(handler,helpInfoFilter,isClean,helpInfoUpdates);
        asyncGetHelpList.execute("");
    }

    private void addTag(int position,int id){
        ArrayList<HotTagView.TagView> tagViews;
        if(id == 0){
            typeList ="";
            tagViews = NormalViews;
        }else{
            sortList = "";
            tagViews = SortViews;
        }
        for(int i=0;i<tagViews.size();i++){
            if(tagViews.get(i).getCurrentTextColor() != Color.WHITE) {
                if (id == 0) {
                    if(tagViews.get(i).getText().toString().equals("普通求助")){
                        typeList += "'" + 0 + "'" + ",";
                    }else{
                        typeList += "'" + 1 + "'" + ",";
                    }
                } else {
                    sortList += "'" + tagViews.get(i).getText().toString() + "'" + ",";
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return 1;
    }

    public static class  ViewHolder {
        @BindView(R.id.contain)
        LinearLayout contain;
        @BindView(R.id.help_type)
        HotTagView help_type;
        @BindView(R.id.help_sort)
        HotTagView help_sort;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
