package com.ubang.huang.ubangapp.common;

import android.app.Activity;
import android.os.Handler;

import com.ubang.huang.ubangapp.bean.PictureURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalHandler {
    public static Handler NotiHanler;
    public static Handler ChatMessageHanler;
    public static Map<String,Handler> ChatHanlers = new HashMap<>();
    public static Map<String, Activity> Activities = new HashMap<>();
    public static Map<Integer,List<PictureURL>> pics = new HashMap<>();
}
