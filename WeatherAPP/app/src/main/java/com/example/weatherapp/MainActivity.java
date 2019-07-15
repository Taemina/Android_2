package com.example.weatherapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.example.weatherapp.fragments.WeatherFragment.CITY;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NAME_CITY = "NAME_CITY";
    public static final String WIND_CITY = "WIND_CITY";
    public static final String PRESSURE_CITY = "PRESSURE_CITY";
    private EditText etEnteredCity;
    private CheckBox cbWind;
    private CheckBox cbPressure;
    private Button btnChoiceCity;
    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        btnChoiceCity.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.city_button) {
            Intent nextActivity = new Intent(this, SecondActivity.class);
            String cityName = String.valueOf(etEnteredCity.getText());

            if (cityName.compareToIgnoreCase("city") == 0 ||
                    cityName.compareToIgnoreCase("город") == 0) {
                nextActivity.putExtra(NAME_CITY, String.valueOf(CITY));
            } else {
                nextActivity.putExtra(NAME_CITY, cityName);
            }
            boolean choiceWind = false;
            if (cbWind.isChecked()) {
                choiceWind = true;
            }
            nextActivity.putExtra(WIND_CITY, choiceWind);

            boolean choicePressure = false;
            if (cbPressure.isChecked()) {
                choicePressure = true;
            }
            nextActivity.putExtra(PRESSURE_CITY, choicePressure);
            // старт службы
            startService(new Intent(MainActivity.this, WeatherInfoService.class));
            startActivity(nextActivity);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME_CITY, String.valueOf(etEnteredCity.getText()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cityName = savedInstanceState.getString(NAME_CITY);
        etEnteredCity.setText(String.valueOf(cityName));
    }

    private void initViews() {
        btnChoiceCity = findViewById(R.id.city_button);
        etEnteredCity = findViewById(R.id.city_edit);
        cbWind = findViewById(R.id.wind);
        cbPressure = findViewById(R.id.pressure);
    }
}
