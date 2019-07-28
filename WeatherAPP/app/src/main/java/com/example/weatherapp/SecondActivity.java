package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.weatherapp.interfaces.CorWeather;
import com.example.weatherapp.interfaces.OpenWeather;
import com.example.weatherapp.model.Coord;
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


public class SecondActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String KEY = "53b74ccc7e98175a23f392a084a3edd3";
    private static final String URL = "http://api.openweathermap.org/";
    private static final int PERMISSION_REQUEST_CODE = 10;
    private LocationManager locationManager;
    private String provider;
    private OpenWeather openWeather;
    private CorWeather corWeather;
    private TextView tvСity;
    private TextView tvDates;
    private TextView tvTemp;
    private TextView tvHumidity;
    private TextView tvCor;
    private Button btnCor;
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
        btnCor.setOnClickListener(this);

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
        if (v.getId() == R.id.cor_button) {
            permission();

        }

    }

private void permission() {
    // Проверим на пермиссии, и если их нет, запросим у пользователя
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
// Запросим координаты
        requestLocation();
    } else {
// Пермиссии нет, будем запрашивать у пользователя
        requestLocationPermissions();
    }
}

    private void requestLocation() {
// Если пермиссии все-таки нет - просто выйдем, приложение не имеет смысла
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {

// Будем получать геоположение через каждые 10 секунд или каждые 10 метров
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String latitude = Double.toString(location.getLatitude());// Широта
                    String longitude = Double.toString(location.getLongitude());// Долгота
                    requestRetrofit(latitude,longitude,KEY);
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                @Override
                public void onProviderEnabled(String provider) {
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    // Запрос пермиссии для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
// Запросим эти две пермиссии у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }


    // Это результат запроса у пользователя пермиссии
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
// Это та самая пермиссия, что мы запрашивали?
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Пермиссия дана
                requestLocation();
            }
        }
    }


    private void initViews() {
        tvСity = findViewById(R.id.text_city);
        tvDates = findViewById(R.id.text_data);
        btnNext = findViewById(R.id.next_button);
        tvTemp = findViewById(R.id.text_temp);
        tvHumidity = findViewById(R.id.text_humidity);
        tvCor = findViewById(R.id.text_cor);
        btnCor= findViewById(R.id.cor_button);
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
        corWeather = retrofit.create(CorWeather.class);
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
    private void requestRetrofit(String lon, String lat, String keyApi) {

        corWeather.loadWeather(lon,lat, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {


                        if (response.body() != null) {
                            tvCor.setText(Float.toString(response.body().getMain().getTemp()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call,
                                          @NonNull Throwable throwable) {

                        tvCor.setText(getResources().getString(R.string.error));
                    }
                });
    }
}

