package com.example.bottom_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class SeeAllPopular extends AppCompatActivity{
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;
    private List<Event> recommendedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_recommended);

        recyclerView = findViewById(R.id.recyclerView);

        // 設置 GridLayoutManager，每行顯示兩個項目
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 2; // 列數
        int spacing = 16; // 間距（像素）
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        // 初始化活動數據並設置適配器
        recommendedEvents = getAllRecommendedEvents();
        eventAdapter = new EventAdapter(recommendedEvents);
        recyclerView.setAdapter(eventAdapter);
    }

    private List<Event> getAllRecommendedEvents() {
        // 這裡添加一些假數據或從數據庫中加載
        List<Event> events = new ArrayList<>();
        events.add(new Event("Event 1", "https://firebasestorage.googleapis.com/v0/b/application-b3354.appspot.com/o/hot.jpg?alt=media&token=de82566d-7154-4bf9-8ec8-5bb64af573dc", "徵求熱氣球觀賞夥伴~"));
        events.add(new Event("Event 2", "https://firebasestorage.googleapis.com/v0/b/application-b3354.appspot.com/o/Taipei101.jpg?alt=media&token=e7c10f7c-2919-46d3-872b-d3f05602dcd9", "有人跨年想要一起跨年的嗎~ 都可以參加!!"));
        return events;
    }
}
