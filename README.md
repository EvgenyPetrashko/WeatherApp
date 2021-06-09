# WeatherApp

## Ideas of the application:
1) Provides convenient service for getting up-to-date / weekly information
2) Allows you to evaluate the accuracy of server data, and subsequently receive data taking into account the average error
3) Demonstrates proficiency in popular libraries such as: Dagger2, Retrofit2, LiveData, Room2, ButterKnife and GSON.

# Technology stack:
- Dagger2 for dependency injections / tool for structuring
- Room2 for client-server conversation / accepts weather data from the server and post it to ViewModels
- LiveData for observing the server data changing and location changing
- Room2 for storing data in sqlite database
- ButterKnife for working with view, especially views initialization
- Gson for converting json data to correspondent java classes

# App schema (MVVM architecture):
![alt-text](/Presentation/weather_app_shema.png)

# Demo
![alt-text](/Presentation/demo.gif)

# Screenshots &#32;
## Fragment which displays current info about weather
![alt-text](/Presentation/screenshot&#32;(1).jpg)

## Fragment which displays weather of nearest days(max: 5-6)
![alt-text](/Presentation/screenshot&#32;(2).jpg)

## Fragment which displays weather of concrete day in 3 hours step (+ average temp/wind speed/humidity)
![alt-text](/Presentation/screenshot&#32;(3).jpg)

## Fragment contains 3 control button for further estimations
![alt-text](/Presentation/screenshot&#32;(4).jpg)

## After pressing the Enter button, we can enter temperature from our thermometer and then app will compare the user and service temperatures
![alt-text](/Presentation/screenshot&#32;(5).jpg)

## After pressing the Show button, we can estimated data
![alt-text](/Presentation/screenshot&#32;(6).jpg)

## After pressing the Delete button, we can see all database entries
![alt-text](/Presentation/screenshot&#32;(7).jpg)

## API
- WeatherAPI: https://openweathermap.org/api

