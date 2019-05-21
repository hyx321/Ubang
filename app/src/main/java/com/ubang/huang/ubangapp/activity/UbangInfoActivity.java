package com.ubang.huang.ubangapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.ubang.huang.ubangapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UbangInfoActivity extends Activity {

    @BindView(R.id.back)
    ImageView back;

    public static void open(Context context){
        context.startActivity(new Intent(context, UbangInfoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_about_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UbangInfoActivity.this.finish();
            }
        });
    }
}
