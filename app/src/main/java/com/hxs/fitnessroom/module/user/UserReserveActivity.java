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
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreAppointment;

import java.util.List;

import cn.com.someday.fgnna.indexview.IndexView;

/**
 * 我的预约
 * Created by shaojunjie on 17-11-6.
 */

public class UserReserveActivity extends BaseActivity implements View.OnClickListener
{
    public BaseUi mUi;
    private ViewPager viewPager;
    private IndexView indexView;
    private View none_data;

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
        indexView = mUi.findViewById(R.id.indexView);
        none_data = mUi.findViewById(R.id.none_data);
        mUi.findViewByIdAndSetClick(R.id.goto_reserve_overlist);
        mUi.findViewByIdAndSetClick(R.id.goto_reserve_overlist2);



        doWork();
    }

    private void doWork()
    {
        mUi.getLoadingView().show();
        new BaseAsyncTask()
        {
            @Override
            protected APIResponse doWorkBackground() throws Exception
            {
                return StoreModel.getStoreAppointmentList(3);
            }

            @Override
            protected void onSuccess(APIResponse data)
            {
                mUi.getLoadingView().hide();
                final APIResponse<List<StoreAppointment>> response = data;

                if(response.data.size() == 0)
                {
                    none_data.setVisibility(View.VISIBLE);
                }
                else
                {
                    none_data.setVisibility(View.GONE);
                    viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
                    {
                        @Override
                        public int getCount()
                        {
                            return response.data.size();
                        }

                        @Override
                        public Fragment getItem(int position)
                        {
                            return UserReserveItemFragment.getNewFragment(response.data.get(position));
                        }

                    });
                    indexView.setupByViewPager(viewPager);
                }
            }
        }.go(this);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goto_reserve_overlist:
            case R.id.goto_reserve_overlist2:
                startActivity(UserReserveOverListActivity.getNewIntent(v.getContext()));
                break;
        }
    }
}
