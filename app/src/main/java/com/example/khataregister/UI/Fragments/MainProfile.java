package com.example.khataregister.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.khataregister.Adaptor.FragmentAdaptor;
import com.example.khataregister.R;
import com.google.android.material.tabs.TabLayout;


public class MainProfile extends AppCompatActivity {

    TabLayout tabLayout;
    public static ViewPager2 viewPager;
    static FragmentAdaptor adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        viewPager=(ViewPager2)findViewById(R.id.view_pager);


        adapter = new FragmentAdaptor(this);
        viewPager.setAdapter(adapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

}