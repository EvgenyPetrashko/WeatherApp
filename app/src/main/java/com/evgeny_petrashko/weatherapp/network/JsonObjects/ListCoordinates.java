package com.evgeny_petrashko.weatherapp.network.JsonObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCoordinates {

    @SerializedName("dt")
    @Expose
    public Long dt;
    @SerializedName("main")
    @Expose
    public Main2 main;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = null;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("visibility")
    @Expose
    public Long visibility;
    @SerializedName("pop")
    @Expose
    public Float pop;
    @SerializedName("rain")
    @Expose
    public RainCoordinates rain;
    @SerializedName("sys")
    @Expose
    public Sys2 sys;
    @SerializedName("dt_txt")
    @Expose
    public String dtTxt;

}
