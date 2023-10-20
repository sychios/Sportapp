package com.example.sportapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatisticsFragmentUserDefinedComparison extends Fragment{

    private String[] categories;

    // 'comp' is referring to the items to compare
    private Spinner comp1Spinner;
    private Spinner comp2Spinner;
    private Spinner timeFrameSpinner;

    private boolean comp1IsSelected = false;
    private String comp1Category;

    private boolean comp2IsSelected = false;
    private String comp2Category;

    private boolean timeframeIsSelected = false;
    private String timeFrame;

    PieChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.statistics_fragment_userdefinedcomparison, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categories = MainActivity.getAppContext().getResources().getStringArray(R.array.standard_categories_names);

        chart = getView().findViewById(R.id.custom_chart);

        SetupSpinner();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetupSpinner(){
        // Setting up spinner
        comp1Spinner = getView().findViewById(R.id.comparison_one);
        comp2Spinner = getView().findViewById(R.id.comparison_two);
        timeFrameSpinner = getView().findViewById(R.id.comparison_timeframe);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getAppContext(), R.layout.spinner_item_statistics , Arrays.asList(categories));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comp1Spinner.setAdapter(adapter);
        comp1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!comp1IsSelected) {
                    comp1IsSelected = true;
                }
                comp1Category = categories[position];
                if(comp2IsSelected && timeframeIsSelected)
                    DisplayChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.getAppContext(), R.layout.spinner_item_statistics , Arrays.asList(categories));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comp2Spinner.setAdapter(adapter2);
        comp2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!comp2IsSelected) {
                    comp2IsSelected = true;
                }
                comp2Category = categories[position];
                if(comp1IsSelected && timeframeIsSelected)
                    DisplayChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


        String[] timeFrames = new String[] {"1", "2", "4", "6", "10"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(MainActivity.getAppContext(), R.layout.spinner_item_statistics , Arrays.asList(timeFrames));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeFrameSpinner.setAdapter(adapter3);
        timeFrameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!timeframeIsSelected) {
                    timeframeIsSelected = true;
                }
                timeFrame = timeFrames[position];
                if(comp1IsSelected && comp2IsSelected)
                    DisplayChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void DisplayChart(){
        if(! (comp1IsSelected && comp2IsSelected && timeframeIsSelected))
            return;

        String[] comparison_cats = new String[] {comp1Category, comp2Category};
        Long time = Long.parseLong(timeFrame);

        List<PieEntry> data = GeneratePieEntryData(comparison_cats, time);

        int darkTextColor = Color.parseColor("#0E131F");
        int lightTextColor = Color.parseColor("#F1FAEE");

        int colorLaufen = Color.parseColor("#14213d");
        int colorYoga = Color.parseColor("#8d99ae");
        int colorÜbungen = Color.parseColor("#432818");
        int colorBurpees = Color.parseColor("#7f1817");
        int colorStrength = Color.parseColor("#bb9457");
        int colorSeilklettern = Color.parseColor("#fb8500");
        int colorBouldern = Color.parseColor("#fca311");
        int colorAeropow = Color.parseColor("#9e2a2b");
        int colorAeroCap = Color.parseColor("#540b0e");
        int colorAnCap = Color.parseColor("#a3b18a");
        int colorAnPow = Color.parseColor("#3a5a40");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(colorLaufen);
        colors.add(colorYoga);
        colors.add(colorÜbungen);
        colors.add(colorBurpees);
        colors.add(colorStrength);
        colors.add(colorSeilklettern);
        colors.add(colorBouldern);
        colors.add(colorAnCap);
        colors.add(colorAeropow);
        colors.add(colorAeroCap);
        colors.add(colorAnPow);

        PieDataSet set = new PieDataSet(data, "");
        set.setColors(colors);

        set.setValueTextSize(20f);
        set.setValueTextColor(lightTextColor);

        PieData d = new PieData(set);

        d.setValueFormatter(new PercentFormatter(chart));

        chart.setEntryLabelTextSize(15f);

        chart.setHoleRadius(15);
        chart.setTransparentCircleRadius(30);

        chart.setUsePercentValues(true);
        chart.setRotationEnabled(false);
        chart.getDescription().setEnabled(false);

        Legend l = chart.getLegend();
        l.setYEntrySpace(10f);
        l.setWordWrapEnabled(true);

            /*LegendEntry l1 = new LegendEntry("l1", Legend.LegendForm.CIRCLE, 8f, 2, null, Color.RED);
            LegendEntry l2 = new LegendEntry("l2", Legend.LegendForm.DEFAULT, 10f, 2, null, Color.BLACK);
            LegendEntry l3 = new LegendEntry("l3", Legend.LegendForm.SQUARE, 12f, 2, null, Color.BLUE);
            LegendEntry l5 = new LegendEntry("l3", Legend.LegendForm.LINE, 14f, 2, null, Color.GREEN);
            */

        l.setTextSize(24f);
        l.setEnabled(true);


        chart.setData(d);
        chart.invalidate();


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<PieEntry> GeneratePieEntryData(String[] categories_to_compare, Long timeframe){

        List<PieEntry> data = new ArrayList<>();

        LocalDateTime _fourWeeksAgo = LocalDateTime.now().minusWeeks(timeframe);
        ArrayList<TrainingInstance> _filteredTrainingInstances = TrainingInstance.filterInstancesByCategories(MainActivity.get_allTrainingInstances(), categories_to_compare);
        LocalDateTime filter_timeframe = LocalDateTime.now().minusWeeks(timeframe);
        _filteredTrainingInstances = TrainingInstance.filterInstancesByDate(_filteredTrainingInstances, filter_timeframe);

        if(!_filteredTrainingInstances.isEmpty()){
            HashMap<String, Float> _categoryOccurences = new HashMap<>();

            for(TrainingInstance i : _filteredTrainingInstances){
                if(_categoryOccurences.get(i.getCategory()) != null) {
                    _categoryOccurences.put(i.getCategory(), _categoryOccurences.get(i.getCategory()) + 1f);
                } else {
                    _categoryOccurences.put(i.getCategory(), 1f);
                }
            }

            float _amountOfTrainingInstances = _filteredTrainingInstances.size();
            for(String key : _categoryOccurences.keySet()){
                float v = (_categoryOccurences.get(key) / _amountOfTrainingInstances);
                data.add(new PieEntry(v, key));
            }
        }
        return data;
    }
}
