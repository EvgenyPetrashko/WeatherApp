package com.evgeny_petrashko.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evgeny_petrashko.weatherapp.ViewModels.WeatherViewModel;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.adapters.WeekAdapter;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeveralDaysWeatherFragment extends Fragment {
    private View view;

    @BindView(R.id.rv_weather_day) RecyclerView rv;

    private WeekAdapter weekAdapter;

    WeatherViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.week_weather_fragment, container, false);
        model = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        ButterKnife.bind(this, view);
        recyclerViewInit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LiveDataWorkFlow();
    }

    // RecyclerView settings
    private void recyclerViewInit(){
        rv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        weekAdapter = new WeekAdapter(model.service.time_pattern);
        rv.setAdapter(weekAdapter);
    }

    // LiveDataObserver settings
    private void LiveDataWorkFlow(){
        final Observer<List<WeatherParams>> week_info_observer = new Observer<List<WeatherParams>>() {
            @Override
            public void onChanged(List<WeatherParams> weatherParams) {
                    weekAdapter.populate(weatherParams);
            }
        };

        model.getSeveralDaysInfo().observe(getViewLifecycleOwner(),week_info_observer);
    }
}
