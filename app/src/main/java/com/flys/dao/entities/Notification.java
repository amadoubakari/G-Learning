package com.flys.dao.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "notification")
public class Notification extends BaseEntity {

    @DatabaseField
    private String title;
    @DatabaseField
    private String subTtitle;
    @DatabaseField
    private String content;
    @DatabaseField
    private String imageUrl;

    public Notification() {
    }

    public Notification(String title, String subTtitle, String content, String imageUrl) {
        this.title = title;
        this.subTtitle = subTtitle;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTtitle() {
        return subTtitle;
    }

    public void setSubTtitle(String subTtitle) {
        this.subTtitle = subTtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", subTtitle='" + subTtitle + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
