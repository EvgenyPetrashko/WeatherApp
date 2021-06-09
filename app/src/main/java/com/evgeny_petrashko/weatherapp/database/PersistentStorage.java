package com.evgeny_petrashko.weatherapp.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistentStorage {
    // Storage for working with SharedPreferences
    public static final String STORAGE_NAME = "StorageName";
    public static final String last_update = "time";
    public static final String last_week_update = "last_week_time";
    public static final String last_weather_info_string = "weather_last_info";
    public static final String last_week_weather_info_string = "weather_week_list";

    public static String total_data_string = "data_size";
    public static String mean_error_string = "mean_error";
    public static String average_percentage_string = "average_percentage";

    public static final String last_service_temperature = "last_service_temp";

    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;
    private Context context = null;

    @Provides
    public PersistentStorage getInstance(){
        return this;
    }

    public void provideContext(Context context){
        settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public void addString(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def_value){
        return settings.getString(key, def_value);
    }

    public void addInteger(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInteger(String key, int def_value){
        return settings.getInt(key, def_value);
    }

    public void addLong(String key, long value){
        editor.putLong(key, value);
        editor.apply();
    }

    public Long getLong(String key, Long def_value){
        return settings.getLong(key, def_value);
    }

    public void addFloat(String key, float value){
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float def_value){
        return settings.getFloat(key, def_value);
    }

    public void remove(String... keys){
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
        HashSet<String> set = null;
    }

    public void putDouble(String key,double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }

    double getDouble(String key, double def_value) {
        return Double.longBitsToDouble(settings.getLong(key, Double.doubleToLongBits(def_value)));
    }
}
