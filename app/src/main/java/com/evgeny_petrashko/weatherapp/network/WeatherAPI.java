package com.evgeny_petrashko.weatherapp.network;

import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherCity;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherCoordinates;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherSeveralDays;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherSeveralDaysCoordinates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<JsonWeatherCity> getCurrentWeather(@Query ("q") String city,
                                            @Query("appid") String API_KEY);

    @GET("weather")
    Call<JsonWeatherCoordinates> getCurrentWeatherByCoordinates(@Query("lat") double latitude,
                                                                @Query("lon") double longitude,
                                                                @Query("appid") String API_KEY);

    @GET("forecast")
    Call<JsonWeatherSeveralDays> getSeveralDaysWeatherInfo(@Query("q") String city,
                                                           @Query("appid") String API_KEY);

    @GET("forecast")
    Call<JsonWeatherSeveralDaysCoordinates> getSeveralDaysWeatherInfo(@Query("lat") double latitude,
                                                                      @Query("lon") double longitude,
                                                                      @Query("appid") String API_KEY);
}
