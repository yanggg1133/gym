package com.hxs.fitnessroom.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.widget.SettingItemView;

/**
 * 预约列表的单个详情页
 * Created by shaojunjie on 17-11-6.
 */

public class UserReserveItemFragment extends BaseFragment
{

    private BaseUi mUi;
    private SettingItemView store_address;
    private SettingItemView time;
    private SettingItemView money;

    public static Fragment getNewFragment()
    {
        return new UserReserveItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.user_reserve_item_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        mUi = new BaseUi(this);
        store_address = mUi.findViewById(R.id.store_address);
        time = mUi.findViewById(R.id.time);
        money = mUi.findViewById(R.id.money);

        store_address.setNoTintIcon(R.mipmap.card_dingwei);
        store_address.hideLine();
        time.setNoTintIcon(R.mipmap.card_time);
        time.hideLine();
        time.hideRightGotoIcon();
        money.setNoTintIcon(R.mipmap.card_jinqian);
        money.hideRightGotoIcon();
        money.hideLine();
    }
}
