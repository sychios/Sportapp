package com.example.sportapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class providing the instances in which the sport-activities are saved
 */
public class TrainingInstance implements Comparable<TrainingInstance> {
    // dd.MM.yyyy -> day.month.year
    // --H:mm:ss -> hours:minutes:seconds
    // --w -> calendar week
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss w";

    private String date;
    private int duration;
    private String category;
    private String identifier;


    /**
     *
     * @param o Traininginstance to be compared to with this Traininginstance
     * @return
     *          - -1: this < other := this is older than other
     *          - 0: this == other := same Date
     *          - 1: this > other := this is younger than other
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(TrainingInstance o) {
        return CompareDates(this.getDate(), o.getDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int CompareDates(String pThisDate, String pOtherDate){
        LocalDateTime thisDateTime = LocalDateTime.parse(pThisDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
        LocalDateTime otherDateTime = LocalDateTime.parse(pOtherDate, DateTimeFormatter.ofPattern(DATE_FORMAT));

        if(thisDateTime.isEqual(otherDateTime))
            return 0;
        else
            return thisDateTime.isAfter(otherDateTime) ? 1 : -1;
    }


    /**
     *
     * @param pCategory
     * @param pDuration
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TrainingInstance(String pCategory, int pDuration) {
            duration = pDuration;
            category = pCategory;
            // TODO: Unable to obtain LocalDate from TemporalAccessor ".." of type java.time.Instant
            date = (LocalDateTime.of(LocalDate.from(Instant.now()), LocalTime.now())).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            identifier = java.util.UUID.randomUUID().toString();
    }

    /**
     *
     * @param pCategory
     * @param pDuration
     * @param pDate
     * @param pIdentifier
     */
    public TrainingInstance(String pCategory, int pDuration, String pDate, String pIdentifier){
        duration = pDuration;
        category = pCategory;
        date = pDate;
        identifier = pIdentifier;
    }

    public String ToStringWithMinutesAndWeek(){
        String[] dateArray = date.split(" ");
        return category + " | " + duration + " Minuten | " + dateArray[0] + " in Woche " + dateArray[2];
    }


    // IDENTIFIER_DATE_CATEGORY_DURATION
    public String InstanceToDataFormat(){
        return this.identifier + "_" +  this.date + "_" + this.category + "_" + this.duration;
    }

    public static TrainingInstance InstanceFromDataFormat(String pInstanceString){
        // Form: <UUID>-DATEFORMAT_<Category>_dd
        // 4f939272-82ec-4327-9acf-106d7c604e05_01.01.2021 13:54:33 53_Yoga_30

        String[] subStrings = pInstanceString.split("_");

        return new TrainingInstance(subStrings[2], Integer.parseInt(subStrings[3]), subStrings[1], subStrings[0]);
    }

    /**
     * Filters @param instances regarding @param d.
     * @param pInstances List of TrainingsInstances
     * @param pLocalDateTime Date
     * @return A filtered List of TrainingsInstances with instances younger than @param d.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TrainingInstance> filterInstancesByDate(ArrayList<TrainingInstance> pInstances, LocalDateTime pLocalDateTime){
        return pInstances
                .stream()
                .filter(i -> CompareDates(i.getDate(), pLocalDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss w"))) > 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Filters @param instances regarding @param d and @param category;
     * @param pInstances List of TrainingInstances
     * @param pLocalDateTime Date
     * @param pCategory Category used for filtering
     * @return A filtered List of TrainingsInstances with instances younger than @param d and the category @param c;
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TrainingInstance> filterInstancesByDateAndCategory(ArrayList<TrainingInstance> pInstances, LocalDateTime pLocalDateTime, String pCategory){
        return pInstances
                .stream()
                .filter(i -> CompareDates(i.getDate(), pLocalDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss w"))) > 0 && i.getCategory().equals(pCategory))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Filters @param instances regarding @param categories.
     * @param pInstances List of TrainingInstances
     * @param pCategories String array
     * @return A sorted list of TrainingInstances
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TrainingInstance> filterInstancesByCategories(ArrayList<TrainingInstance> pInstances, String[] pCategories){
        ArrayList<TrainingInstance> filteredList = new ArrayList<>();
        if(pCategories.length == 0)
            return filteredList;
        for (String c: pCategories) {
            filteredList.addAll(pInstances
                    .stream()
                    .filter(i -> i.getCategory().equals(c))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        filteredList.sort(TrainingInstance::compareTo);
        return filteredList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TrainingInstance> CreateRandom(int pAmount, boolean pIsRandom){
        ArrayList<TrainingInstance> instances = new ArrayList<>();

        String[] cats = {"Laufen", "Yoga", "Übungen", "Burpees", "AeroCap", "AeroPow", "AnCap", "AnPow", "Strength", "Seilklettern", "Bouldern"};

        int index;

        for(int i = 0; i<pAmount; i++){
            Random random = new Random();
            int minDay = (int) LocalDate.of(2021, 1, 1).toEpochDay();
            LocalDate month_day = LocalDate.now();
            int maxDay = (int) LocalDate.of(2021, month_day.getMonthValue(), month_day.getDayOfMonth()).toEpochDay();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

            LocalDateTime ldt = LocalDateTime.of(randomDate, LocalTime.now());
            String formattedString = ldt.format(DateTimeFormatter.ofPattern(DATE_FORMAT));

            index = pIsRandom? random.nextInt(cats.length) : i % (cats.length);
            instances.add(new TrainingInstance(cats[index], 30, formattedString,java.util.UUID.randomUUID().toString()));
        }

        return instances;
    }

    public String getDate() { return date; }

    public int getDuration() { return duration;}

    public String getIdentifier() { return identifier; }

    public String getCategory(){
        return category;
    }

    public String getDurationForRV(){
        return  duration + "min, " + "  ";
    }

    public String getDateForRV(){ return date.split(" ")[0]; }

    public int getCalendarWeek(){return Integer.parseInt(date.split(" ")[2]); }
}
