package com.hxs.fitnessroom.module.home.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ViewUtil;

import java.util.ArrayList;

/**
 * 城市或地区条件选择弹出框
 * Created by je on 9/7/17.
 */

public class AreaSelectDialogFragment extends DialogFragment
{

    private View view1;

    // DialogFragment.show() will take care of adding the fragment
    // in a transaction.  We also want to remove any currently showing
    // dialog, so make our own transaction and take care of that here.
    public static void show(FragmentManager fragmentManager, ArrayList<String> areaNames)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(AreaSelectDialogFragment.class.getSimpleName());
        if (prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = AreaSelectDialogFragment.newInstance(areaNames);
        newFragment.show(ft, AreaSelectDialogFragment.class.getSimpleName());
    }


    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    private static AreaSelectDialogFragment newInstance(ArrayList<String> areaNames)
    {
        AreaSelectDialogFragment f = new AreaSelectDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putStringArrayList("areas", areaNames);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mAresNames = getArguments().getStringArrayList("areas");
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFragmentTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.widget_area_select_dialog, container, false);
    }


    private ArrayList<String> mAresNames;
    private RecyclerView area_recyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        area_recyclerView = (RecyclerView) view.findViewById(R.id.area_recyclerView);
        view1 = view.findViewById(R.id.view);
        view1.animate()
                .alpha(1)
                .translationY(-ViewUtil.dpToPx(50, getActivity()))
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(500).start();


    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        view1.animate()
                .alpha(0)
                .translationY(ViewUtil.dpToPx(50, getActivity()))
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(500).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
            }
        }).start();
    }


    @Override
    public void onAttach(Context context)
    {

        super.onAttach(context);

        LogUtil.dClass("onAttach");
    }

    @Override
    public void onDetach()
    {
        LogUtil.dClass("onDetach");
        super.onDetach();
    }

    @Override
    public void dismiss()
    {
        LogUtil.dClass("dismiss");
        super.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        LogUtil.dClass("onDismiss");
        super.onDismiss(dialog);
    }

    @Override
    public void onStop()
    {
        LogUtil.dClass("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        LogUtil.dClass("onDestroyView");
        super.onDestroyView();
    }
}
