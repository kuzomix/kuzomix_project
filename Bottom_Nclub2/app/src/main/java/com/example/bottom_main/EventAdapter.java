package com.example.bottom_main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // 請確保你已添加 Glide 的依賴

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> recommendedEvents;

    public EventAdapter(List<Event> recommendedEvents) {
        this.recommendedEvents = recommendedEvents;
    }

    // 定義 ViewHolder
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventTitle = itemView.findViewById(R.id.eventTitle);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = recommendedEvents.get(position);
        holder.eventTitle.setText(event.getTitle());

        // 加載圖片
        Glide.with(holder.eventImage.getContext())
                .load(event.getImageUrl())
                .into(holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return recommendedEvents.size();
    }
}
