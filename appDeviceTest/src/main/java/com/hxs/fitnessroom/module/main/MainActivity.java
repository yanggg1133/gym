package com.hxs.fitnessroom.module.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.sports.SportsMainFragment;
import com.hxs.fitnessroom.module.user.UserMainFragment;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.home.StoreListFragment;

import java.util.ArrayList;
import java.util.List;

import fitnessroom.hxs.com.singesdk.MessageReceiver;


/**
 * APP 主入口 Activity
 *
 * @author shaojunjie on 9/2/17
 * @Email fgnna@qq.com
 */
public class MainActivity extends BaseActivity
{
    public static final String KEY_INDEX = "KEY_INDEX";

    public static final int INDEX_STORE = R.id.navigation_home;
    public static final int INDEX_SPORT = R.id.navigation_scan_code;
    public static final int INDEX_USER = R.id.navigation_user;
    private View new_message_icon;

    public static final Intent getNewIntent(Context context)
    {
        return getNewIntent(context,INDEX_STORE);
    }
    public static final Intent getNewIntent(Context context,int index)
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_INDEX,index);
        return intent;
    }

    private BottomNavigationView navigation;
    private Fragment mNowShowFragment;
    private List<Fragment> mFragmentStack = new ArrayList<>();
    private StoreListFragment mStoreListFragment;
    private SportsMainFragment mSportsMainFragment;
    private UserMainFragment mUserMainFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    if (null == mStoreListFragment)
                    {
                        mStoreListFragment = new StoreListFragment();
                        addFragment(mStoreListFragment, fragmentTransaction);
                    } else
                    {
                        showFragment(mStoreListFragment, fragmentTransaction);
                    }
                    return true;
                case R.id.navigation_scan_code:
                    if (null == mSportsMainFragment)
                    {
                        mSportsMainFragment = new SportsMainFragment();
                        addFragment(mSportsMainFragment, fragmentTransaction);
                    } else
                    {
                        showFragment(mSportsMainFragment, fragmentTransaction);
                    }
                    return true;
                case R.id.navigation_user:
                    if (null == mUserMainFragment)
                    {
                        mUserMainFragment = new UserMainFragment();
                        addFragment(mUserMainFragment, fragmentTransaction);
                    } else
                    {
                        showFragment(mUserMainFragment, fragmentTransaction);
                    }
                    return true;
            }
            return false;
        }


    };

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        int index = getIntent().getIntExtra(KEY_INDEX,0);
        navigation.setSelectedItemId(index);
        if(index == INDEX_SPORT)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mSportsMainFragment.startSport();
                }
            },500);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState != null)
        {
            mFragmentStack.clear();
            mStoreListFragment = (StoreListFragment) getSupportFragmentManager().findFragmentByTag(StoreListFragment.class.getSimpleName());
            mSportsMainFragment = (SportsMainFragment) getSupportFragmentManager().findFragmentByTag(SportsMainFragment.class.getSimpleName());
            mUserMainFragment = (UserMainFragment) getSupportFragmentManager().findFragmentByTag(UserMainFragment.class.getSimpleName());
            addToStack(mStoreListFragment);
            addToStack(mSportsMainFragment);
            addToStack(mUserMainFragment);
        }
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        setupNavigation();

        new_message_icon = findViewById(R.id.new_message_icon);
        if(MessageReceiver.isHasNewMessage(this))
            showNewMessageIcon(true);
        registerNewMessageBroadcastReceiver();
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
        navigation.post(new Runnable()
        {
            @Override
            public void run()
            {
                navigation.setSelectedItemId(R.id.navigation_home);
            }
        });
    }


    private void addFragment(Fragment fragment, FragmentTransaction fragmentTransaction)
    {
        fragmentTransaction.add(R.id.m_home_fragment, fragment, fragment.getClass().getSimpleName());
        mFragmentStack.add(fragment);
        mNowShowFragment = fragment;
        showFragment(fragment, fragmentTransaction);
    }

    private void addToStack(Fragment fragment)
    {
        if (fragment != null)
        {
            mFragmentStack.add(fragment);
        }
    }


    private void showFragment(Fragment currentFragment, FragmentTransaction fragmentTransaction)
    {

        for (int i = 0, n = mFragmentStack.size(); i < n; i++)
        {
            if (mFragmentStack.get(i).equals(currentFragment))
            {
                fragmentTransaction.show(mFragmentStack.get(i));
            } else
            {
                fragmentTransaction.hide(mFragmentStack.get(i));
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        mNowShowFragment = currentFragment;
    }

    private long exitTime = 0;

    //退出确认
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                exitTime = System.currentTimeMillis();
            } else
            {
                moveTaskToBack(false);
                return true;
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onNewMessage()
    {
        showNewMessageIcon(true);
    }

    public void showNewMessageIcon(boolean isShow)
    {
        new_message_icon.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

}
