package com.hxs.fitnessroom.module.home.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ToastUtil;

import java.util.List;

/**
 * 预约时间选择控件
 * Created by shaojunjie on 17-10-31.
 */

public class StoreReserveSelectTimeView extends GridLayout
{

    /**
     * 列数
     */
    private final int mColumnCount = 3;

    /**
     * 数据缓存
     */
    private List<List> mData;

    public StoreReserveSelectTimeView(Context context)
    {
        super(context);
        initializ();
    }

    public StoreReserveSelectTimeView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initializ();
    }

    public StoreReserveSelectTimeView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initializ();
    }

    private void initializ()
    {
        setColumnCount(mColumnCount);
    }


    /**
     * 　设置数据
     */
    public void setData(List<List> data)
    {
        if (null == data || 0 == data.size())
        {
            ToastUtil.toastShort("网络数据异常!");
            return;
        }
        for (List data2 : data)
        {
            if (null == data2 || 0 == data2.size())
            {
                ToastUtil.toastShort("网络数据异常!");
                return;
            }
        }

        mData = data;
        initializView();
    }


    private int startIndex = 0;
    private int rowCount = 0;

    /**
     * 开始根据数据创建视图
     */
    private void initializView()
    {
        rowCount += mData.size();

        LogUtil.dClass(rowCount);

        for (List childData : mData)
        {
            int size = childData.size() / mColumnCount;
            LogUtil.dClass(childData.size());
            rowCount += childData.size() % mColumnCount > 0 ? size + 1 : size;
            LogUtil.dClass(rowCount);
        }

        for (List childData : mData)
        {
            TextView textView = createDateView();
            textView.setText("11111");
            addView(textView);
            for (Object child : childData)
            {

                TextView textView1 = createTimeView();
                textView1.setText("10506562606");
                addView(textView1);
            }
        }
    }


    /**
     * 创建一个日期View
     *
     * @return
     */
    private TextView createDateView()
    {

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setTextColor(0xFF656C91);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.setGravity(Gravity.FILL);
        layoutParams.columnSpec = spec(startIndex, mColumnCount,3);
        startIndex += mColumnCount;
        return textView;
    }


    /**
     * 创建一个时间段View
     *
     * @return
     */
    private TextView createTimeView()
    {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setTextColor(0xFFC7D0FF);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.columnSpec = spec(startIndex++, mColumnCount,1);
        return textView;
    }
}
