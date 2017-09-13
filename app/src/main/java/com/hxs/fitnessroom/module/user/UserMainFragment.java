package com.hxs.fitnessroom.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.module.user.ui.UserMainUi;



/**
 * 我的 主界面
 * Created by je on 9/2/17.
 */

public class UserMainFragment extends BaseFragment implements View.OnClickListener
{
    private UserMainUi mUserMainUi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.user_main_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mUserMainUi = new UserMainUi(this);

        mUserMainUi.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_avatar:
                startActivity(WelcomeActivity.getNewIntent(v.getContext()));
                break;
        }
    }
}
