package com.hxs.fitnessroom.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;


/**
 * 设置栏控件
 * 参考个人中心的跳转列表栏
 * Created by je on 9/11/17.
 */

public class SettingItemView extends ConstraintLayout
{
    private ImageView setting_icon;
    private TextView setting_name;
    private TextView setting_content;
    private View bottom_line;

    public SettingItemView(Context context)
    {
        super(context);
        initializa();
    }

    public SettingItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializa();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializa();
    }

    private void initializa()
    {
        inflate(getContext(), R.layout.widget_setting_item,this);
        setting_icon = (ImageView) findViewById(R.id.setting_icon);
        setting_name = (TextView) findViewById(R.id.setting_name);
        setting_content = (TextView) findViewById(R.id.setting_content);
        bottom_line =  findViewById(R.id.bottom_line);
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        setBackgroundResource(typedValue.resourceId);
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
    }

    public void hideLine()
    {
        bottom_line.setVisibility(GONE);
    }
}
