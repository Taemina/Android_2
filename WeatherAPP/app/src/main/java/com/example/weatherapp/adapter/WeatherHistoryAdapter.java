package com.example.weatherapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherapp.Note;
import com.example.weatherapp.NoteDataReader;
import com.example.weatherapp.R;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder> {
    private String[] data;


    private final NoteDataReader noteDataReader;


    public WeatherHistoryAdapter(NoteDataReader noteDataReader) {
        this.noteDataReader = noteDataReader;
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
        holder.bind(noteDataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return noteDataReader.getCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textNote;
        private Note note;

        public ViewHolder(View itemView) {
            super(itemView);
            textNote = itemView.findViewById(R.id.textView);


        }
        public void bind(Note note) {
            this.note = note;
            String str=note.getNameCity()+": "+note.getWeather();
            textNote.setText(str);
        }
    }
}
