package com.evgeny_petrashko.weatherapp;

import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherCity;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherCoordinates;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherSeveralDays;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.JsonWeatherSeveralDaysCoordinates;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.List2;
import com.evgeny_petrashko.weatherapp.network.JsonObjects.ListCoordinates;
import com.evgeny_petrashko.weatherapp.network.NetworkService;
import com.evgeny_petrashko.weatherapp.network.WeatherAPI;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.TestCase.assertNotNull;

public class NetworkTest {
    private NetworkService networkService;
    private String base_url = "https://api.openweathermap.org/data/2.5/";
    private String API_KEY = "2a3ef1ce7aa8026db782522f1b604aee";
    double latitude;
    double longitude;
    WeatherAPI service;


    @Before
    public void preparation(){
        networkService = Mockito.mock(NetworkService.class);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherAPI.class);

    }

    @Test
    public void isNull(){
        assertNotNull(networkService);
    }

    @Test
    public void getActualInfo() throws InterruptedException {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Call<JsonWeatherCity> call = service.getCurrentWeather("Kazan", API_KEY);
                call.enqueue(new Callback<JsonWeatherCity>() {
                    @Override
                    public void onResponse(Call<JsonWeatherCity> call, Response<JsonWeatherCity> response) {
                        if (response.isSuccessful()){
                            JsonWeatherCity json = response.body();
                            System.out.println(json.weather.get(0).main);
                            System.out.println(json.main.temp);
                            System.out.println(json.wind.speed);
                            System.out.println(json.main.humidity);
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonWeatherCity> call, Throwable t) {

                    }
                });

                return null;
            }
        }).when(networkService).getCurrentInfo();

        networkService.getCurrentInfo();
        Thread.sleep(5000);
    }

    @Test
    public void getActualInfoByCoordinates() throws InterruptedException {
        Call<JsonWeatherCoordinates> call = service.getCurrentWeatherByCoordinates(latitude, longitude, API_KEY);
        call.enqueue(new Callback<JsonWeatherCoordinates>() {
            @Override
            public void onResponse(Call<JsonWeatherCoordinates> call, Response<JsonWeatherCoordinates> response) {
                if (response.isSuccessful()){
                    JsonWeatherCoordinates json = response.body();
                    System.out.println(json.weather.get(0).main);
                    System.out.println(json.main.temp);
                    System.out.println(json.wind.speed);
                    System.out.println(json.main.humidity);
                }

            }

            @Override
            public void onFailure(Call<JsonWeatherCoordinates> call, Throwable t) {
            }
        });

        Thread.sleep(5000);
    }

    @Test
    public void getWeekInfo() throws InterruptedException {
        Call<JsonWeatherSeveralDays> call = service.getSeveralDaysWeatherInfo("Kazan", API_KEY);

        call.enqueue(new Callback<JsonWeatherSeveralDays>() {
            @Override
            public void onResponse(Call<JsonWeatherSeveralDays> call, Response<JsonWeatherSeveralDays> response) {
                if (response.isSuccessful()){
                    JsonWeatherSeveralDays json = response.body();
                    List<List2> weather_list = json.list;
                    for (List2 list2 : weather_list) {
                        System.out.println("Weather Entry:");
                        System.out.println("Date: " + list2.dtTxt);
                        System.out.println("Temp: " + list2.main.temp);
                        System.out.println("Weather: " + list2.weather.get(0).main);
                        System.out.println("Wind: " + list2.wind.speed);
                        System.out.println("Humidity: " + list2.main.humidity + "\n");

                    }
                }
            }
            @Override
            public void onFailure(Call<JsonWeatherSeveralDays> call, Throwable t) {

            }
        });
        Thread.sleep(5000);

    }

    @Test
    public void getWeekInfoByCoordinates() throws InterruptedException {
        Mockito.when(networkService.getLatitude()).thenReturn(50d);
        Mockito.when(networkService.getLongitude()).thenReturn(50d);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Call<JsonWeatherSeveralDaysCoordinates> call = service.getSeveralDaysWeatherInfo(networkService.getLatitude(), networkService.getLongitude(), API_KEY);

                call.enqueue(new Callback<JsonWeatherSeveralDaysCoordinates>() {
                    @Override
                    public void onResponse(Call<JsonWeatherSeveralDaysCoordinates> call, Response<JsonWeatherSeveralDaysCoordinates> response) {
                        if (response.isSuccessful()){
                            JsonWeatherSeveralDaysCoordinates json = response.body();
                            List<ListCoordinates> weather_list = json.list;
                            for (ListCoordinates listCoordinates : weather_list) {
                                System.out.println("Weather Entry:");
                                System.out.println("Date: " + listCoordinates.dtTxt);
                                System.out.println("Temp: " + listCoordinates.main.temp);
                                System.out.println("Weather: " + listCoordinates.weather.get(0).main);
                                System.out.println("Wind: " + listCoordinates.wind.speed);
                                System.out.println("Humidity: " + listCoordinates.main.humidity + "\n");
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<JsonWeatherSeveralDaysCoordinates> call, Throwable t) {
                    }
                });
                return null;
            }
        }).when(networkService).getWeekInfo();

        networkService.getWeekInfo();

        Thread.sleep(5000);
    }

}
