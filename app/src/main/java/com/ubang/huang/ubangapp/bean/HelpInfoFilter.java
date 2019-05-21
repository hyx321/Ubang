package com.ubang.huang.ubangapp.bean;

/**
 * Created by huang on 2019/4/13.
 * @auther
 */

import java.util.List;

public class HelpInfoFilter {
    private String helpTypeList;
    private String helpSortList;
    private int start;
    private int space;

    public String getHelpTypeList() {
        return helpTypeList;
    }

    public void setHelpTypeList(String helpTypeList) {
        this.helpTypeList = helpTypeList;
    }

    public String getHelpSortList() {
        return helpSortList;
    }

    public void setHelpSortList(String helpSortList) {
        this.helpSortList = helpSortList;
    }

    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getSpace() {
        return space;
    }
    public void setSpace(int space) {
        this.space = space;
    }
    @Override
    public String toString() {
        return "{\"helpTypeList\":\"" + helpTypeList + "\", \"helpSortList\":\"" + helpSortList + "\", \"start\":\""
                + start + "\", \"space\":\"" + space + "\"}";
    }

}