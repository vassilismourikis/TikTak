package com.example.tiktak;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

@SuppressWarnings("deprecation")
public class PagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArraylist = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();



    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm,numOfTabs);

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragmentArraylist.get(position);

    }

    @Override
    public int getCount() {
        return fragmentArraylist.size();
    }


    public void addFragment(Fragment fragment,String title){
        fragmentArraylist.add(fragment);
        fragmentTitle.add(title);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
