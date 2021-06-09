package com.evgeny_petrashko.weatherapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import java.util.ArrayList;
import java.util.HashMap;


public class ConcreteDayViewModel extends BaseViewModel {
    public static String average_temperature_string = "average_temp";
    public static String average_wind_speed_string = "average_wind";
    public static String average_humidity_string = "average_humidity";

    public ConcreteDayViewModel(@NonNull Application application) {
        super(application);
    }

    // Calculating average data for concrete day
    public HashMap<String, Float> getAverageData(ArrayList<WeatherParams> params){
        HashMap<String, Float> map = new HashMap<>();
        float average_temp = 0f;
        float average_wind = 0f;
        float average_humidity = 0f;

        for (WeatherParams param : params) {
            average_temp += param.temp;
            average_wind += param.wind;
            average_humidity += param.humidity;
        }

        average_temp /= params.size();
        average_wind /= params.size();
        average_humidity /= params.size();

        map.put(average_temperature_string, average_temp);
        map.put(average_wind_speed_string, average_wind);
        map.put(average_humidity_string, average_humidity);
        return map;
    }

}
