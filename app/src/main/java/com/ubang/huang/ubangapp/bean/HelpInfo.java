package com.ubang.huang.ubangapp.bean;

/**
 * Created by huang on 2019/4/4.
 */

public class HelpInfo {
    private String seekHerpName;
    private String seekHelpTime;
    private String helpContent;
    private String helpType;
    private int isFinished;
    private String helpName;

    public String getSeekHerpName() {
        return seekHerpName;
    }

    public void setSeekHerpName(String seekHerpName) {
        this.seekHerpName = seekHerpName;
    }

    public String getSeekHelpTime() {
        return seekHelpTime;
    }

    public void setSeekHelpTime(String seekHelpTime) {
        this.seekHelpTime = seekHelpTime;
    }

    public String getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }

    public int getFinished() {
        return isFinished;
    }

    public void setFinished(int finished) {
        isFinished = finished;
    }

    public String getHelpName() {
        return helpName;
    }

    public void setHelpName(String helpName) {
        this.helpName = helpName;
    }

    public String getHelpType() {
        return helpType;
    }

    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }
}
