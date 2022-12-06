package com.example.khataregister.Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.khataregister.UI.Fragments.AnalyticsFragment;
import com.example.khataregister.UI.Fragments.DetailsFragment;
import com.example.khataregister.UI.Fragments.ProfileFragment;

public class FragmentAdaptor extends FragmentStateAdapter {

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
                ProfileFragment profileFragment1 = new ProfileFragment();
                return profileFragment1;
        }
    }



    @Override
    public int getItemCount() {
        return 3;
    }
}