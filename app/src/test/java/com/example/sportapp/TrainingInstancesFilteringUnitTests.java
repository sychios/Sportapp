package com.example.sportapp;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TrainingInstancesFilteringUnitTests {
    ArrayList<TrainingInstance> instances;

    @Before
    @Test
    public void GenerateList(){
        instances = TrainingInstance.CreateRandom(30, true);
    }

    @Test
    public void FilterByDateAndCategory(){
        instances.sort(TrainingInstance::compareTo);
        for(TrainingInstance i : instances){
            if(i.getCategory() == "Laufen" | i.getCategory() == "Burpees"){
                System.out.println(i.ToStringWithMinutesAndWeek());
            }
        }
        LocalDateTime lowerBound = LocalDateTime.of(2021, 1, 10, 15,15);
        ArrayList<TrainingInstance> filtered = TrainingInstance.filterInstancesByCategories(instances, new String[] {"Laufen", "Burpees"});

        System.out.println("################# FILTERED #################");
        for(TrainingInstance i : filtered){
            System.out.println(i.ToStringWithMinutesAndWeek() + " " + i.getCategory());
        }
    }
}
