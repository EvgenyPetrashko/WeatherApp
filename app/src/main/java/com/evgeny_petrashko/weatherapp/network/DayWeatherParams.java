package com.evgeny_petrashko.weatherapp.network;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DayWeatherParams implements Comparable{
    // class which represents day weather
    // time corresponds to day itself
    // weather corresponds to 'average' weather in this day
    // wind corresponds to average wind speed
    public ArrayList<WeatherParams> weather_list;

    public String date;

    private long milliseconds;

    public String weather = "Clear";

    public int max_temperature;

    public int min_temperature;

    public double average_wind;

    private HashMap<String, Integer> average_weather;

    public DayWeatherParams(String incoming_pattern, ArrayList<WeatherParams> weather_list){
        this.weather_list = weather_list;
        average_weather = new HashMap<>();
        calculations();

        SimpleDateFormat format1 = new SimpleDateFormat(incoming_pattern);
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);

        try {
            date = format2.format(format1.parse(weather_list.get(0).time));
            milliseconds = format1.parse(weather_list.get(0).time).getTime();
        } catch (ParseException e) {
            date = weather_list.get(0).time;
        }

    }

    // Calculation the 'average_weather', average wind, min/max temperatures
    public void calculations(){
        max_temperature = 0;
        min_temperature = 100;
        double wind_sum = 0;
        for (WeatherParams params : weather_list) {
            if (params.temp > max_temperature){
                max_temperature = params.temp;
            }
            if (params.temp < min_temperature){
                min_temperature = params.temp;
            }
            wind_sum += params.wind;

            average_weather.put(params.weather, average_weather.getOrDefault(params.weather, 0) + 1);
        }

        int frequent_weather_count = 0;
        for (Map.Entry<String, Integer> stringIntegerEntry : average_weather.entrySet()) {
            if (stringIntegerEntry.getValue() > frequent_weather_count){
                frequent_weather_count = stringIntegerEntry.getValue();
                weather = stringIntegerEntry.getKey();
            }
        }

        average_wind = wind_sum / weather_list.size();
    }

    @Override
    public int compareTo(Object o) {
        return (this.milliseconds - ((DayWeatherParams) o).milliseconds > 0) ? 1 : -1;
    }
}
