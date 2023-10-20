package com.example.sportapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TrainingViewHolder>{
    private Context context = MainActivity.getAppContext();
    private ArrayList<TrainingInstance> trainingInstances;

    // Laufen,Yoga,Übungen,Burpees,AeroCap,AeroPow, AnCap, AnPow, Strength, Seilklettern, Bouldern

    private HashMap<String, int[]> _colorsForCategory = new HashMap<>();

    public static class TrainingViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rv_element;
        TextView trainingInstanceCategory;
        TextView trainingInstanceDuration;
        TextView trainingInstanceDate;

        TrainingViewHolder(View itemView){
            super(itemView);
            rv_element = itemView.findViewById(R.id.rv_element);
            trainingInstanceCategory = itemView.findViewById(R.id.rv_trainingInstanceCategory);
            trainingInstanceDuration = itemView.findViewById(R.id.rv_trainingInstanceDuration);
            trainingInstanceDate = itemView.findViewById(R.id.rv_trainingInstanceDate);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    RVAdapter(){
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set up color Hashmap
        _colorsForCategory.put("Laufen", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Laufen), ContextCompat.getColor(context, R.color.rv_textcolor_light)});
        _colorsForCategory.put("Yoga", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Yoga), ContextCompat.getColor(context, R.color.rv_textcolor_dark)});
        _colorsForCategory.put("Übungen", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Übungen), ContextCompat.getColor(context, R.color.rv_textcolor_light)});
        _colorsForCategory.put("Burpees", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Burpees), ContextCompat.getColor(context, R.color.rv_textcolor_light)});
        _colorsForCategory.put("AeroCap", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_AeroCap), ContextCompat.getColor(context, R.color.rv_textcolor_light)});
        _colorsForCategory.put("AeroPow", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_AeroPow), ContextCompat.getColor(context, R.color.rv_textcolor_light)});
        _colorsForCategory.put("AnCap", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_AnCap), ContextCompat.getColor(context, R.color.rv_textcolor_dark)});
        _colorsForCategory.put("AnPow", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_AnPow), ContextCompat.getColor(context, R.color.rv_textcolor_light)});
        _colorsForCategory.put("Strength", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Strength), ContextCompat.getColor(context, R.color.rv_textcolor_dark)});
        _colorsForCategory.put("Seilklettern", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Seilklettern), ContextCompat.getColor(context, R.color.rv_textcolor_dark)});
        _colorsForCategory.put("Bouldern", new int[]{ContextCompat.getColor(context, R.color.rv_bgcolor_Bouldern), ContextCompat.getColor(context, R.color.rv_textcolor_dark)});


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_instance , parent, false);
        TrainingViewHolder tvh = new TrainingViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {

        String instanceCategory = trainingInstances.get(position).getCategory();
        // [backgroundcolor, textcolor]
        int colors[];
        try{
            colors = _colorsForCategory.get(instanceCategory);

            holder.rv_element.setBackgroundColor(colors[0]);
            holder.trainingInstanceCategory.setTextColor(colors[1]);
            holder.trainingInstanceDuration.setTextColor(colors[1]);
            holder.trainingInstanceDate.setTextColor(colors[1]);

            holder.trainingInstanceCategory.setText(trainingInstances.get(position).getCategory());
        } catch(NullPointerException e){
            colors = _colorsForCategory.get("Laufen");

            holder.rv_element.setBackgroundColor(colors[0]);
            holder.trainingInstanceCategory.setTextColor(colors[1]);
            holder.trainingInstanceDuration.setTextColor(colors[1]);
            holder.trainingInstanceDate.setTextColor(colors[1]);


            holder.trainingInstanceCategory.setText("Error: Not Found");
        }

        holder.trainingInstanceDuration.setText(trainingInstances.get(position).getDurationForRV());
        holder.trainingInstanceDate.setText(trainingInstances.get(position).getDateForRV());
    }

    @Override
    public int getItemCount() {
        return trainingInstances.size();
    }

    public void SetItems(ArrayList<TrainingInstance> mInstances){
        this.trainingInstances = mInstances;
    }

    public Context getContext(){return MainActivity.getAppContext();}

    public void DeleteItem(int position){
        trainingInstances.remove(position);
        notifyItemRemoved(position);

        MemoryAccess.WriteTrainingInstancesListToMemory(trainingInstances);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddItem(TrainingInstance instance){
        Log.e("AddItem: ", instance.ToStringWithMinutesAndWeek());
        trainingInstances.add(instance);
        trainingInstances.sort(TrainingInstance::compareTo);

        int position = trainingInstances.indexOf(instance);

        notifyItemInserted(position);
        notifyItemChanged(position);

        //MainActivity.AddTrainingInstance(instance);
        MemoryAccess.AppendTrainingInstancesToMemory(instance);
    }

    public ArrayList<TrainingInstance> getTrainingInstances(){
        return trainingInstances;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView, TrainingInstance instance){
        final int adapterPosition = viewHolder.getLayoutPosition();
        DeleteItem(adapterPosition);

        Snackbar snackbar = Snackbar
                .make(recyclerView, "Training entfernt!", Snackbar.LENGTH_LONG)
                .setAction("Rückgängig machen", v -> AddItem(instance));
        snackbar.show();
    }


}
