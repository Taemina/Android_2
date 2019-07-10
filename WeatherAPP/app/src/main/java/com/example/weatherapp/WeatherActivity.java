package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.weatherapp.fragments.WeatherFragment;
import com.example.weatherapp.model.CityIndex;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.example.weatherapp.fragments.WeatherFragment.PARCEL;

public class WeatherActivity extends AppCompatActivity {

    public static Intent start(@NonNull Context context, @NonNull CityIndex parcel) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(PARCEL, parcel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            WeatherFragment weatherFragment = new WeatherFragment();
            weatherFragment.setArguments(getIntent().getExtras());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.placeholder_weather, weatherFragment)
                    .commit();
        }
    }

}
