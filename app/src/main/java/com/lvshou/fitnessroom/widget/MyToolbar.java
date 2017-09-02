package com.lvshou.fitnessroom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.lvshou.fitnessroom.R;

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
    }
}
