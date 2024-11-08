// 路徑：com/example/bottom_main/Event.java
package com.example.bottom_main;

public class Event {
    private String title;
    private String imageUrl;
    private String description;

    public Event(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
