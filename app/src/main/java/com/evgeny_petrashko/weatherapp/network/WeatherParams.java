package com.evgeny_petrashko.weatherapp.network;


import android.os.Parcel;
import android.os.Parcelable;

public class WeatherParams implements Parcelable{
    public String time = "Error";

    public String weather = "Error";

    public int temp = 0;

    public double wind = 0;

    public int humidity = 0;

    protected WeatherParams(Parcel in) {
        time = in.readString();
        weather = in.readString();
        temp = in.readInt();
        wind = in.readDouble();
        humidity = in.readInt();
    }

    public WeatherParams(){

    }

    public static final Creator<WeatherParams> CREATOR = new Creator<WeatherParams>() {
        @Override
        public WeatherParams createFromParcel(Parcel in) {
            return new WeatherParams(in);
        }

        @Override
        public WeatherParams[] newArray(int size) {
            return new WeatherParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(weather);
        dest.writeInt(temp);
        dest.writeDouble(wind);
        dest.writeInt(humidity);
    }
}
