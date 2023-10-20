package com.example.sportapp;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class MemoryAccess{
    private static String TRAININGHISTORY_FILENAME = "training_history";
    private static String CATEGORIES_FILENAME = "categories";

    private MemoryAccess(){}

    /**
     * Accesses Shared Preferences to read Training Instances as String and converts
     * to ArrayList of TrainingInstances.
     *
     * @return Sorted ArrayList of Training Instances
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TrainingInstance> ReadTrainingInstancesListFromMemory(){

        ArrayList<TrainingInstance> instances = new ArrayList<>();

        SharedPreferences prefs = MainActivity.preferences;

        String[] splitArr = prefs.getString(TRAININGHISTORY_FILENAME, "null").split("~");

        for(int i = 0; i < splitArr.length; i++){
                    if(! (splitArr[i].length() == 0))
                        instances.add(TrainingInstance.InstanceFromDataFormat(splitArr[i]));
        }
        instances.sort(TrainingInstance::compareTo);
        return instances;
    }

    public static void WriteTrainingInstancesListToMemory(ArrayList<TrainingInstance> mAllTrainingInstances){
        SharedPreferences prefs = MainActivity.preferences;
        SharedPreferences.Editor editor = prefs.edit();

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < mAllTrainingInstances.size(); i++){
            sb.append(mAllTrainingInstances.get(i).InstanceToDataFormat());
            sb.append("~");
        }

        String allInstancesString = sb.toString();

        editor.putString(TRAININGHISTORY_FILENAME, allInstancesString);
        editor.apply();
    }

    public static void AppendTrainingInstancesToMemory(TrainingInstance ti){
        SharedPreferences prefs = MainActivity.preferences;
        SharedPreferences.Editor editor = prefs.edit();

        String newTrainingInstanceList = prefs.getString(TRAININGHISTORY_FILENAME, "null") +
                ti.InstanceToDataFormat() +
                "~";

        editor.putString(TRAININGHISTORY_FILENAME, newTrainingInstanceList);
        editor.apply();
    }

    public static boolean UpdateCategoriesToMemory(ArrayList<String> mCategories){
        throw new UnsupportedOperationException();
    }

    public static ArrayList<String> ReadCategoriesFromMemory(){
        throw new UnsupportedOperationException();
    }
}
