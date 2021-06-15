package com.example.tiktak;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;





@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    private final int STORAGE_PERMISSION_CODE = 1;
    Client c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c=new Client("Vasilis");


        tabLayout = findViewById(R.id.tabBar);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
        pagerAdapter.addFragment(new SearchFragment(),"Search");
        pagerAdapter.addFragment(new ProfileFragment(),"Profile");
        pagerAdapter.addFragment(new SubscribeFragment(),"Subscribe");

        getIntent().putExtra("Client", (Parcelable) c);

        viewPager.setAdapter(pagerAdapter);





    }






}