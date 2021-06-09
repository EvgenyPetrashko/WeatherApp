package com.evgeny_petrashko.weatherapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.evgeny_petrashko.weatherapp.ViewModels.BaseViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule implements LocationListener {
    public static String latitude_string = "latitude";
    public static String longitude_string = "longitude";
    public static String last_coordinated_update = "last_coord_update";

    BaseViewModel model;

    Context context;

    @Provides
    public LocationModule getInstance(){
        return this;
    }

    // Gets ViewModel from the activity
    public void provideViewModel(BaseViewModel model){
        this.model = model;
        this.context = model.getApplication().getApplicationContext();
    }

    // Puts location string to the liveData object
    private void provideCityName(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String city = addresses.get(0).getLocality();
            String concrete = addresses.get(0).getSubLocality();
            String full = concrete + ", " + city;
            model.getCity().setValue(full);
        } catch (IOException e) {
            model.getCity().setValue("Unknown locality");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (model != null){
            double latitude = location.getLatitude();;
            double longitude = location.getLongitude();
            model.saveGeographicalCoordinates(latitude, longitude);
            provideCityName(latitude, longitude);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
