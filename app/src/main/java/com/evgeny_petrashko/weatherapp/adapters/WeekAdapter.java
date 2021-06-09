package com.evgeny_petrashko.weatherapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evgeny_petrashko.weatherapp.Const;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.activities.ConcreteDayWeatherActivity;
import com.evgeny_petrashko.weatherapp.network.DayWeatherParams;
import com.evgeny_petrashko.weatherapp.network.WeatherParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DayOfWeekHolder> {
    public static String array_string = "array";
    public static String max_temp_string = "max_temp";
    public static String min_temp_string = "min_temp";
    public static String date_string = "date";
    private ArrayList<DayWeatherParams> weather_list;
    private String incoming_pattern;
    private Context context;

    public WeekAdapter(String time_pattern){
        this.incoming_pattern = time_pattern;
        weather_list = new ArrayList<>();
    }

    // Distinguish the entities by its days for making and estimating whole day
    private ArrayList<DayWeatherParams> dateDistinguish(List<WeatherParams> weather_list) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(incoming_pattern);
        SimpleDateFormat helper = new SimpleDateFormat("yyyy-MM-dd");
        Calendar current_calendar = Calendar.getInstance();

        HashMap<String, ArrayList<WeatherParams>> map_days = new HashMap<>();

        for (WeatherParams weatherParams : weather_list) {
            String date = "";
            try {
                Date data = dateFormat.parse(weatherParams.time);
                date = helper.format(data);

            } catch (ParseException e) {
                date = helper.format(current_calendar.getTime());
            }
            if (map_days.containsKey(date)){
                map_days.get(date).add(weatherParams);
            }else{
                ArrayList<WeatherParams> new_list = new ArrayList<>();
                new_list.add(weatherParams);
                map_days.put(date, new_list);
            }
        }

        ArrayList<DayWeatherParams> days = new ArrayList<>();

        for (Map.Entry<String, ArrayList<WeatherParams>> stringArrayListEntry : map_days.entrySet()) {
            days.add(new DayWeatherParams(incoming_pattern, stringArrayListEntry.getValue()));
        }

        Collections.sort(days);
        return days;

    }

    private String getMaxMinTemperatureString(int max_temp, int min_temp){
        return max_temp + Const.degree_sign + "/" + min_temp + Const.degree_sign;
    }

    // Getting list of weatherParams from the corresponding fragment/activity
    public void populate(List<WeatherParams> weather_list){
        this.weather_list = dateDistinguish(weather_list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayOfWeekHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DayOfWeekHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_day_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DayOfWeekHolder holder, int position) {
        DayWeatherParams dayWeatherParams = weather_list.get(position);
        holder.report.setText(Const.central_dot_sign + dayWeatherParams.weather);
        holder.date.setText(dayWeatherParams.date);
        holder.max_min_temp.setText(getMaxMinTemperatureString(dayWeatherParams.max_temperature, dayWeatherParams.min_temperature));

        if (dayWeatherParams.weather.startsWith("Rain")){
            holder.weather.setImageResource(R.drawable.rainy_weather_small);
        }else if (dayWeatherParams.weather.startsWith("Cloud")){
            holder.weather.setImageResource(R.drawable.cloudy_weather_small);
        }else if (dayWeatherParams.weather.startsWith("Snow")){
            holder.weather.setImageResource(R.drawable.snowly_weather_small);
        }else{
            holder.weather.setImageResource(R.drawable.sunny_weather_small);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConcreteDayWeatherActivity.class);
                intent.putExtra(array_string, dayWeatherParams.weather_list);
                intent.putExtra(max_temp_string, dayWeatherParams.max_temperature);
                intent.putExtra(min_temp_string, dayWeatherParams.min_temperature);
                intent.putExtra(date_string, dayWeatherParams.date);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return weather_list.size();
    }

    public class DayOfWeekHolder extends RecyclerView.ViewHolder {
        ImageView weather;

        TextView date;

        TextView report;

        TextView max_min_temp;
        public DayOfWeekHolder(@NonNull View itemView) {
            super(itemView);
            weather = itemView.findViewById(R.id.weather_day_image);
            date = itemView.findViewById(R.id.weather_day_date);
            report = itemView.findViewById(R.id.weather_day_report);
            max_min_temp = itemView.findViewById(R.id.weather_day_max_min_temp);
        }
    }
}
