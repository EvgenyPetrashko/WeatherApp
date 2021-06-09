package com.evgeny_petrashko.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evgeny_petrashko.weatherapp.Const;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<WeatherParams> weather_list;
    private SimpleDateFormat format;
    private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

    public DayAdapter(List<WeatherParams> weather_list, String incoming_time_pattern){
        this.weather_list = weather_list;
        this.format = new SimpleDateFormat(incoming_time_pattern);
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_by_hours_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.DayViewHolder holder, int position) {
        WeatherParams params = weather_list.get(position);
        holder.time.setText(timeConversion(params.time));
        holder.report.setText(Const.central_dot_sign + params.weather);
        holder.temp.setText(params.temp + Const.degree_sign);
        holder.wind.setText(params.wind + " m/s");
        holder.humidity.setText(params.humidity + "%");

        if (params.weather.startsWith("Rain")){
            holder.image.setImageResource(R.drawable.rainy_weather_small);
        }else if (params.weather.startsWith("Cloud")){
            holder.image.setImageResource(R.drawable.cloudy_weather_small);
        }else if (params.weather.startsWith("Snow")){
            holder.image.setImageResource(R.drawable.snowly_weather_small);
        }else{
            holder.image.setImageResource(R.drawable.sunny_weather_small);
        }

    }

    @Override
    public int getItemCount() {
        return weather_list.size();
    }

    private String timeConversion(String time){
        try {
            return format2.format(format.parse(time));
        } catch (ParseException e) {
            return "Time Error";
        }
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        TextView time;

        TextView report;

        TextView temp;

        TextView wind;

        TextView humidity;

        ImageView image;
        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.weather_day_by_hours_time_title);
            report = itemView.findViewById(R.id.weather_day_by_hours_report_title);
            temp = itemView.findViewById(R.id.weather_day_by_hours_temperature_title);
            wind = itemView.findViewById(R.id.weather_day_by_hours_wind_title);
            humidity = itemView.findViewById(R.id.weather_day_by_hours_humidity_title);
            image = itemView.findViewById(R.id.weather_day_by_hours_image);
        }
    }
}
