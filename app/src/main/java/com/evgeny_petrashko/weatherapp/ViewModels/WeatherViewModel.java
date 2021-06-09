package com.evgeny_petrashko.weatherapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;

import com.evgeny_petrashko.weatherapp.database.PersistentStorage;
import com.evgeny_petrashko.weatherapp.database.WeatherEntity;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WeatherViewModel extends BaseViewModel {
    private Gson gson;

    public WeatherViewModel(@NonNull Application application){
        super(application);
        gson = new Gson();
    }

    public boolean temperatureValidation(String temperature){
        try {
            Integer.parseInt(temperature);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }


    // Adds data to the database and update the estimated data
    public void addDataToDatabase(int user_temperature){
        WeatherEntity entity = new WeatherEntity();
        entity.user_temperature = user_temperature;
        entity.service_temperature = getLastServiceTemperature();
        entity.time = Calendar.getInstance().getTimeInMillis();
        weatherDao.insert(entity);

        estimateDataSet();
    }

    // Get last saved service temperature
    public int getLastServiceTemperature(){
        return storage.getInteger(PersistentStorage.last_service_temperature, 0);
    }

    // Get time of last saved service temperature
    public long getLastServiceCallDayTime(){
        return storage.getLong(PersistentStorage.last_update, 0L);
    }

    // Get time of last saved service week info
    public long getLastServiceCallWeekTime(){
        return storage.getLong(PersistentStorage.last_week_update, 0L);
    }

    // Save last service day info
    public void SaveLastServiceDayInfo(WeatherParams params){
        storage.addInteger(PersistentStorage.last_service_temperature, params.temp);
        storage.addLong(PersistentStorage.last_update, Calendar.getInstance().getTimeInMillis());
        storage.addString(PersistentStorage.last_weather_info_string, gson.toJson(params));
    }

    // Save last service week info
    public void SaveLastServiceWeekInfo(List<WeatherParams> weatherParams){
        storage.addLong(PersistentStorage.last_week_update, Calendar.getInstance().getTimeInMillis());
        storage.addString(PersistentStorage.last_week_weather_info_string, gson.toJson(weatherParams));
    }

    // Get last estimated data (last information about data size, mean error and etc.)
    public HashMap<String, Object> getEstimatedData(){
        HashMap<String, Object> map = new HashMap<>();

        map.put(PersistentStorage.total_data_string,
                storage.getInteger(PersistentStorage.total_data_string, 0));
        map.put(PersistentStorage.mean_error_string,
                storage.getFloat(PersistentStorage.mean_error_string, 0f));
        map.put(PersistentStorage.average_percentage_string,
                storage.getFloat(PersistentStorage.average_percentage_string, 0f));
        return map;
    }

    // Delete the whole dataSet
    public void deleteEstimatedData(){
        weatherDao.deleteAll();

        storage.remove(PersistentStorage.total_data_string, PersistentStorage.mean_error_string,
                PersistentStorage.average_percentage_string);
    }

    // Cache function which returns last saved day information in order to don't interrupt the server many time
    public void postLastWeatherDayInfo(){
        WeatherParams params;
        try {
            params = gson.fromJson(storage.getString(PersistentStorage.last_weather_info_string, "error"), WeatherParams.class);
        }catch (JsonSyntaxException e){
            params = new WeatherParams();
        }
        getActualInfo().setValue(params);
    }

    // Cache function which returns last saved week information in order to don't interrupt the server many time
    public void postLastWeatherWeekInfo(){
        List<WeatherParams> paramsList;
        try {
            Type type = new TypeToken< List < WeatherParams >>() {}.getType();
            paramsList = gson.fromJson(storage.getString(PersistentStorage.last_week_weather_info_string, "error"), type);
        }catch (JsonSyntaxException e){
            paramsList = new ArrayList<>();
        }
        getSeveralDaysInfo().setValue(paramsList);
    }

}
