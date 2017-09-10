package com.hxs.fitnessroom.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.widget.AreaSelectDialogFragment;
import com.hxs.fitnessroom.module.home.widget.RulerViewLayout;
import com.hxs.fitnessroom.module.home.widget.ruler.VerticalScaleView;
import com.hxs.fitnessroom.module.pay.mode.RechargeModel;
import com.hxs.fitnessroom.module.pay.mode.entity.RechargeBean;

import java.util.ArrayList;

import fitnessroom.hxs.com.paylib.PayFactory;



/**
 * 我的 主界面
 * Created by je on 9/2/17.
 */

public class UserMainFragment extends BaseFragment
{
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
        final TextView textView = findViewById(R.id.body_height);
        RulerViewLayout rulerViewLayout = findViewById(R.id.rulerViewLayout);
        rulerViewLayout.setOnChengedListener(new RulerViewLayout.OnChengedListener()
        {
            @Override
            public void onChenged(int height)
            {
                textView.setText("身高"+height+"cm");
            }
        });
//        textView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//                AreaSelectDialogFragment.show(getFragmentManager(), new ArrayList<String>());
//
//            }
//        });

    }
}
