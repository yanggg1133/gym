package com.hxs.fitnessroom.module.home.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.home.model.entity.AreaBean;
import com.hxs.fitnessroom.util.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * 用于首页选择的门店查询条件
 * 城市或地区条件选择弹出框
 * Created by je on 9/7/17.
 */

public class AreaSelectDialogFragment extends DialogFragment implements View.OnClickListener
{
    public static final int SELECT_TYPE_CITY = 1;
    public static final int SELECT_TYPE_COUNTY = 2;
    private int mSelectType;
    private List<AreaBean> mAreas;
    private int mCurrentCityIndex;
    private int mCurrentCountyIndex;
    private View cancel_action;
    private View confirm_action;


    private void setSelectType(int selectType)
    {
        mSelectType = selectType;
    }

    private void setOnSelectCallBack(OnSelectCallBack onSelectCallBack)
    {
        this.mOnSelectCallBack = onSelectCallBack;
    }

    private void setAreas(int currentCityIndex, int currentCountyIndex, List<AreaBean> areas)
    {
        mCurrentCityIndex = currentCityIndex;
        mCurrentCountyIndex = currentCountyIndex;
        mAreas = areas;
    }

    @IntDef({SELECT_TYPE_CITY, SELECT_TYPE_COUNTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SELECT_TYPE
    {
    }

    ;

    public static void show(FragmentManager fragmentManager,
                            @SELECT_TYPE int selectType,
                            int currentCityIndex,
                            int currentCountyIndex,
                            List<AreaBean> areaBeen,
                            OnSelectCallBack onSelectCallBack)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(AreaSelectDialogFragment.class.getSimpleName());
        if (prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        AreaSelectDialogFragment newFragment = new AreaSelectDialogFragment();
        newFragment.setOnSelectCallBack(onSelectCallBack);
        newFragment.setAreas(currentCityIndex, currentCountyIndex, areaBeen);
        newFragment.setSelectType(selectType);
        newFragment.show(ft, AreaSelectDialogFragment.class.getSimpleName());

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.widget_area_select_dialog, container, false);
    }

    private NumberPickerView picker_area;
    private View dialog_background;
    private View dialog_widget_layout;

    private OnSelectCallBack mOnSelectCallBack;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        picker_area = (NumberPickerView) view.findViewById(R.id.picker_area);
        dialog_background = view.findViewById(R.id.dialog_background);
        dialog_widget_layout = view.findViewById(R.id.dialog_widget_layout);
        confirm_action = view.findViewById(R.id.confirm_action);
        cancel_action = view.findViewById(R.id.cancel_action);
        initView();
        windowInAnimate();
    }


    private void windowInAnimate()
    {
        dialog_background.animate().alpha(0.3f).setInterpolator(new FastOutSlowInInterpolator()).setDuration(400).start();
        dialog_widget_layout.animate().alpha(1f).setInterpolator(new FastOutSlowInInterpolator()).translationYBy(-ViewUtil.dpToPx(30, getContext())).setDuration(400).start();
    }

    private void windowOutAnimate()
    {
        dialog_background.animate().alpha(0.0f).setInterpolator(new FastOutSlowInInterpolator()).setDuration(300).start();
        dialog_widget_layout.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                dismiss();
            }
        }).setInterpolator(new FastOutSlowInInterpolator()).translationYBy(ViewUtil.dpToPx(60, getContext())).setDuration(400).start();
    }

    private void initView()
    {
        cancel_action.setOnClickListener(this);
        confirm_action.setOnClickListener(this);
        dialog_background.setOnClickListener(this);
        if (mSelectType == SELECT_TYPE_CITY)
        {
            initCity();
        } else
        {
            initCounty();
        }

    }

    private void initCounty()
    {
        String[] oldCounty = mAreas.get(mCurrentCityIndex).county;
        String[] countyArray = new String[oldCounty.length+1];
        countyArray[0] = "全部";
        for (int i = 1; i < countyArray.length; i++)
        {
            countyArray[i] = oldCounty[i-1];
        }
        picker_area.setDisplayedValues(countyArray);
        picker_area.setMinValue(0);
        picker_area.setMaxValue(countyArray.length - 1);
        picker_area.setValue(mCurrentCountyIndex + 1);

    }

    private void initCity()
    {
        String[] cityArray = new String[mAreas.size()];
        for (int i = 0; i < cityArray.length; i++)
        {
            cityArray[i] = mAreas.get(i).city;
        }
        picker_area.setDisplayedValues(cityArray);
        picker_area.setMinValue(0);
        picker_area.setMaxValue(cityArray.length - 1);
        picker_area.setValue(mCurrentCityIndex);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.dialog_background:
            case R.id.cancel_action:
                windowOutAnimate();
                break;
            case R.id.confirm_action:
                if(mSelectType == SELECT_TYPE_CITY)
                {
                    mCurrentCityIndex = picker_area.getValue();
                }
                else
                {
                    mCurrentCountyIndex = picker_area.getValue() - 1;
                }
                mOnSelectCallBack.onSelect(mCurrentCityIndex,mCurrentCountyIndex);
                windowOutAnimate();
                break;
        }
    }


    /**
     * 选择后返回
     */
    public interface OnSelectCallBack
    {
        void onSelect(int cityIndex, int countyIndex);
    }
}
