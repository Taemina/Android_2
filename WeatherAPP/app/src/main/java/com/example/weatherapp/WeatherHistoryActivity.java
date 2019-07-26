package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.weatherapp.adapter.WeatherHistoryAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class WeatherHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] weatherList = {"21.05.2019: +23", "22.05.2019: -14", "23.05.2019: +12"};
    private EditText etEnteredWeather;
    private Button btnAddWeather;
    private Date date = new Date();
    private SimpleDateFormat format1;
    private NoteDataSource notesDataSource;     // Источник данных
    private NoteDataReader noteDataReader;      // Читатель данных
    private WeatherHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_history);
        initDataSource();
        initViews();
        manageRecyclerView();
        //btnAddWeather.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.add_button) {
            format1 = new SimpleDateFormat("dd.MM.yyyy");
            String a = format1.format(date) + ": " + etEnteredWeather.getText().toString();
            weatherList = AddElement(weatherList, a);
            manageRecyclerView();

        }
    }

    private void manageRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeatherHistoryAdapter(noteDataReader);
        recyclerView.setAdapter(adapter);

    }
    private void initDataSource() {
        notesDataSource = new NoteDataSource(getApplicationContext());
        notesDataSource.open();
        noteDataReader = notesDataSource.getNoteDataReader();
    }
    private void initViews() {
        btnAddWeather = findViewById(R.id.add_button);
        etEnteredWeather = findViewById(R.id.add_edit);

    }

    private String[] AddElement(String[] a, String e) {
        a = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
}
