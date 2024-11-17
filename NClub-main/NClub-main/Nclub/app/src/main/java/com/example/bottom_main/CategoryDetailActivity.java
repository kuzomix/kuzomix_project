package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;  // RecyclerView，用來顯示活動列表
    private EventAdapter adapter;  // 活動的適配器
    private List<Event> events = new ArrayList<>();  // 用來存儲指定分類下的所有活動

    private String categoryId;  // 用來保存傳遞過來的分類 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);  // 設置正確的佈局

        // 初始化 RecyclerView 並設置適配器
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        adapter = new EventAdapter(events, this);

        // 設置 GridLayoutManager 顯示為兩列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        eventsRecyclerView.setLayoutManager(gridLayoutManager);

        // 設置適配器給 RecyclerView
        eventsRecyclerView.setAdapter(adapter);

        // 獲取從 Intent 傳遞過來的分類 ID
        categoryId = getIntent().getStringExtra("categoryId");

        // 如果分類 ID 有值，則加載該分類下的所有活動
        if (categoryId != null) {
            loadCategoryEvents(categoryId);  // 根據 categoryId 加載該分類的活動
        } else {
            // 如果分類 ID 無效，顯示提示
            Toast.makeText(this, "無效的分類 ID", Toast.LENGTH_SHORT).show();
        }
    }

    // 從 Firebase 根據分類 ID 加載活動資料
    private void loadCategoryEvents(String categoryId) {
        // 引用 Firebase 中的 "Items" 節點
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items");

        // 根據 category 篩選活動，這裡是查詢所有屬於指定 categoryId 的活動
        itemsRef.orderByChild("category").equalTo(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        events.clear();  // 清空舊的活動資料

                        // 遍歷查詢結果並將活動加入列表
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            String eventId = eventSnapshot.getKey();  // 活動 ID
                            String title = eventSnapshot.child("title").getValue(String.class);  // 活動標題
                            String imageUrl = eventSnapshot.child("pic").getValue(String.class);  // 圖片 URL
                            String description = eventSnapshot.child("description").getValue(String.class);  // 描述
                            String tag = eventSnapshot.child("tag").getValue(String.class);  // 標籤
                            String address = eventSnapshot.child("address").getValue(String.class);  // 地址
                            Integer bed = eventSnapshot.child("bed").getValue(Integer.class);  // 床位數（假設為整數）

                            // 如果資料有效，將活動資料封裝成 Event 對象並加入活動列表
                            if (title != null && imageUrl != null && description != null) {
                                // 將額外資料也加入到 Event 類中
                                Event event = new Event(eventId, title, imageUrl, description);
                                events.add(event);  // 添加到活動列表
                                Log.d("Firebase", "已載入活動: " + event.getTitle());
                            } else {
                                Log.w("Firebase", "資料缺失，略過此活動");
                            }
                        }

                        // 通知適配器資料已經更新，刷新 RecyclerView
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 如果 Firebase 加載資料失敗，顯示錯誤提示
                        Log.e("Firebase", "加載活動資料失敗", databaseError.toException());
                        Toast.makeText(CategoryDetailActivity.this, "加載活動資料失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
