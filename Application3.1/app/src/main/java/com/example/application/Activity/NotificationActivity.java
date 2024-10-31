package com.example.application.Activity;

import android.os.Bundle;
import android.widget.ListView;  // 導入 ListView 類
import androidx.appcompat.app.AppCompatActivity;
import com.example.application.R;

public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification); // 確保佈局檔案名正確

        ListView listView = findViewById(R.id.listview);
        // 設置 ListView 的數據或適配器（adapter）
        // 您可以在這裡設置 ListView 的 adapter
    }
}
