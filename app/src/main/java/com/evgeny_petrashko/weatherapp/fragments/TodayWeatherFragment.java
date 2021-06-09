package com.evgeny_petrashko.weatherapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.evgeny_petrashko.weatherapp.App;
import com.evgeny_petrashko.weatherapp.Const;
import com.evgeny_petrashko.weatherapp.ViewModels.WeatherViewModel;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.activities.MainActivity;
import com.evgeny_petrashko.weatherapp.database.PersistentStorage;
import com.evgeny_petrashko.weatherapp.network.NetworkService;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayWeatherFragment extends Fragment {
    private View view;

    @BindView(R.id.location_label) TextView location_label;

    @BindView(R.id.today_weather_image) ImageView weather_image;

    @BindView(R.id.today_weather_report_image) ImageView today_weather_image;

    @BindView(R.id.today_weather_report_text) TextView report_text;

    @BindView(R.id.today_weather_temp_text) TextView temperature_text;

    @BindView(R.id.today_weather_wind_text) TextView wind_text;

    @BindView(R.id.today_weather_humidity_text) TextView humidity_text;

    WeatherViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.today_weather_fragment, container, false);
        ButterKnife.bind(this, view);

        model = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LiveDataWorkFlow();
    }

    // LiveDataObservers settings (listen the actual weather(from Network Service) and location(from LocationModule))
    private void LiveDataWorkFlow(){
        final Observer<WeatherParams> current_info_observer = new Observer<WeatherParams>() {
            @Override
            public void onChanged(@Nullable final WeatherParams params) {
                report_text.setText(params.weather);
                temperature_text.setText(params.temp + Const.degree_sign);
                wind_text.setText(params.wind + " m/s");
                humidity_text.setText(params.humidity + "%");

                if (params.weather.startsWith("Rain")){
                    today_weather_image.setImageResource(R.drawable.rainy_weather_small);
                }else if (params.weather.startsWith("Cloud")){
                    today_weather_image.setImageResource(R.drawable.cloudy_weather_small);
                }else if (params.weather.startsWith("Snow")){
                    today_weather_image.setImageResource(R.drawable.snowly_weather_small);
                }else{
                    today_weather_image.setImageResource(R.drawable.sunny_weather_small);
                }
            }
        };
        model.getActualInfo().observe(getViewLifecycleOwner(),current_info_observer);


        final Observer<String> city_name = new Observer<String>() {
            @Override
            public void onChanged(String city_name_string) {
                location_label.setText(city_name_string);
            }
        };
        model.getCity().observe(getViewLifecycleOwner(), city_name);

    }

}
