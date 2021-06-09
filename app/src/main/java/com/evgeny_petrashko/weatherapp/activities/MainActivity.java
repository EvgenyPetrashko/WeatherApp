package com.evgeny_petrashko.weatherapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.evgeny_petrashko.weatherapp.App;
import com.evgeny_petrashko.weatherapp.AppComponent;
import com.evgeny_petrashko.weatherapp.LocationModule;
import com.evgeny_petrashko.weatherapp.ViewModels.WeatherViewModel;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.database.PersistentStorage;
import com.evgeny_petrashko.weatherapp.fragments.EstimatorFragment;
import com.evgeny_petrashko.weatherapp.fragments.SeveralDaysWeatherFragment;
import com.evgeny_petrashko.weatherapp.fragments.TodayWeatherFragment;
import com.evgeny_petrashko.weatherapp.network.NetworkService;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    AppComponent appComponent;

    WeatherViewModel model;

    @Inject
    NetworkService service;

    @Inject
    PersistentStorage storage;

    @Inject
    LocationModule locationModule;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private Handler mHandler;
    private long mInterval;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        appComponent = App.appComponent;
        appComponent.inject(this);
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(WeatherViewModel.class);
        service.ProvideLiveDataModel(model);
        storage.provideContext(this);
        locationModule.provideViewModel(model);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bottomNavigationViewInit();

        LiveDataInit();

        mInterval = service.time_when_update_is_needed;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getLastLocation();

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    // Weather checker for repeatable weather functions calling
    Runnable weatherChecker = new Runnable() {
        @Override
        public void run() {
            try {
                long now = Calendar.getInstance().getTimeInMillis();

                if (now - model.getLastServiceCallDayTime() > mInterval / 2) {
                    service.getCurrentInfo();
                } else {
                    model.postLastWeatherDayInfo();
                }

                if (now - model.getLastServiceCallWeekTime() > 6 * mInterval) {
                    service.getWeekInfo();
                } else {
                    model.postLastWeatherWeekInfo();
                }
            } finally {
                mHandler.postDelayed(weatherChecker, mInterval);
            }
        }
    };

    // Run the weather checker
    void startRepeatingTask() {
        weatherChecker.run();
    }

    // Stop repeating task, which retrieves actual info about weather in user region
    void stopRepeatingTask() {
        mHandler.removeCallbacks(weatherChecker);
    }

    // Bottom navifation view initialization
    private void bottomNavigationViewInit() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.week_nav_button:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SeveralDaysWeatherFragment()).commit();
                        break;
                    case R.id.today_nav_button:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayWeatherFragment()).commit();
                        break;
                    case R.id.estimator_nav_button:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EstimatorFragment()).commit();
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.today_nav_button);
    }

    // Set up liveData observers
    // Listening actual weather from NetworkService
    // Listening week weather from NetworkService
    // Listening internet errors from NetworkService
    // Listening user location from LocationModule
    private void LiveDataInit() {
        final Observer<WeatherParams> current_info_observer = new Observer<WeatherParams>() {
            @Override
            public void onChanged(@Nullable final WeatherParams params) {
                model.SaveLastServiceDayInfo(params);
            }
        };


        model.getActualInfo().observe(this, current_info_observer);

        final Observer<List<WeatherParams>> week_info_observer = new Observer<List<WeatherParams>>() {
            @Override
            public void onChanged(List<WeatherParams> weatherParams) {
                model.SaveLastServiceWeekInfo(weatherParams);
            }
        };

        model.getSeveralDaysInfo().observe(this, week_info_observer);

        final Observer<String> error_observer = new Observer<String>() {
            @Override
            public void onChanged(String error_message) {
                Toast.makeText(MainActivity.this, "Internet connection problems, please wait a little bit)", Toast.LENGTH_LONG).show();
                Log.d("ErrorMes", error_message);
            }
        };

        model.getError().observe(this, error_observer);
    }

    // Request permission for location retrieving
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 200);
    }

    // Check if location providers is enabled
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }else{
                requestPermissions();
            }
        }else{
            finish();
        }
    }

    // Gets last location of user. Functionality provided in LocationModule
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationModule);
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    // Check permissions for location retrieving
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


}