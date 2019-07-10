package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.WeatherActivity;
import com.example.weatherapp.model.CityIndex;

public class CitiesListFragment extends ListFragment {
    private final String CURRENT_CITY_INDEX = "CURRENT_CITY_INDEX";

    private boolean isDualPane;
    private CityIndex cityIndexParcel;
    private View detailsFrame;

    private int defaultIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activityContext = getActivity();
        if (activityContext == null) {
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                activityContext,
                R.layout.item, R.id.textView, getResources().getStringArray(R.array.cities_list)
        );

        setListAdapter(adapter);

        detailsFrame = activityContext.findViewById(R.id.placeholder_weather);
        isDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;


        if (savedInstanceState != null) {
            cityIndexParcel = (CityIndex) savedInstanceState.getSerializable(CURRENT_CITY_INDEX);
        } else {
            cityIndexParcel = new CityIndex(defaultIndex, getResources().getTextArray(R.array.cities_list)[defaultIndex].toString());
        }

        if (isDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showWeather(cityIndexParcel);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_CITY_INDEX, cityIndexParcel);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {


        TextView textView = v.findViewById(R.id.textView);
        cityIndexParcel = new CityIndex(position, textView.getText().toString());
        showWeather(cityIndexParcel);
    }

    private void showWeather(CityIndex parcel) {
        if (getActivity() == null) {
            return;
        }
        if (parcel == null) {
            return;
        }

        int index = parcel.getIndex();

        if (isDualPane) {
            getListView().setItemChecked(index, true);
            WeatherFragment weatherFragment = (WeatherFragment) getActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.placeholder_weather);

            if (weatherFragment == null || weatherFragment.getParcel().getIndex() != index) {
                weatherFragment = weatherFragment.createInstance(parcel);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.placeholder_weather, weatherFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        } else {
            startActivity(WeatherActivity.start(getContext(), parcel));
        }
    }
}
