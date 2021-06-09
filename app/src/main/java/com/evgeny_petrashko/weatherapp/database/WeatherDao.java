package com.evgeny_petrashko.weatherapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM WEATHERENTITY")
    List<WeatherEntity> getAll();

    @Insert
    void insert(WeatherEntity weatherEntity);

    @Delete
    void delete(WeatherEntity weatherEntity);

    @Query("DELETE FROM WEATHERENTITY")
    void deleteAll();

    @Query("SELECT MAX(id) FROM WEATHERENTITY")
    int getLastId();
}
