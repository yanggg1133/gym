package com.hxs.fitnessroom.module.scan;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import fitnessroom.hxs.com.codescan.CameraUtil;



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
        return inflater.inflate(R.layout.scan_main_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = findViewById(R.id.codeScan);
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(CameraUtil.isCameraCanUse()){
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, 10);
                }else{
                    Toast.makeText(view.getContext(),"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        NumberPickerView numberPicker = findViewById(R.id.picker);
        String[] ssss = new String[]{"1990 年","1991 年","1992 年","1993 年","1994 年","1995 年","1996 年"};
        numberPicker.setDisplayedValues(ssss);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(ssss.length-1);
//        NumberPicker numberPicker2 = findViewById(R.id.NumberPicker2);
//        String[] ssss2 = new String[]{"1 月","2 月","3 月","4 月"};
//        numberPicker2.setMinValue(0);
//        numberPicker2.setMaxValue(ssss2.length-1);
//        numberPicker2.setDisplayedValues(ssss2);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(),"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();

    }
}
