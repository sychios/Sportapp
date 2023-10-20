package com.example.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatisticsActivity extends AppCompatActivity {

    public static SharedPreferences preferences;

    private Button returnButton;
    private TabLayout tabLayout;
    private TabLayout tabLayoutDots;
    private ViewPager2 viewPager;

    private PieChart chart;

    // tab titles
    private String[] modes = new String[]{"7 Tage", "4 Wochen", "10 Wochen"};

    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);

        returnButton = findViewById(R.id.statistics_return_button);
        returnButton.setOnClickListener(v -> startActivity(new Intent(StatisticsActivity.this, MainActivity.class)));

        tabLayout = findViewById(R.id.statistics_tab_layout);
        tabLayoutDots = findViewById(R.id.dots_tab_layout);
        viewPager = findViewById(R.id.statistics_pager);

        init();
    }

    private void init(){
        getSupportActionBar().setElevation(0);

        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));
        viewPager.registerOnPageChangeCallback(pageChangeCallback);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(modes[position])).attach();
        new TabLayoutMediator(tabLayoutDots, viewPager, (tab, position) -> tab.setText("")).attach();


    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {
        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 1:
                    return new StatisticsFragmentFourWeeks();
                case 2:
                    return new StatisticsFragmentTenWeeks();
                default:
                    return new StatisticsFragmentUserDefinedComparison();
            }
        }

        @Override
        public int getItemCount() {
            return modes.length;
        }
    }

}