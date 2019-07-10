package com.example.weatherapp.model;

import java.io.Serializable;

public class CityIndex implements Serializable {
    private int index;
    private String cityName;


    public CityIndex(int imageIndex, String cityName) {
        this.index = imageIndex;
        this.cityName = cityName;

    }

    public int getIndex() {
        return index;
    }

    public String getCityName() {
        return cityName;
    }

}