package com.evgeny_petrashko.weatherapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.evgeny_petrashko.weatherapp.Const;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.ViewModels.TableViewModel;
import com.evgeny_petrashko.weatherapp.database.WeatherEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TableActivity extends AppCompatActivity {

    TableViewModel model;

    SimpleDateFormat format;

    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");


    @BindView(R.id.data_table) TableLayout table;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.data_table_activity);

        model = new ViewModelProvider(this).get(TableViewModel.class);

        ButterKnife.bind(this);

        format = new SimpleDateFormat(model.service.time_pattern);

        TableConfiguration();
    }

    // Table populating by retrieving information from the database
    private void TableConfiguration(){
        List<WeatherEntity> weatherParams = model.getWholeDataSet();
        ViewGroup viewGroup = findViewById(R.id.content);
        for (WeatherEntity entity : weatherParams) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.table_row_entity_layout, viewGroup, false);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(entity.time);

            String date = format2.format(calendar.getTime());

            ((TextView)(tableRow.findViewById(R.id.date_measurement_title))).setText(date);
            ((TextView)(tableRow.findViewById(R.id.service_temp_title))).setText(entity.service_temperature + Const.degree_sign);
            ((TextView)(tableRow.findViewById(R.id.user_temp_title))).setText(entity.user_temperature + Const.degree_sign);
            ((ImageButton)(tableRow.findViewById(R.id.delete_row_button))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(date, entity, tableRow);
                }
            });
            table.addView(tableRow);
        }
    }

    // Show delete dialog
    private void showDialog(String time, WeatherEntity entity, TableRow row){
        new AlertDialog.Builder(this, R.style.DarkDialogTheme)
                .setMessage("Do you really want to delete entry dated " + time)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        table.removeView(row);
                        model.deleteEntry(entity);
                        model.estimateDataSet();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }


}
