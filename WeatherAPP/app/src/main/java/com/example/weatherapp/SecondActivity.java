package com.example.weatherapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.weatherapp.interfaces.OpenWeather;
import com.example.weatherapp.model.WeatherRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weatherapp.MainActivity.NAME_CITY;
import static com.example.weatherapp.MainActivity.PRESSURE_CITY;
import static com.example.weatherapp.MainActivity.WIND_CITY;


public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY = "53b74ccc7e98175a23f392a084a3edd3";
    private static final String URL = "http://api.openweathermap.org/";
    private OpenWeather openWeather;
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
    private NoteDataSource notesDataSource;     // Источник данных
    private NoteDataReader noteDataReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initRetrofit();
        initViews();
        btnNext.setOnClickListener(this);


        if (getIntent() != null && getIntent().getExtras() != null) {
            cityName = getIntent().getStringExtra(NAME_CITY);
            cityWind = getIntent().getBooleanExtra(WIND_CITY, true);
            cityPressure = getIntent().getBooleanExtra(PRESSURE_CITY, true);
            requestRetrofit(cityName, KEY);
            tvСity.setText(String.valueOf(cityName));
        }
        format1 = new SimpleDateFormat("dd.MM.yyyy");
        String newDate = String.valueOf(format1.format(date));
        tvDates.setText(newDate);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        initDataSource();


    }
    private void initDataSource() {
        notesDataSource = new NoteDataSource(getApplicationContext());
        notesDataSource.open();
        noteDataReader = notesDataSource.getNoteDataReader();
    }
    @Override
    protected void onResume() {
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

    private void showTemperatureSensors(SensorEvent event) {
        // tvTemp.setText(String.valueOf(event.values[0]));
    }

    private void showHumiditySensors(SensorEvent event) {
        String text = String.valueOf(getResources().getText(R.string.humidity));
        text = text + " " + event.values[0];
        //  tvHumidity.setText(text);
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
        tvTemp = findViewById(R.id.text_temp);
        tvHumidity = findViewById(R.id.text_humidity);
    }

    private void addViewGroup(String info) {
        ViewGroup elements2 = findViewById(R.id.layout);
        TextView textViewManual = new TextView(this);
        int colorAccent = ContextCompat.getColor(this, R.color.colorText);
        textViewManual.setTextColor(colorAccent);
        textViewManual.setText(info);
        textViewManual.setTextSize(30);
        textViewManual.setGravity(Gravity.CENTER_HORIZONTAL);
        elements2.addView(textViewManual);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // Базовая часть адреса
                // Конвертер, необходимый для преобразования JSON'а в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Создаем объект, при помощи которого будем выполнять запросы
        openWeather = retrofit.create(OpenWeather.class);
    }

    private void requestRetrofit(String city, String keyApi) {
        openWeather.loadWeather(city, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        String info;

                        if (response.body() != null) {
                            tvTemp.setText(Float.toString(response.body().getMain().getTemp()));
                            if (cityWind == true) {
                                info = getResources().getString(R.string.wind) + " " + Float.toString(response.body().getWind().getSpeed()) + " " + getResources().getString(R.string.wind_end);
                                addViewGroup(info);
                            }
                            if (cityPressure == true) {
                                info = getResources().getString(R.string.pressure) + " " + Float.toString(response.body().getMain().getPressure()) + " " + getResources().getString(R.string.pressure_end);
                                addViewGroup(info);
                            }
                            notesDataSource.addNote(tvСity.getText().toString(),
                                    tvTemp.getText().toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call,
                                          @NonNull Throwable throwable) {

                        tvTemp.setText(getResources().getString(R.string.error));
                    }
                });
    }

}

