package com.example.tiktak;

import android.content.Context;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

@SuppressWarnings("deprecation")
public class PagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES= new int[]{R.string.Search,R.string.Profile,R.string.Subscribe};
    private final Context mContext;



    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment= new SearchFragment();
                break;
            case 1:
                fragment= new ProfileFragment();
                break;
            case 2:
                fragment= new SubscribeFragment();
                break;
        }
        return fragment;


    }

    @Override
    public int getCount() {
        return 3;
    }




    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Search";
            case 1:
                return "Profile";
            case 2:
                return "Subscribe";
        }
        return null;
        }
    }

