package com.evgeny_petrashko.weatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evgeny_petrashko.weatherapp.App;
import com.evgeny_petrashko.weatherapp.AppComponent;
import com.evgeny_petrashko.weatherapp.Const;
import com.evgeny_petrashko.weatherapp.ViewModels.ConcreteDayViewModel;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.adapters.DayAdapter;
import com.evgeny_petrashko.weatherapp.adapters.WeekAdapter;
import com.evgeny_petrashko.weatherapp.database.PersistentStorage;
import com.evgeny_petrashko.weatherapp.network.NetworkService;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConcreteDayWeatherActivity extends AppCompatActivity {

    private ConcreteDayViewModel model;

    @BindView(R.id.weather_by_hour_rv) RecyclerView weather_by_hours;

    @BindView(R.id.average_temperature_card_title) TextView average_temp;
    String average_temp_preTitle = "Average temperature:\n";

    @BindView(R.id.max_temperature_card_title) TextView max_temp;
    String max_temp_preTitle = "Max temperature:\n";

    @BindView(R.id.min_temperature_card_title) TextView min_temp;
    String min_temperature_preTitle = "Min temperature:\n";

    @BindView(R.id.average_wind_card_title) TextView average_wind;
    String average_wind_preTitle = "Average speed of wind:\n";

    @BindView(R.id.average_humidity_card_title) TextView average_humidity;
    String average_humidity_preTitle = "Average humidity:\n";

    @BindView(R.id.concrete_day_date_title) TextView date_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        model = new ViewModelProvider(this).get(ConcreteDayViewModel.class);

        setContentView(R.layout.concrete_day_weather_activity);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        ArrayList<WeatherParams> params = intent.getParcelableArrayListExtra(WeekAdapter.array_string);
        int min_temperature = intent.getIntExtra(WeekAdapter.min_temp_string, 0);
        int max_temperature = intent.getIntExtra(WeekAdapter.max_temp_string, 0);
        String date = intent.getStringExtra(WeekAdapter.date_string);

        date_title.setText(date);

        recyclerViewSettings(params);
        horizontalViewSettings(params, max_temperature, min_temperature);

    }

    // RecyclerView settings: Adapter populating and etc.
    private void recyclerViewSettings(ArrayList<WeatherParams> params){
        weather_by_hours.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        DayAdapter dayAdapter = new DayAdapter(params, model.service.time_pattern);
        weather_by_hours.setAdapter(dayAdapter);
    }

    // HorizontalView settings: set title of
    // (AverageTemperatureCard, MaxTemperatureCard, MinTemperatureCard, AverageWindSpeedCard, AverageHumidityCard)
    private void horizontalViewSettings(ArrayList<WeatherParams> params, int max_temperature, int min_temperature){
        HashMap<String, Float> map = model.getAverageData(params);
        float average_temperature = map.getOrDefault(ConcreteDayViewModel.average_temperature_string, 0f);
        average_temp.setText(average_temp_preTitle + average_temperature + Const.degree_sign);
        float average_wind_speed = map.getOrDefault(ConcreteDayViewModel.average_wind_speed_string, 0f);
        average_wind.setText(average_wind_preTitle + average_wind_speed + " m/s");
        float average_hum = map.getOrDefault(ConcreteDayViewModel.average_humidity_string, 0f);
        average_humidity.setText(average_humidity_preTitle + average_hum + "%");

        min_temp.setText(min_temperature_preTitle + min_temperature + Const.degree_sign);
        max_temp.setText(max_temp_preTitle + max_temperature + Const.degree_sign);
    }
}
