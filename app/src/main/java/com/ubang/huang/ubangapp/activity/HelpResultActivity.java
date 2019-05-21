package com.ubang.huang.ubangapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.BeanParam;
import com.ubang.huang.ubangapp.common.CP;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/5/4.
 * @author huang
 */

public class HelpResultActivity extends Activity{

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help_type)
    TextView help_type;
    @BindView(R.id.create_time)
    TextView create_time;
    @BindView(R.id.create_start_detail_time)
    TextView create_start_detail_time;
    @BindView(R.id.create_end_detail_time)
    TextView create_end_detail_time;
    @BindView(R.id.total_time)
    TextView total_time;
    @BindView(R.id.total_distant)
    TextView total_distant;
    @BindView(R.id.runman)
    ImageView runman;

    public static void open(Context context){
        context.startActivity(new Intent(context,HelpResultActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekhelp_help_detail_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setInfo();
        addBackListener();
    }

    private void addBackListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpResultActivity.this.finish();
            }
        });
    }

    private void setInfo() {

        help_type.setText(BeanParam.HelpInfo.getType());
        String publish_time = BeanParam.HelpInfo.getPublish_time();
        create_time.setText(publish_time.substring(0,10));
        String start_time = BeanParam.HelpInfo.getCreate_time();
        create_start_detail_time.setText(start_time.substring(0,start_time.length()-2));
        String end_time = BeanParam.HelpInfo.getEnd_time();
        create_end_detail_time.setText(end_time.substring(0,end_time.length()-2));
        String DistanceTime = getDistanceTime(start_time.substring(0,start_time.length()-2),end_time.substring(0,end_time.length()-2));
        String totalTime = "合计 "+DistanceTime;
        total_time.setText(totalTime);
        String totalDistant = "合计 "+ CP.Distant+" 米";
        total_distant.setText(totalDistant);

        getAnimationDrawable(false).start();
    }

    public static String getDistanceTime(String starttime, String endtime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(starttime);
            two = df.parse(endtime);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = day + "天" + hour + "小时" + min + "分" + sec + "秒";
        if (day == 0){
            if (hour == 0){
                if(min == 0){
                    result = sec + "秒";
                }else {
                    result = min + "分" + sec + "秒";
                }
            }else {
                result = hour + "小时" + min + "分" + sec + "秒";
            }
        }

        return result;
    }

    @TargetApi(21)
    private AnimationDrawable getAnimationDrawable(boolean fromXml) {
        if (fromXml) {
            //mPuppet.setImageDrawable(getResources().getDrawable(R.drawable.run));
            AnimationDrawable animationDrawable = (AnimationDrawable) runman.getDrawable();
            //animationDrawable.setOneShot(false);
            return animationDrawable;
        } else {
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (int i = 0; i < 31; i++) {
                int id = getResources().getIdentifier("runman" + i, "drawable", getPackageName());
                Drawable drawable = getDrawable(id);
                if (null != drawable) {
                    animationDrawable.addFrame(drawable, 100);
                }
            }
            runman.setImageDrawable(animationDrawable);
            return animationDrawable;
        }
    }

}
