package com.lvshou.fitnessroom.module.home;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvshou.fitnessroom.R;
import com.lvshou.fitnessroom.base.baseclass.BaseFragment;

import static com.lvshou.fitnessroom.R.id.longitude;

/**
 * 首页界面
 * Created by je on 9/2/17.
 */

public class HomeMainFragment extends BaseFragment
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.home_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        //经度
        TextView latitude = (TextView) findViewById(R.id.latitude);
        //纬度
        TextView longitude = (TextView) findViewById(R.id.longitude);
        Location location = new Location(LocationManager.GPS_PROVIDER);
        latitude.setText(location.getLatitude()+"");
        longitude.setText(location.getLongitude()+"");

    }
}
