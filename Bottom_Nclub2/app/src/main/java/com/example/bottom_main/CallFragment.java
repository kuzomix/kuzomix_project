package com.example.bottom_main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;   //11.10添加
import android.widget.ArrayAdapter;  //11.10添加
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CallFragment extends Fragment {

    private Spinner tagSpinner;       //11.10添加

    private static final int PICK_IMAGE_REQUEST = 1; // 常量用於圖片選擇請求
    private TextView detailDate; // 用於顯示選擇的日期
    private Button selectDateBtn; // 用於選擇日期的按鈕
    private ImageView detailImage; // 用於顯示選擇的圖片
    private Button selectImageBtn; // 用於選擇圖片的按鈕
    private Uri imageUri; // 用於存儲圖片的 URI
    private Spinner categorySpinner; // 新增 Spinner 元件
//    private List<ActivityItem> ITEMS = new ArrayList<ActivityItem>();
    private String username;
    private String userId;
    private int tagArrayId;
    private EditText activityName, activityAddress, activityDescription, participantCount;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        // 獲取傳遞過來的 username
        if (getArguments() != null) {
            username = getArguments().getString("username");
            userId = getArguments().getString("userId");
            Log.d("Debug", "CallFragment username:"+ username );

        }

        // 找到按钮
        Button create = view.findViewById(R.id.create);
        Button call_back = view.findViewById(R.id.call_back);
        selectDateBtn = view.findViewById(R.id.selectDateBtn); // 找到選擇日期的按鈕
        detailDate = view.findViewById(R.id.detailDate); // 找到顯示日期的 TextView
        detailImage = view.findViewById(R.id.detailImage); // 找到顯示圖片的 ImageView
        selectImageBtn = view.findViewById(R.id.selectImageBtn); // 找到選擇圖片的按鈕
        activityName = view.findViewById(R.id.activityName);
        activityAddress = view.findViewById(R.id.detailIngredients);
        activityDescription = view.findViewById(R.id.detailDesc);
        participantCount = view.findViewById(R.id.participantCount);

        // 初始化 Spinner
        categorySpinner = view.findViewById(R.id.categorySpinner);
        tagSpinner = view.findViewById(R.id.tagSpinner);

        // 使用 ArrayAdapter 將分類選項設置給 Spinner
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                getActivity(),
//                R.array.categories,
//                android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
//        Log.d("Debug", "CallFragment: categorySpinner ");

        // 根據所選類別更新 tagSpinner 選項
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Leisure
                        tagArrayId = R.array.leisure_tags;
                        break;
                    case 1: // Sports
                        tagArrayId = R.array.sports_tags;
                        break;
                    case 2: // Car Meet
                        tagArrayId = R.array.car_meet_tags;
                        break;
                    case 3: // Travel
                        tagArrayId = R.array.travel_tags;
                        break;
                    case 4: // Food
                        tagArrayId = R.array.food_tags;
                        break;
                    case 5: // Pets
                        tagArrayId = R.array.pet_tags;
                        break;
                    case 6: // Learning
                        tagArrayId = R.array.learning_tags;
                        break;
                    default:
                        tagArrayId = R.array.travel_tags;
                        break;
                }

                ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(
                        getActivity(), tagArrayId, android.R.layout.simple_spinner_item);
                tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tagSpinner.setAdapter(tagAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // Set up button actions
        create.setOnClickListener(v -> {
            String nameString = activityName.getText().toString();;
            String addressString = activityAddress.getText().toString();
            String descriptionString = activityDescription.getText().toString();
            String perticipantCountString = participantCount.getText().toString();
            int activityBed =5;
            if (!perticipantCountString.isEmpty()) {
                activityBed = Integer.parseInt(perticipantCountString);
            }
            String selectedDate = detailDate.getText().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String selectedTag = tagSpinner.getSelectedItem().toString();
            String imageUriString = ""; // 將 Uri 轉換為 String
            if (imageUri == null) {
                imageUriString = "https://firebasestorage.googleapis.com/v0/b/nclub-a408e.appspot.com/o/Taipei101.jpg?alt=media&token=eb1f8169-c982-44e0-a7ca-1ccefef6c733"; // 將 Uri 轉換為 String
            } else {
                imageUriString = imageUri.toString(); // 將 Uri 轉換為 String
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser item = auth.getCurrentUser();

            if (item != null) {
                String itemUid = FirebaseDatabase.getInstance().getReference("items").push().getKey();
                String itemId = "itemId_" +itemUid;
                Log.d("Debug", "CallFragment itemId: " + itemId);
                String chatroomId = "chatroomId_" + itemUid; // 使用 Firebase UID 作為chatroom ID
                Log.d("Debug", "CallFragment chatroomId_: " + chatroomId);

                Log.d("Debug", "setOnClickListener selectedDate:" + selectedDate);
                Log.d("Debug", "setOnClickListener selectedCategory:" + selectedCategory);
                Log.d("Debug", "setOnClickListener selectedTag:" + selectedTag);
                Log.d("Debug", "setOnClickListener imageUriString:" + imageUriString);

//                ActivityItem newItem = new ActivityItem(selectedDate, selectedCategory, selectedTag, imageUriString, itemId);
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("items");
                // 定義活動項目
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("address", addressString);
                itemData.put("bed", activityBed);
                itemData.put("dateTour", selectedDate);
                itemData.put("description", descriptionString);
                itemData.put("duration", selectedDate);
                itemData.put("pic",imageUriString);
                itemData.put("distance", "");
                itemData.put("price", 1000.0);
                itemData.put("score", 5);
                itemData.put("timeTour", "12:00 PM");
                itemData.put("title", nameString);
                itemData.put("category", selectedCategory);
                itemData.put("tag", selectedTag);
                itemData.put("tagArrayId", tagArrayId);
                itemData.put("ownUser", userId); // 設置擁有者為當前用戶


                // 定義聊天房間
                Map<String, Object> chatroomData = new HashMap<>();
                chatroomData.put("members", new HashMap<String, Boolean>() {{
                    put(userId, true); // 添加當前用戶
                }});
                // 定義消息
                Map<String, Object> messages = new HashMap<>();
                messages.put("message_id_1", new HashMap<String, Object>() {{
                    put("sender", userId);
                    put("text", descriptionString);
                    put("timestamp", System.currentTimeMillis() / 1000); // 當前時間戳
                }});
                chatroomData.put("messages", messages);
                chatroomData.put("tourItemId", itemId);

//                reference.child(itemId).setValue(newItem)
//                        .addOnSuccessListener(documentReference -> Toast.makeText(getActivity(), "活動已創建", Toast.LENGTH_SHORT).show())
//                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "創建活動失敗", Toast.LENGTH_SHORT).show());
//                Toast.makeText(getActivity(), "活動已創建", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(getActivity(), "創建失敗: ", Toast.LENGTH_SHORT).show();
//            }
                // 寫入數據到 Firebase
                DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items").child(itemId);
                DatabaseReference chatroomsRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId);

                // 寫入活動項目
                itemsRef.setValue(itemData)
                        .addOnSuccessListener(aVoid -> {
                            // 寫入聊天房間
                            chatroomsRef.setValue(chatroomData)
                                    .addOnSuccessListener(aVoid1 -> Toast.makeText(getActivity(), "活動和聊天房間已創建", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "創建聊天房間失敗", Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "創建活動失敗", Toast.LENGTH_SHORT).show());
            }
        });

        call_back.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "返回主頁面", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        // Set up date selection
        selectDateBtn.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay);
                        detailDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Set up image selection
        selectImageBtn.setOnClickListener(view12 -> openFileChooser());

        return view;
    }

//        // 设置点击监听器
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 显示 Toast 消息
//                Toast.makeText(getActivity(), "活動已創建", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        call_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "返回主頁面", Toast.LENGTH_SHORT).show();
//
//                // 使用 FragmentTransaction 切换到 HomeFragment
//                FragmentManager fragmentManager = getParentFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                // 替换当前的 CallFragment 为 HomeFragment
//                fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
//
//                // 可选：将该事务添加到返回栈中，以便用户可以按返回键返回 CallFragment
//                fragmentTransaction.addToBackStack(null);
//
//                // 提交事务
//                fragmentTransaction.commit();
//            }
//        });
//
//        // 設置選擇日期的按鈕點擊事件
//        selectDateBtn.setOnClickListener(view1 -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
//                    (datePicker, selectedYear, selectedMonth, selectedDay) -> {
//                        // 更新 TextView 顯示選擇的日期
//                        String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
//                        detailDate.setText(date);
//                    }, year, month, day);
//            datePickerDialog.show();
//        });
//
//        // 設置選擇圖片的按鈕點擊事件
//        selectImageBtn.setOnClickListener(view12 -> openFileChooser());
//
//        return view;
//    }

    // 打開文件選擇器以選擇圖片
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "選擇圖片"), PICK_IMAGE_REQUEST);
    }

    // 處理圖片選擇的結果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                detailImage.setImageBitmap(bitmap); // 將選擇的圖片顯示在 ImageView 中
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "選擇圖片失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
