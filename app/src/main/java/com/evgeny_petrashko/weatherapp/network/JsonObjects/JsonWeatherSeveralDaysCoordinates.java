package com.evgeny_petrashko.weatherapp.network.JsonObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class JsonWeatherSeveralDaysCoordinates {

    @SerializedName("cod")
    @Expose
    public String cod;
    @SerializedName("message")
    @Expose
    public Long message;
    @SerializedName("cnt")
    @Expose
    public Long cnt;
    @SerializedName("list")
    @Expose
    public List<ListCoordinates> list = null;
    @SerializedName("city")
    @Expose
    public City city;

}


class RainCoordinates {

    @SerializedName("3h")
    @Expose
    public Float _3h;

}

