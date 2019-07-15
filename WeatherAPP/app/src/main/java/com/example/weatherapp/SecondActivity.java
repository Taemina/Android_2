package com.example.weatherapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.weatherapp.MainActivity.NAME_CITY;
import static com.example.weatherapp.MainActivity.PRESSURE_CITY;
import static com.example.weatherapp.MainActivity.WIND_CITY;


public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvСity;
    private TextView tvDates;
    private TextView tvTemp;
    private TextView tvHumidity;
    private Button btnNext;
    private String cityName;
    private boolean cityWind;
    private boolean cityPressure;
    private Date date = new Date();
    private SimpleDateFormat format1;
    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
        btnNext.setOnClickListener(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            cityName = getIntent().getStringExtra(NAME_CITY);
            cityWind = getIntent().getBooleanExtra(WIND_CITY, true);
            cityPressure = getIntent().getBooleanExtra(PRESSURE_CITY, true);
            tvСity.setText(String.valueOf(cityName));
        }
        format1 = new SimpleDateFormat("dd.MM.yyyy");
        String newDate = String.valueOf(format1.format(date));
        tvDates.setText(newDate);

        if (cityWind == true) {
            ViewGroup elements2 = findViewById(R.id.layout);
            TextView textViewManual = new TextView(this);
            int colorAccent = ContextCompat.getColor(this, R.color.colorText);
            textViewManual.setTextColor(colorAccent);
            textViewManual.setText("Ветер 5 м/с");
            textViewManual.setTextSize(30);
            textViewManual.setGravity(Gravity.CENTER_HORIZONTAL);
            elements2.addView(textViewManual);

        }
        if (cityPressure == true) {
            ViewGroup elements2 = findViewById(R.id.layout);
            TextView textViewManual = new TextView(this);
            int colorAccent = ContextCompat.getColor(this, R.color.colorText);
            textViewManual.setTextColor(colorAccent);
            textViewManual.setText("Давление 750 мм рт.ст.");
            textViewManual.setTextSize(30);
            textViewManual.setGravity(Gravity.CENTER_HORIZONTAL);
            elements2.addView(textViewManual);

        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(listenerTemperature, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemperature, sensorTemperature);
        sensorManager.unregisterListener(listenerHumidity, sensorHumidity);
    }
    private void showTemperatureSensors(SensorEvent event){
        tvTemp.setText(String.valueOf(event.values[0]));
    }
    private void showHumiditySensors(SensorEvent event){
        String text =String.valueOf(getResources().getText(R.string.humidity));
        text=text+" "+event.values[0];
        tvHumidity.setText(text);
    }
    SensorEventListener listenerTemperature = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showTemperatureSensors(event);
        }
    };
    SensorEventListener listenerHumidity = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showHumiditySensors(event);
        }
    };
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_button) {
            Intent nextActivity = new Intent(this, WeatherHistoryActivity.class);
            startActivity(nextActivity);
        }
    }

    private void initViews() {
        tvСity = findViewById(R.id.text_city);
        tvDates = findViewById(R.id.text_data);
        btnNext = findViewById(R.id.next_button);
        tvTemp=findViewById(R.id.text_temp);
        tvHumidity=findViewById(R.id.text_humidity);
    }
}
