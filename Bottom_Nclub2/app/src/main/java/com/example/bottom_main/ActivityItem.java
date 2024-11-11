package com.example.bottom_main;

import android.net.Uri;

public class ActivityItem {
    private String date;
    private String category;
    private String tag;
    private String imageUri;
    private String itemId;

    public ActivityItem(String date, String category, String tag, String imageUri, String itemId) {
        this.date = date;
        this.category = category;
        this.tag = tag;
        this.imageUri = imageUri;
        this.itemId = itemId;
    }

    // Getter 方法
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getTag() { return tag; }
    public String getImageUri() { return imageUri; }
    public String getItemId() { return itemId; }
}