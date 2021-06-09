package com.evgeny_petrashko.weatherapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WeatherEntity {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "weather")
    public String weather_report;

    @ColumnInfo(name = "service_temperature")
    public int service_temperature;

    @ColumnInfo(name = "user_temperature")
    public int user_temperature;

    @ColumnInfo(name = "time", defaultValue = "0")
    public long time;
}
