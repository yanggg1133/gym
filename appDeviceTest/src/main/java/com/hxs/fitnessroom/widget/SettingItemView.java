package com.hxs.fitnessroom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 设置栏控件
 * 参考个人中心的跳转列表栏
 * Created by je on 9/11/17.
 */

public class SettingItemView extends ConstraintLayout
{
    @SettingLayoutType
    private int mSettingLayoutType;


    public static final int TYPE_SETTING = 1;
    public static final int TYPE_USERINFO = 2;
    private ImageView setting_right_image;
    private ImageView setting_right_icon;
    private ImageView setting_icon_noTint;
    private View new_message_icon;

    @IntDef({TYPE_SETTING, TYPE_USERINFO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SettingLayoutType {}


    private ImageView setting_icon;
    private TextView setting_name;
    private TextView setting_content;
    private View bottom_line;

    public SettingItemView(Context context)
    {
        super(context);
        initializa(null);
    }

    public SettingItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializa(attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializa(attrs);
    }

    private void initializa(AttributeSet attrs)
    {
        inflate(getContext(), R.layout.widget_setting_item,this);
        if (attrs != null) {
            final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView);
            //noinspection WrongConstant
            mSettingLayoutType = typedArray.getInt(R.styleable.SettingItemView_layoutType,TYPE_SETTING);
            typedArray.recycle();
        }


        setting_icon = (ImageView) findViewById(R.id.setting_icon);
        setting_name = (TextView) findViewById(R.id.setting_name);
        setting_content = (TextView) findViewById(R.id.setting_content);
        bottom_line =  findViewById(R.id.bottom_line);
        setting_right_image = (ImageView) findViewById(R.id.setting_right_image);
        setting_right_icon = (ImageView) findViewById(R.id.setting_right_icon);
        setting_icon_noTint = (ImageView) findViewById(R.id.setting_icon_noTint);
        new_message_icon =  findViewById(R.id.new_message_icon);

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        setBackgroundResource(typedValue.resourceId);

        if(mSettingLayoutType == TYPE_USERINFO)
        {
            setting_right_image.setVisibility(VISIBLE);
            setting_content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            setting_content.setTextColor(getResources().getColor(R.color.colorListItemTitleText));
            setting_name.setTextColor(getResources().getColor(R.color.colorListItemContentText));
            setting_icon.setVisibility(GONE);
            setPadding(ViewUtil.dpToPx(4,getContext()),0,0,0);
        }


    }

    /**
     * 设置各值
     * @param iconResId  icon
     * @param name       名字
     * @param content    右侧的内容，如果没有可传空
     */
    public void setValue(@DrawableRes int iconResId, String name, @Nullable  String content)
    {
        setting_icon.setImageResource(iconResId);
        setting_name.setText(name);
        if(null != content)
            setting_content.setText(content);
        else
            setting_content.setText("");
    }

    /**
     * 设置各值
     * @param content    右侧的内容
     */
    public void setConetnt(String content)
    {
        if(null != content)
            setting_content.setText(content);
        else
            setting_content.setText("");
    }

    /**
     * 设置各值
     * @param name       名字
     * @param content    右侧的内容，如果没有可传空
     */
    public void setValue(String name, @Nullable  String content)
    {
        setting_name.setText(name);
        if(null != content)
            setting_content.setText(content);
    }

    public void setNoTintIcon(@DrawableRes int iconResId)
    {
        setting_icon_noTint.setImageResource(iconResId);
    }

    public ImageView getRightImageView()
    {
        return setting_right_image;
    }

    public void hideLine()
    {
        bottom_line.setVisibility(GONE);
    }

    public void hideRightGotoIcon()
    {
        setting_right_icon.setVisibility(INVISIBLE);
    }

    public void showNewMessageIcon()
    {
        new_message_icon.setVisibility(VISIBLE);
    }
    public void hideNewMessageIcon()
    {
        new_message_icon.setVisibility(GONE);
    }
}
