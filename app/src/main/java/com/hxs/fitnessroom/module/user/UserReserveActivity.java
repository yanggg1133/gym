package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;

/**
 * 我的预约
 * Created by shaojunjie on 17-11-6.
 */

public class UserReserveActivity extends BaseActivity
{
    private BaseUi mUi;
    private ViewPager viewPager;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,UserReserveActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reserve);
        mUi = new BaseUi(this);
        mUi.setTitle("我的预约");
        mUi.setBackAction(true);
        viewPager = mUi.findViewById(R.id.viewpager);

        doWork();
    }

    private void doWork()
    {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return 3;
            }

            @Override
            public Fragment getItem(int position)
            {
                return UserReserveItemFragment.getNewFragment();
            }

        });
    }
}
