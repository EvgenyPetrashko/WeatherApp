package com.evgeny_petrashko.weatherapp.network;

import android.app.Application;

import com.evgeny_petrashko.weatherapp.ViewModels.BaseViewModel;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherCity;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherCoordinates;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherSeveralDays;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherSeveralDaysCoordinates;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.List2;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.ListCoordinates;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkService {
    private String API_KEY = "2a3ef1ce7aa8026db782522f1b604aee";
    private String base_url = "https://api.openweathermap.org/data/2.5/";
    public String time_pattern = "yyyy-MM-dd HH:mm:ss";
    private WeatherAPI service;
    private String city;
    private Double latitude = null;
    private Double longitude = null;

    public long time_when_update_is_needed = 60 * 1000;

    BaseViewModel model;

    public NetworkService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        city = "Kazan";
        service = retrofit.create(WeatherAPI.class);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    // Set geographical coordinates visible for network service
    public void setGeographicalCoordinates(double latitude, double longitude){
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
    }


    @Provides
    NetworkService getNetworkService(){
        return this;
    }

    // Provide LiveDataModel from activity
    public void ProvideLiveDataModel(BaseViewModel liveDataModel){
        this.model = liveDataModel;
    }

    // Put actual info about actual weather related to specified city or coordinates, to liveDataObject
    public void getCurrentInfo(){
        if (latitude != null && longitude != null){
            getInfoByCoordinates();
        }else {
            getInfoByCity();
        }
    }

    // Put actual info about week weather related to specified city or coordinates, to liveDataObject

    public void getWeekInfo(){
        if (latitude != null && longitude != null){
            getWeekInfoByCoordinates();
        }else {
            getWeekInfoByCity();
        }

    }

    private void getInfoByCity(){
        Call<JsonWeatherCity> call = service.getCurrentWeather(city, API_KEY);
        call.enqueue(new Callback<JsonWeatherCity>() {
            @Override
            public void onResponse(Call<JsonWeatherCity> call, Response<JsonWeatherCity> response) {
                if (response.isSuccessful()){
                    JsonWeatherCity json = response.body();
                    WeatherParams params = new WeatherParams();
                    params.temp = KelvinToCelsius(json.main.temp);
                    params.weather = json.weather.get(0).main;
                    params.wind = json.wind.speed;
                    params.humidity = json.main.humidity;
                    model.getActualInfo().setValue(params);
                }

            }

            @Override
            public void onFailure(Call<JsonWeatherCity> call, Throwable t) {
                model.getError().setValue(t.getMessage());
            }
        });
    }

    private void getInfoByCoordinates(){
        Call<JsonWeatherCoordinates> call = service.getCurrentWeatherByCoordinates(latitude, longitude, API_KEY);
        call.enqueue(new Callback<JsonWeatherCoordinates>() {
            @Override
            public void onResponse(Call<JsonWeatherCoordinates> call, Response<JsonWeatherCoordinates> response) {
                if (response.isSuccessful()){
                    JsonWeatherCoordinates json = response.body();
                    WeatherParams params = new WeatherParams();
                    params.temp = KelvinToCelsius(json.main.temp);
                    params.weather = json.weather.get(0).main;
                    params.wind = json.wind.speed;
                    params.humidity = json.main.humidity;
                    model.getActualInfo().setValue(params);
                }

            }

            @Override
            public void onFailure(Call<JsonWeatherCoordinates> call, Throwable t) {
                model.getError().setValue(t.getMessage());
            }
        });
    }

    public void getWeekInfoByCity(){
        Call<JsonWeatherSeveralDays> call = service.getSeveralDaysWeatherInfo(city, API_KEY);

        call.enqueue(new Callback<JsonWeatherSeveralDays>() {
            @Override
            public void onResponse(Call<JsonWeatherSeveralDays> call, Response<JsonWeatherSeveralDays> response) {
                if (response.isSuccessful()){
                    JsonWeatherSeveralDays json = response.body();
                    List<List2> weather_list = json.list;
                    List<WeatherParams> list = new ArrayList<>();
                    for (List2 list2 : weather_list) {
                        WeatherParams params = new WeatherParams();
                        params.time = list2.dtTxt;
                        params.temp = KelvinToCelsius(list2.main.temp);
                        params.weather = list2.weather.get(0).main;
                        params.wind = list2.wind.speed;
                        params.humidity = list2.main.humidity;
                        list.add(params);
                    }

                    model.getSeveralDaysInfo().setValue(list);
                }
            }
            @Override
            public void onFailure(Call<JsonWeatherSeveralDays> call, Throwable t) {
                model.getError().setValue(t.getMessage());

            }
        });
    }

    public void getWeekInfoByCoordinates(){
        Call<JsonWeatherSeveralDaysCoordinates> call = service.getSeveralDaysWeatherInfo(latitude, longitude, API_KEY);

        call.enqueue(new Callback<JsonWeatherSeveralDaysCoordinates>() {
            @Override
            public void onResponse(Call<JsonWeatherSeveralDaysCoordinates> call, Response<JsonWeatherSeveralDaysCoordinates> response) {
                if (response.isSuccessful()){
                    JsonWeatherSeveralDaysCoordinates json = response.body();
                    List<ListCoordinates> weather_list = json.list;
                    List<WeatherParams> list = new ArrayList<>();
                    for (ListCoordinates listCoordinates : weather_list) {
                        WeatherParams params = new WeatherParams();
                        params.time = listCoordinates.dtTxt;
                        params.temp = KelvinToCelsius(listCoordinates.main.temp);
                        params.weather = listCoordinates.weather.get(0).main;
                        params.wind = listCoordinates.wind.speed;
                        params.humidity = listCoordinates.main.humidity;
                        list.add(params);
                    }

                    model.getSeveralDaysInfo().setValue(list);
                }
            }
            @Override
            public void onFailure(Call<JsonWeatherSeveralDaysCoordinates> call, Throwable t) {
                model.getError().setValue(t.getMessage());
            }
        });
    }

    // Kelvin to celsius conversion
    private int KelvinToCelsius(double kelvin){
        return (int) Math.round(kelvin - 273.15d);
    }
}
