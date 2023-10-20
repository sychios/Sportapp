package com.example.sportapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class TrainingFormActivity extends AppCompatActivity {

    private RVAdapter rvAdapter;

    private Button buttonCancel;
    private Button buttonConfirm;

    private Button buttonDuration15;
    private Button buttonDuration30;
    private Button buttonDuration60;
    private Button buttonDurationManually;
    private Button activeDurationButton;
    private boolean buttonDurationIsSelected = false;
    private int duration;

    private EditText trainingDuration;
    private String spinnerSelectedCategory = "";

    // Spinner
    private Spinner categorySpinner;
    private boolean spinnerIsInitialized = false;
    private boolean categoryIsSelected = false;

    // im Speicher Liste ablegen??
    private List<String> categories;
    private String[] _categories = MainActivity.getAppContext().getResources().getStringArray(R.array.standard_categories_names);


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.training_form);

        rvAdapter = MainActivity.getRVAdapter();

        trainingDuration = findViewById(R.id.input_training_duration);

        buttonDuration15 = findViewById(R.id.button_duration_15);
        buttonDuration15.setOnClickListener(v -> ButtonClicked(buttonDuration15, 15));

        buttonDuration30 = findViewById(R.id.button_duration_30);
        buttonDuration30.setOnClickListener(v -> ButtonClicked(buttonDuration30, 30));

        buttonDuration60 = findViewById(R.id.button_duration_60);
        buttonDuration60.setOnClickListener(v -> ButtonClicked(buttonDuration60, 60));

        buttonDurationManually = findViewById(R.id.button_duration_manually);
        buttonDurationManually.setOnClickListener(v -> ButtonClicked(buttonDurationManually, -1));

        categories = Arrays.asList(_categories);


        // Setting up spinner
        categorySpinner = findViewById(R.id.spinner_training_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item , categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerIsInitialized) {
                    spinnerIsInitialized = true;
                    return;
                }
                spinnerSelectedCategory = _categories[position];
                categoryIsSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        trainingDuration = findViewById(R.id.input_training_duration);

        buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(v -> startActivity(new Intent(TrainingFormActivity.this, MainActivity.class)));

        buttonConfirm = findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(v -> {

            if(!categoryIsSelected | spinnerSelectedCategory.equals(_categories[0])) {
                Toast.makeText(getApplicationContext(), "Keine Kategorie ausgewählt. ", Toast.LENGTH_SHORT).show();
                return;
            } else if(!buttonDurationIsSelected) {
                Toast.makeText(getApplicationContext(), "Keine Trainignsdauer gewählt." ,Toast.LENGTH_SHORT).show();
                return;
            } else if(activeDurationButton == buttonDurationManually && trainingDuration.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Gib' 'ne Trainingsdauer ein du Noob." ,Toast.LENGTH_SHORT).show();
                return;
            } else {
                if(activeDurationButton == buttonDurationManually) {
                    try {
                        this.duration = Integer.parseInt(trainingDuration.getText().toString());

                        TrainingInstance ti = new TrainingInstance(spinnerSelectedCategory, this.duration);
                        rvAdapter.AddItem(ti);
                        //MainActivity.AddTrainingInstance(ti);

                        startActivity(new Intent(TrainingFormActivity.this, MainActivity.class));

                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    TrainingInstance ti = new TrainingInstance(spinnerSelectedCategory, this.duration);
                    rvAdapter.AddItem(ti);
                    //MainActivity.AddTrainingInstance(ti);

                    Toast.makeText(getApplicationContext(), "Trainingsinstanz erstellt", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(TrainingFormActivity.this, MainActivity.class));
                }
            }
        });

    }

    /**
     * Active Colors: Text= #F1FAEE, Background=
     * Disactive Colors: Text= #1D3557 , Background= #A8DADC
     * @param pButton
     */
    private void ButtonClicked(Button pButton, int pDuration){
        // Manuelle Eingabe
        if (pDuration == -1) {
            if(activeDurationButton != null)
                ResetButtonLayout(activeDurationButton);
            trainingDuration.setVisibility(View.VISIBLE);
            activeDurationButton = buttonDurationManually;
            buttonDurationIsSelected = true;
        } else if(activeDurationButton == null){
            SetActiveButtonLayout(pButton);
            activeDurationButton = pButton;
            duration = pDuration;
        } else {
            ResetButtonLayout(activeDurationButton);
            activeDurationButton = pButton;
            SetActiveButtonLayout(activeDurationButton);
            duration = pDuration;
        }
    }

    private void ResetButtonLayout(Button pButton){
        if(pButton == buttonDurationManually){
            trainingDuration.setVisibility(View.INVISIBLE);
        } else {
            pButton.setBackgroundColor(Color.parseColor("#A8DADC"));
            pButton.setTextColor(Color.parseColor("#1D3557"));
        }
        buttonDurationIsSelected = false;
        duration = 0;
    }

    private void SetActiveButtonLayout(Button pButton){
        pButton.setBackgroundColor(Color.parseColor("#1D3557"));
        pButton.setTextColor(Color.parseColor("#F1FAEE"));
        buttonDurationIsSelected = true;
    }
}
