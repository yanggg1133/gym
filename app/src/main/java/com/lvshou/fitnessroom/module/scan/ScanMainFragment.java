package com.lvshou.fitnessroom.module.scan;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lvshou.fitnessroom.R;
import com.lvshou.fitnessroom.base.baseclass.BaseFragment;

/**
 * 扫一扫主入口界面
 * Created by je on 9/2/17.
 */

public class ScanMainFragment extends BaseFragment
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.scan_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
