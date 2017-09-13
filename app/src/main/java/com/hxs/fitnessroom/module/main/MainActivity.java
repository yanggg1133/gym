package com.hxs.fitnessroom.module.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.scan.ScanMainFragment;
import com.hxs.fitnessroom.module.user.UserMainFragment;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.home.StoreListFragment;

/**
 *
 * APP 主入口 Activity
 * @author shaojunjie on 9/2/17
 * @Email fgnna@qq.com
 *
 */
public class MainActivity extends BaseActivity
{

    public static final Intent getNewIntent(Context context)
    {
        return new Intent(context,MainActivity.class);
    }


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
        setContentView(R.layout.main_activity);

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
                        return new StoreListFragment();
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
        /**
         * 底部导航栏状态颜色
         */
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_empty}
        }, new int[]{
                getResources().getColor(R.color.colorMenu),
                getResources().getColor(R.color.colorMenuDark)
        });

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(colorStateList);
        navigation.setItemTextColor(colorStateList);
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
