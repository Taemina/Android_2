package com.example.weatherapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherapp.R;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder> {
    private String[] data;


    public WeatherHistoryAdapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public WeatherHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder((TextView) v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(TextView view) {
            super(view);
            textView = view;

        }
    }
}
