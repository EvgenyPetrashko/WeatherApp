package com.evgeny_petrashko.weatherapp;

import com.evgeny_petrashko.weatherapp.ViewModels.BaseViewModel;
import com.evgeny_petrashko.weatherapp.ViewModels.WeatherViewModel;
import com.evgeny_petrashko.weatherapp.activities.ConcreteDayWeatherActivity;
import com.evgeny_petrashko.weatherapp.activities.MainActivity;
import com.evgeny_petrashko.weatherapp.database.PersistentStorage;
import com.evgeny_petrashko.weatherapp.fragments.SeveralDaysWeatherFragment;
import com.evgeny_petrashko.weatherapp.fragments.TodayWeatherFragment;
import com.evgeny_petrashko.weatherapp.network.NetworkService;

import dagger.Component;

@Component(modules = {NetworkService.class, PersistentStorage.class, LocationModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(BaseViewModel baseViewModel);

}

