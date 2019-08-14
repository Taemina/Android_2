package com.example.weatherapp.interfaces;

import com.example.weatherapp.model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface CorWeather {
    @GET("data/2.5/weather?")
    Call<WeatherRequest> loadWeather(@Query("lon") String lon,@Query("lat") String lat, @Query("appid") String keyApi);
}
