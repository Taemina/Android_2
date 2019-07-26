package com.example.weatherapp;

public class Note {
    private long id;
    private String weather;
    private String nameCity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWeather() {
        return  weather;
    }

    public void setWeather(String note) {
        this.weather = note;
    }

    public String getNameCity() {
        return  nameCity;
    }

    public void setNameCity(String title) {
        this.nameCity = title;
    }
}
