package com.ubang.huang.ubangapp.bean;

/**
 * Created by huang on 2019/4/10.
 */

public class SendNews {
    private String keyword;
    private int start;
    private int num;
    private String appkey;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "?keyword=" + keyword +
                "&start=" + start +
                "&num=" + num +
                "&appkey=" + appkey;
    }
}
