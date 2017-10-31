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
    private Bitmap imageBitmap;
    private String description;
    private String pubDate;
    private String link;

    public Item(String title, Bitmap imageBitmap, String description, String pubDate, String link) {
        this.title = title;
        this.imageBitmap = imageBitmap;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
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
