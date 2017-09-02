package com.lvshou.fitnessroom.module.main;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.lvshou.fitnessroom.R;
import com.lvshou.fitnessroom.base.baseclass.BaseActivity;
import com.lvshou.fitnessroom.module.home.HomeMainFragment;
import com.lvshou.fitnessroom.module.scan.ScanMainFragment;
import com.lvshou.fitnessroom.module.user.UserMainFragment;
import com.lvshou.fitnessroom.util.LogUtil;

/**
 *
 * APP 主入口 Activity
 * @author shaojunjie on 9/2/17
 * @Email fgnna@qq.com
 *
 */
public class MainActivity extends BaseActivity
{

    private BottomNavigationView navigation;
    private ViewPager viewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            switch (item.getItemId())
            {
                case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_scan_code:
                        viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_user:
                        viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }



    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager();
        setupNavigation();
    }

    private void setupViewPager()
    {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position)
            {
                switch (position)
                {
                    case 0:
                        return new HomeMainFragment();
                    case 1:
                        return new ScanMainFragment();
                    case 2:
                        return new UserMainFragment();
                }
                return null;
            }

            @Override
            public int getCount()
            {
                return 3;
            }
        });

    }

    private void setupNavigation()
    {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_scan_code);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_user);
                        break;
                }

            }
        });
    }

}
