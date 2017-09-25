package com.hxs.fitnessroom.module.main;

import android.Manifest;
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

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.sports.SportsMainFragment;
import com.hxs.fitnessroom.module.user.UserMainFragment;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.home.StoreListFragment;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * APP 主入口 Activity
 *
 * @author shaojunjie on 9/2/17
 * @Email fgnna@qq.com
 */
public class MainActivity extends BaseActivity
{


    public static final Intent getNewIntent(Context context)
    {
        return new Intent(context, MainActivity.class);
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        /**
         * 如果性别为空，清除用户登录状态
         */
        if (HXSUser.isLogin() && UserBean.SEX_TYPE_NULL == HXSUser.getSex())
        {
            HXSUser.signOut();
        }

        requestPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        });

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

    }

    @Override
    public void onPermissionsPass()
    {
        super.onPermissionsPass();
        LocationUtil.refreshLocation();
        new Handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                if (HXSUser.isLogin())
                    startActivity(MainActivity.getNewIntent(MainActivity.this));
                else
                    startActivity(WelcomeActivity.getNewIntent(MainActivity.this));
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
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
        return super.onKeyDown(keyCode, event);
    }

}
