package com.ruili.personalcapitaldemo.model;

import android.graphics.Bitmap;

/**
 * Created by Rui Li on 10/29/17.
 */

/**
 * This class holds an in-memory representation of an article
 */
public class Item {
    private String title;
    private String imageUrl;
    private String description;
    private String pubDate;
    private String link;

    public Item(String title, String imageUrl, String description, String pubDate, String link) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }
}
