package com.ubang.huang.ubangapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ubang.huang.ubangapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2019/1/18.
 *
 * @author = huangyouxin
 * 显示手机内图片的适配器
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    public interface ItemClickCallBack{
        void onItemClick(int pos);
    }

    private List<String> picList = new ArrayList<>();
    private ItemClickCallBack clickCallBack;
    private Context context;

    public PicAdapter(List<String> picList, Context context) {
        this.picList = picList;
        this.context = context;
    }

    /**
     * 创建新View，被LayoutManager所调用
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_pic_item,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     * @param viewHolder viewHolder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        //viewHolder.imageView.setImageBitmap(getBitmap(picList.get(position)));
        viewHolder.imageView.setImageURI(Uri.fromFile(new File(picList.get(position))));
    }

    @Nullable
    private Bitmap getBitmap(String url){
        try {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = context.getContentResolver().query(Uri.parse(url), filePathColumns, null, null, null);
            c.moveToFirst();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            return BitmapFactory.decodeFile(c.getString(c.getColumnIndex(filePathColumns[0])));
        }catch (Exception e){
            e.printStackTrace();
            return null;
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
        return picList.size();
    }

    /**
    *自定义的ViewHolder，持有每个Item的的所有界面元素
    */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}





















