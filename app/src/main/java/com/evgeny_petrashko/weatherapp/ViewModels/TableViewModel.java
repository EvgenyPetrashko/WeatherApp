package com.evgeny_petrashko.weatherapp.ViewModels;
import android.app.Application;

import androidx.annotation.NonNull;

import com.evgeny_petrashko.weatherapp.database.WeatherEntity;

import java.util.List;

public class TableViewModel extends BaseViewModel {

    public TableViewModel(@NonNull Application application) {
        super(application);
    }

    // Returns the whole dataSet
    public List<WeatherEntity> getWholeDataSet(){
        return weatherDao.getAll();
    }

    // Delete concrete entry from the database
    public void deleteEntry(WeatherEntity weatherEntity){
        weatherDao.delete(weatherEntity);
    }
}
