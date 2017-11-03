package com.hxs.fitnessroom.module.home.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.image.ImageLoader;

/**
 * Created by shaojunjie on 17-10-30.
 */

public class StoreInfoView extends ConstraintLayout
{
    private ImageView store_image;
    private TextView store_address;
    private TextView store_distance;
    private TextView store_name;
    private ImageView store_status_icon;
    private TextView store_status_text;
    private TextView store_status_tips;
    private View line;

    public StoreInfoView(Context context)
    {
        super(context);
        init();
    }

    public StoreInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public StoreInfoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        if(null == getLayoutParams())
        {
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        inflate(getContext(), R.layout.store_main_item,this);
        store_image = findViewById(R.id.store_image);
        store_address = findViewById(R.id.store_address);
        store_distance = findViewById(R.id.store_distance);
        store_name = findViewById(R.id.store_name);
        store_status_icon = findViewById(R.id.store_status_icon);
        store_status_text = findViewById(R.id.store_status_text);
        store_status_tips = findViewById(R.id.store_status_tips);
        line = findViewById(R.id.line);
    }

    /**
     * 设置信息
     * @param storeBean
     */
    public void setStoreBean(StoreBean storeBean)
    {
        store_name.setText(storeBean.name);
        store_distance.setText(storeBean.distance);
        store_address.setText(storeBean.address);

        store_status_text.setText(storeBean.getStatusName());
        store_status_tips.setText(storeBean.statusDesc);
        switch (storeBean.status)
        {
            case StoreBean.STATUS_Idle:
                store_status_icon.setImageResource(R.mipmap.ic_status_qingxian);
            case StoreBean.STATUS_Moderate:
                store_status_icon.setImageResource(R.mipmap.ic_status_shizhong);
            case StoreBean.STATUS_Crowded:
            case StoreBean.STATUS_Full:
                store_status_icon.setImageResource(R.mipmap.ic_status_baoman);
        }

        if(ValidateUtil.isEmpty(storeBean.img))
            Glide.with(getContext()).clear(store_image);
        else
            ImageLoader.load(storeBean.img,store_image);
    }


    /**
     * 隐藏分割线
     */
    public void hideLine()
    {
        line.setVisibility(GONE);
    }

}
