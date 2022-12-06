package com.example.khataregister;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdaptor extends FragmentStateAdapter {

    String [] titles = {"Profile", "Details", "Analytics"};


    public FragmentAdaptor(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                DetailsFragment detailsFragment = new DetailsFragment();
                return detailsFragment;
            case 2:
                AnalyticsFragment analyticsFragment = new AnalyticsFragment();
                return analyticsFragment;
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}