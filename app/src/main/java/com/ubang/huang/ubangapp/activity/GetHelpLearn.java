package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ubang.huang.ubangapp.R;
import com.ubang.huang.ubangapp.common.CP;
import com.ubang.huang.ubangapp.util.RoundImageView;

import java.text.Bidi;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/4/14.
 * @author huang
 */

public class GetHelpLearn extends Activity{
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
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.helper_contain)
    CardView helper_contain;
    @BindView(R.id.get_help)
    Button get_help;
    @BindView(R.id.back)
    ImageView back;

    public static void open(Context context) {
        context.startActivity(new Intent(context, GetHelpLearn.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpothers_gethelp_learn);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        setInfo();
        addListener();
    }

    private void addListener() {
        get_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper_contain.setVisibility(View.VISIBLE);
                get_help.setVisibility(View.GONE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetHelpLearn.this.finish();
            }
        });
    }

    private void setInfo() {
        helper_contain.setVisibility(View.GONE);
        content.setText(CP.helpInfo.getHelp_content());
        date.setText(CP.helpInfo.getHelp_date());
        name.setText(CP.helpInfo.getHelp_name());

    }
}
