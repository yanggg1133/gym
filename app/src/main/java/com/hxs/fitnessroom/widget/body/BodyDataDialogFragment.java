package com.hxs.fitnessroom.widget.body;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.widget.MyToolbar;

/**
 * 选择 性别 身高 出生日期生的弹框控件
 * Created by je on 9/7/17.
 */

public class BodyDataDialogFragment extends DialogFragment implements View.OnClickListener
{

    public static void show(FragmentManager fragmentManager)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(BodyDataDialogFragment.class.getSimpleName());
        if (prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = BodyDataDialogFragment.newInstance();
        newFragment.show(ft, BodyDataDialogFragment.class.getSimpleName());
    }

    private static BodyDataDialogFragment newInstance()
    {
        BodyDataDialogFragment f = new BodyDataDialogFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.widget_body_data_dialog, container, false);
    }

    private RulerViewLayout rulerViewLayout;

    private TextView body_height_text;
    private View boy_button;
    private View girl_button;
    private View next_button;
    private View body_image;
    private MyToolbar toolbar;

    @UserBean.SexType
    private int mSex = UserBean.SEX_TYPE_GIRL;
    private int mBodyHeight = 160;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rulerViewLayout = (RulerViewLayout) view.findViewById(R.id.rulerViewLayout);
        boy_button =  view.findViewById(R.id.boy_button);
        girl_button =  view.findViewById(R.id.girl_button);
        next_button =  view.findViewById(R.id.next_button);
        body_image =  view.findViewById(R.id.body_image);
        toolbar = (MyToolbar) view.findViewById(R.id.toolbar);
        body_height_text = (TextView) view.findViewById(R.id.body_height_text);

        toolbar.setTitle("身高");
        boy_button.setOnClickListener(this);
        girl_button.setOnClickListener(this);
        next_button.setOnClickListener(this);

        rulerViewLayout.setHeight(mBodyHeight);
        rulerViewLayout.setOnChengedListener(new RulerViewLayout.OnChengedListener()
        {
            @Override
            public void onChenged(int height)
            {
                body_height_text.setText(String.valueOf(height));
                mBodyHeight = height;
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.boy_button:
                body_image.setBackgroundResource(R.drawable.boy);
                boy_button.setBackgroundResource(R.drawable.bg_gradient_d068ff_e452b1);
                girl_button.setBackground(null);
                break;
            case R.id.girl_button:
                body_image.setBackgroundResource(R.drawable.girl);
                girl_button.setBackgroundResource(R.drawable.bg_gradient_d068ff_e452b1);
                boy_button.setBackground(null);
                break;
            case R.id.next_button:
                toolbar.setTitle("出生年月");
                break;
        }
    }
}
