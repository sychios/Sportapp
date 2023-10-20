package com.example.sportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences preferences;
    public static String[] categories = {"Laufen", "Yoga", "Übungen", "Burpees", "AeroCap", "AeroPow", "AnCap", "AnPow", "Strength", "Seilklettern", "Bouldern"};

    private static Context context;

    private static ArrayList<TrainingInstance> _allTrainingInstances = new ArrayList<>();

    private Button buttonOptions;
    private Button buttonAddTraining;

    private RecyclerView rv;
    private static RVAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.context = getApplicationContext();
        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_element);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        rv.setLayoutManager(llm);

        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, rv));
        itemTouchHelper.attachToRecyclerView(rv);

        buttonAddTraining = findViewById(R.id.button_add_training);
        buttonAddTraining.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TrainingFormActivity.class)));

        buttonOptions = findViewById(R.id.button_options);
        buttonOptions.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StatisticsActivity.class)));

        //TODO: reading instances from memory throws critical IndexOutOfBounds exception
        //_allTrainingInstances = MemoryAccess.ReadTrainingInstancesListFromMemory();

        _allTrainingInstances = new ArrayList<>();

        // Add random instances
        //_allTrainingInstances = TrainingInstance.CreateRandom(60, true);
        //_allTrainingInstances.sort(TrainingInstance::compareTo);

        adapter.SetItems(_allTrainingInstances);
        adapter.notifyDataSetChanged();
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }

    public static RVAdapter getRVAdapter(){
        return adapter;
    }

    public static void SetTrainingInstances(ArrayList<TrainingInstance> mInstances){
        MemoryAccess.WriteTrainingInstancesListToMemory(_allTrainingInstances);
        _allTrainingInstances = mInstances;
        adapter.notifyDataSetChanged();
    }

    public static ArrayList<TrainingInstance> get_allTrainingInstances(){
        return _allTrainingInstances;
    }
}