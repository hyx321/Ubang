package com.ubang.huang.ubangapp.bean;

/**
 * Created by huang on 2019/4/10.
 */

public class GetNewsUpdate {
    private String id;
    private String html;
    private String source;
    private String pubDate;
    private String title;
    private Boolean havePic;
    private String desc;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getHavePic() {
        return havePic;
    }

    public void setHavePic(Boolean havePic) {
        this.havePic = havePic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\'" + id + "\'" +
                ", \"html\":\'" + html + "\'" +
                ", \"source\":\'" + source + "\'" +
                ", \"pubDate\":\'" + pubDate + "\'" +
                ", \"title\":\'" + title + "\'" +
                ", \"havePic\":" + havePic +
                ", \"desc\":\'" + desc + "\'" +
                ", \"url\":\'" + url + "\'" +
                '}';
    }
}