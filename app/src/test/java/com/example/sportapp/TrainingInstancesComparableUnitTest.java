package com.example.sportapp;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TrainingInstancesComparableUnitTest {
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy --H:mm:ss");


    TrainingInstance ti0 = new TrainingInstance("Joggen", 30, "05.03.2021 --14:00:00", "id1");
    TrainingInstance ti1 = new TrainingInstance("Joggen", 30, "05.03.2021 --14:00:00", "id1");
    TrainingInstance ti2 = new TrainingInstance("Joggen", 30, "05.01.2021 --14:00:00", "id1");
    TrainingInstance ti3 = new TrainingInstance("Joggen", 30, "05.01.2019 --14:00:00", "id1");
    TrainingInstance ti4 = new TrainingInstance("Joggen", 30, "05.05.2021 --14:00:00", "id1");
    TrainingInstance ti5 = new TrainingInstance("Joggen", 30, "18.05.2021 --14:00:00", "id1");
    TrainingInstance ti6 = new TrainingInstance("Joggen", 30, "02.05.2021 --14:15:00", "id1");

    TrainingInstance ti7 = new TrainingInstance("Joggen", 30, "02.05.2021 --17:00:00", "id1");
    TrainingInstance ti8 = new TrainingInstance("Joggen", 30, "02.05.2021 --12:00:00", "id1");
    TrainingInstance ti9 = new TrainingInstance("Joggen", 30, "02.05.2021 --14:25:00", "id1");
    TrainingInstance ti10 = new TrainingInstance("Joggen", 30, "02.05.2021 --14:03:00", "id1");


    public static Instant between() {
        long startSeconds = Instant.now().minus(Duration.ofDays(100 * 365)).getEpochSecond();
        long endSeconds = Instant.now().minus(Duration.ofDays(10)).getEpochSecond();
        long random = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);
        return Instant.ofEpochSecond(random);
    }

    @Test
    public void sort(){
        ArrayList<TrainingInstance> i = new ArrayList<>();

        for(int j = 0; j<10; j++){
            i.add(new TrainingInstance("Joggen", 10, format.format(Date.from(between())), ""));
        }

        System.out.println("Vor dem Sortieren: \n");
        for(TrainingInstance elem : i)
            System.out.println(elem.ToStringWithMinutesAndWeek());

        i.sort(TrainingInstance::compareTo);

        System.out.println("\n Nach dem Sortieren: \n");
        for(TrainingInstance elem : i)
            System.out.println(elem.ToStringWithMinutesAndWeek());
    }


    @Test
    public void SameDate(){
        int c = ti0.compareTo(ti1);
        Assert.assertEquals(true, c == 0);
    }

    @Test
    public void OlderYear(){
        int c = ti1.compareTo(ti3);
        Assert.assertEquals(true, c > 0);
    }

    @Test
    public void YoungerYear(){
        int c = ti3.compareTo(ti4);
        Assert.assertEquals(true, c < 0);
    }

    @Test
    public void OlderMonth(){
        int c = ti1.compareTo(ti2);
        Assert.assertEquals(true, c > 0);
    }

    @Test
    public void YoungerMonth(){
        int c = ti1.compareTo(ti4);
        Assert.assertEquals(true, c < 0);
    }

    @Test
    public void OlderDay(){
        int c = ti4.compareTo(ti5);
        Assert.assertEquals(true, c < 0);
    }

    @Test
    public void YoungerDay(){
        int c = ti4.compareTo(ti6);
        Assert.assertEquals(true, c > 0);
    }

    @Test
    public void YoungerHour(){
        int c = ti6.compareTo(ti7);
        Assert.assertEquals(true, c < 0);
    }

    @Test
    public void OlderHour(){
        int c = ti6.compareTo(ti8);
        Assert.assertEquals(true, c > 0);
    }

    @Test
    public void YoungerMinute(){
        int c = ti6.compareTo(ti9);
        Assert.assertEquals(true, c < 0);
    }

    @Test
    public void OlderMinute(){
        int c = ti6.compareTo(ti10);
        Assert.assertEquals(true, c > 0);
    }

}