package com.evgeny_petrashko.weatherapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.evgeny_petrashko.weatherapp.App;
import com.evgeny_petrashko.weatherapp.LocationModule;
import com.evgeny_petrashko.weatherapp.database.PersistentStorage;
import com.evgeny_petrashko.weatherapp.database.WeatherDao;
import com.evgeny_petrashko.weatherapp.database.WeatherEntity;
import com.evgeny_petrashko.weatherapp.network.NetworkService;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class BaseViewModel extends AndroidViewModel {

    // LiveData object which is responsible for actual weather info translation
    private MutableLiveData<WeatherParams> actual_info = new MutableLiveData<>();

    // LiveData object which is responsible for week weather info translation
    private MutableLiveData<List<WeatherParams>> several_days_info = new MutableLiveData<>();

    // LiveData object which is responsible for error string translation
    private MutableLiveData<String> error_info = new MutableLiveData<>();

    // LiveData object which is responsible for actual user location translation
    private MutableLiveData<String> city_name = new MutableLiveData<>();

    public WeatherDao weatherDao;

    @Inject
    public PersistentStorage storage;

    @Inject
    public NetworkService service;

    @Inject
    public LocationModule locationModule;

    public BaseViewModel(@NonNull Application application){
        super(application);
        weatherDao = App.weatherDatabase.weatherDao();
        App.appComponent.inject(this);
    }


    // Geographical coordinates transfer to NetworkService
    public void saveGeographicalCoordinates(double latitude, double longitude){
        service.setGeographicalCoordinates(latitude, longitude);
        storage.addLong(LocationModule.latitude_string, Double.doubleToLongBits(latitude));
        storage.addLong(LocationModule.longitude_string, Double.doubleToLongBits(longitude));
        storage.addLong(LocationModule.last_coordinated_update, Calendar.getInstance().getTimeInMillis());
    }

    public MutableLiveData<WeatherParams> getActualInfo(){
        System.out.println("Strange");
        if (actual_info == null){
            actual_info = new MutableLiveData<>();
        }
        return actual_info;
    }

    public MutableLiveData<List<WeatherParams>> getSeveralDaysInfo(){
        if (several_days_info == null){
            several_days_info = new MutableLiveData<>();
        }
        return several_days_info;
    }

    public MutableLiveData<String> getError(){
        if (error_info == null){
            error_info = new MutableLiveData<>();
        }
        return error_info;
    }

    public MutableLiveData<String> getCity(){
        if (city_name == null){
            city_name = new MutableLiveData<>();
        }
        return city_name;
    }

    // Data set estimating: data size, mean error, mean percentage of correctness
    public void estimateDataSet(){
        List<WeatherEntity> list = weatherDao.getAll();
        float mean_error = 0f;
        float average_percentage = 0f;
        // mistake by 1 degree is equivalent to 95% correctness of local measurement
        float cost_of_error = 0.05f;

        for (WeatherEntity weatherEntity : list) {
            mean_error += weatherEntity.user_temperature - weatherEntity.service_temperature;
            average_percentage += 1 - cost_of_error * Math.abs(weatherEntity.user_temperature - weatherEntity.service_temperature);
        }

        if (!list.isEmpty()){
            mean_error /= list.size();

            average_percentage /= list.size();
            average_percentage *= 100;
        }

        storage.addInteger("data_size", list.size());
        storage.addFloat("mean_error", mean_error);
        storage.addFloat("average_percentage", average_percentage);
    }
}
