package com.ubang.huang.ubangapp.bean;

/**
 * Created by huang on 2019/4/10.
 */

public class ServiceMessage {
    private int whoSend;
    private String content;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWhoSend() {
        return whoSend;
    }

    public void setWhoSend(int whoSend) {
        this.whoSend = whoSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
