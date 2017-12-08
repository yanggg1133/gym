package com.hxs.fitnessroom.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.ViewUtil;


/**
 * 公用的界面顶栏
 * Created by je on 9/1/17.
 */
public class MyToolbar extends Toolbar
{
    private TextView toolbar_title;
    private ViewGroup toolbar_layout;
    private ImageView toolbar_left_back;
    private TextView toolbar_right_text_button;

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

    private void initializa()
    {
        setContentInsetsAbsolute(0,0);
        inflate(getContext(),R.layout.my_toolbar_inner,this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            setElevation(ViewUtil.dpToPx(0,getContext()));
        }
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_right_text_button = (TextView) findViewById(R.id.toolbar_right_text_button);
        toolbar_left_back = (ImageView) findViewById(R.id.toolbar_left_back);
        toolbar_layout = (ViewGroup) findViewById(R.id.toolbar_layout);
    }

    public void setTitle(String title)
    {
        toolbar_title.setText(title);
    }

    public void setTitleColor(@ColorInt int color)
    {
        toolbar_title.setTextColor(color);
    }

    public void setBackground(Drawable background)
    {
        toolbar_layout.setBackground(background);
    }

    public void setBack(boolean canBack,@Nullable OnClickListener onClickListener)
    {
        if(canBack)
            toolbar_left_back.setVisibility(VISIBLE);
        else
            toolbar_left_back.setVisibility(GONE);
        toolbar_left_back.setOnClickListener(onClickListener);
    }

    /**
     * 设置 右侧按钮的文字及点击事件
     * 按钮ID: toolbar_right_text_button
     *
     * @param text
     * @param onClickListener
     */
    public void setRightTextButton(String text, View.OnClickListener onClickListener)
    {
        toolbar_right_text_button.setVisibility(VISIBLE);
        toolbar_right_text_button.setText(text);
        toolbar_right_text_button.setOnClickListener(onClickListener);
    }

}
