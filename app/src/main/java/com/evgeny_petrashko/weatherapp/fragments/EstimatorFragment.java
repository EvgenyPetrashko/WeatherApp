package com.evgeny_petrashko.weatherapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.evgeny_petrashko.weatherapp.Const;
import com.evgeny_petrashko.weatherapp.ViewModels.WeatherViewModel;
import com.evgeny_petrashko.weatherapp.R;
import com.evgeny_petrashko.weatherapp.activities.TableActivity;
import com.evgeny_petrashko.weatherapp.database.PersistentStorage;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EstimatorFragment extends Fragment {
    private View view;

    @BindView(R.id.show_data_block) ExpandableLayout show_data_block;

    @BindView(R.id.enter_data_block) ExpandableLayout enter_data_block;

    @BindView(R.id.total_data_collected_title) TextView total_data_collected_title;
    String total_data_collected_preTitle = "Total data collected: ";

    @BindView(R.id.average_percentage_of_correctness_title)
    TextView average_percentage_of_correctness_title;
    String average_percentage_preTitle = "Average percentage of correctness: ";

    @BindView(R.id.mean_error_title) TextView mean_error_title;
    String mean_error_preTitle = "Mean error: ";

    @BindView(R.id.predicted_temperature_title) TextView predicted_temperature_title;
    String predicted_temperature_preTitle = "Predicted temperature (including mean error): ";

    @BindView(R.id.enter_temperature_edit_text) EditText enter_temperature_edit_text;

    // Control Buttons
    @BindView(R.id.enter_temp_button) Button enter_data;

    @BindView(R.id.show_data_button) Button show_data;

    @BindView(R.id.delete_data_button) Button delete_data;

    @BindView(R.id.view_data_button) Button view_button;

    private WeatherViewModel model;

    private String show_data_button_stateShown = "Hide data";
    private String show_data_button_stateHidden = "Show Estimated Data";

    private String enter_data_button_stateShown = "Enter";
    private String enter_data_button_stateHidden = "Enter temperature from your thermometer";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        view = inflater.inflate(R.layout.estimator_weather_fragment, container, false);

        ButterKnife.bind(this, view);

        buttonsClickListeners();
        return view;
    }

    // Button onClickListeners set up
    private void buttonsClickListeners(){
        enter_data.setOnClickListener(enterData());

        show_data.setOnClickListener(showData());

        delete_data.setOnClickListener(deleteData());

        view_button.setOnClickListener(viewData());
    }

    // On Enter temperature button click listener which shows onEnterConfirmationDialog or
    // shows the edit text for data entering
    private View.OnClickListener enterData(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enter_data_block.isExpanded()){
                    String entered_temperature = enter_temperature_edit_text.getText().toString();
                    if (model.temperatureValidation(entered_temperature)){
                        showDialogWithSpecifiedString("Do you want to add this temperature into database?", EnterDataConfirm(entered_temperature));
                    }
                }else{
                    enter_data_block.expand();
                    show_data_block.collapse();

                    enter_data.setText(enter_data_button_stateShown);
                    show_data.setText(show_data_button_stateHidden);

                }
            }
        };
    }

    // On Show dataSet button click listener which shows Estimated data or hide it
    private View.OnClickListener showData(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_data_block.isExpanded()){
                    show_data_block.collapse();

                    show_data.setText(show_data_button_stateHidden);
                }else{
                    show_data_block.expand();
                    enter_data_block.collapse();

                    HashMap<String, Object> map = model.getEstimatedData();
                    int data_size = Math.round((int)map.getOrDefault(PersistentStorage.total_data_string, 0f));
                    total_data_collected_title.setText(total_data_collected_preTitle + data_size);
                    if (data_size == 0){
                        mean_error_title.setText(mean_error_preTitle + "No data");
                        average_percentage_of_correctness_title.setText(average_percentage_preTitle + "No data");
                        predicted_temperature_title.setText(predicted_temperature_preTitle + "No data");
                    }else{
                        mean_error_title.setText(mean_error_preTitle + (float) map.getOrDefault(PersistentStorage.mean_error_string, 0f));
                        average_percentage_of_correctness_title.setText(average_percentage_preTitle + (float) map.getOrDefault(PersistentStorage.average_percentage_string, 0f) + "%");
                        int predicted_temp = (int) Math.round(model.getLastServiceTemperature() + (float) map.getOrDefault(PersistentStorage.mean_error_string, 0f));
                        predicted_temperature_title.setText(predicted_temperature_preTitle + predicted_temp + Const.degree_sign);
                    }

                    show_data.setText(show_data_button_stateShown);
                    enter_data.setText(enter_data_button_stateHidden);
                    enter_temperature_edit_text.setText("");
                }
            }
        };
    }

    // On Delete dataSet button click listener which show onDeleteConfirmationDialog
    private View.OnClickListener deleteData(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWithSpecifiedString("Do you really want to delete whole data set?", DeleteDataConfirm());
            }
        };
    }

    // On View dataSet button click listener which shows us activity with whole saved dataSet
    private View.OnClickListener viewData(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_data_block.collapse();
                show_data.setText(show_data_button_stateHidden);
                Intent intent = new Intent(EstimatorFragment.this.getContext(), TableActivity.class);
                startActivity(intent);
            }
        };
    }

    // Function which shows dialog with specified string(Confident in adding or in deleting)
    // and performs the correspondent actions
    private void showDialogWithSpecifiedString(String title, DialogInterface.OnClickListener onClickListener){
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setPositiveButton("Yes", onClickListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    // Adds data to the dataSet
    private DialogInterface.OnClickListener EnterDataConfirm(String temperature){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enter_data_block.collapse();

                enter_data.setText(enter_data_button_stateHidden);
                enter_temperature_edit_text.setText("");

                model.addDataToDatabase(Integer.parseInt(temperature));
            }
        };
    }

    // Delete the whole dataset
    private DialogInterface.OnClickListener DeleteDataConfirm(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enter_data_block.collapse();
                show_data_block.collapse();
                show_data.setText(show_data_button_stateHidden);
                enter_data.setText(enter_data_button_stateHidden);

                model.deleteEstimatedData();
            }
        };
    }
}
