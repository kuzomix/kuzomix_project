package com.example.bottom_main.Domain;
import androidx.recyclerview.widget.GridLayoutManager;

public class RecommendedAll {
        private RecyclerView recyclerView;
        private EventAdapter eventAdapter;
        private List<Event> recommendedEvents;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_all_recommended_events);

            recyclerView = findViewById(R.id.recyclerView);

            // 設置 GridLayoutManager，每行顯示兩個項目
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            // 初始化活動數據並設置適配器
            recommendedEvents = getAllRecommendedEvents();
            eventAdapter = new EventAdapter(recommendedEvents);
            recyclerView.setAdapter(eventAdapter);
        }

        private List<Event> getAllRecommendedEvents() {
            // 從資料庫或伺服器中獲取所有推薦活動
            return new ArrayList<>();
        }
    }

}
