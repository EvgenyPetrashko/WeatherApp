package com.evgeny_petrashko.weatherapp;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.evgeny_petrashko.weatherapp.database.WeatherDatabase;

public class App extends Application {
    public static AppComponent appComponent;
    public static WeatherDatabase weatherDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        RoomInitialization();
    }

    // Dagger Initialization
    private void initDagger() {
        appComponent = DaggerAppComponent.builder().build();
    }

    // Room DataBase Initialization
    private void RoomInitialization(){
        weatherDatabase = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "weather_db").addMigrations(WeatherDatabase.MIGRATION_1_2).allowMainThreadQueries().build();
    }
}
