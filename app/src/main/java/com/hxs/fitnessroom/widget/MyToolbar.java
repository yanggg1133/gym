package com.hxs.fitnessroom.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.ViewUitl;


/**
 * 公用的界面顶栏
 * Created by je on 9/1/17.
 */
public class MyToolbar extends Toolbar
{
    public MyToolbar(Context context)
    {
        this(context, null);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializa();
    }

//    android:layout_height="?attr/actionBarSize"
//    android:background="?attr/colorPrimary"
//    android:elevation="4dp"
//    android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
//    app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
//    app:contentInsetStart="0dp"
    private void initializa()
    {
        setContentInsetsAbsolute(0,0);
        inflate(getContext(),R.layout.my_toolbar_inner,this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            setElevation(ViewUitl.dpToPx(0,getContext()));
        }
        setBackgroundResource(R.color.colorPrimary);
    }
}
