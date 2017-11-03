package com.hxs.fitnessroom.module.home.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 预约时间选择控件
 * Created by shaojunjie on 17-10-31.
 */

public class StoreReserveSelectTimeView extends GridLayout implements View.OnClickListener
{

    /**
     * 列数
     */
    private final int mColumnCount = 3;
    private int mColumnMargin;
    private int mColumnHeight;

    /**
     * 数据缓存
     */
    private StoreReserveBean mData;
    private OnSelectChangedListener mOnSelectChangedListener;
    private List<SelectViewBean> selectedViewBeanList = new ArrayList<>();

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
        mColumnMargin = ViewUtil.dpToPx(8, getContext());
        mColumnHeight = ViewUtil.dpToPx(25, getContext());
        setPadding(mColumnMargin, mColumnMargin, mColumnMargin, mColumnMargin);
        setColumnCount(mColumnCount);
    }

    /**
     * 选择项变更监听
     *
     * @param onSelectChangedListener
     */
    public void setOnSelectChangedListener(OnSelectChangedListener onSelectChangedListener)
    {
        this.mOnSelectChangedListener = onSelectChangedListener;
    }

    /**
     * 　设置数据
     */
    public void setData(StoreReserveBean data)
    {
        if (null == data.appointment || 0 == data.appointment.size())
        {
            ToastUtil.toastShort("网络数据异常!");
            return;
        }
        for (StoreReserveBean.Appointment appointment : data.appointment)
        {
            if (null == appointment.time || 0 == appointment.time.size())
            {
                ToastUtil.toastShort("网络数据异常!");
                return;
            }
        }

        mData = data;
        initializView();
    }


    /**
     * 开始根据数据创建视图
     */
    private void initializView()
    {

        for (StoreReserveBean.Appointment appointment : mData.appointment)
        {
            /**
             * 日期
             */
            TextView dateView = createDateView();
            dateView.setText(appointment.date);
            addView(dateView);

            /**
             * 时间段view
             */
            SelectViewBean tempSelectViewBean = null;
            for (StoreReserveBean.Time time : appointment.time)
            {

                TextView timeView = createTimeView(time.status);
                timeView.setText(time.timeDesc);
                addView(timeView);

                tempSelectViewBean = initOnclick(timeView, time, tempSelectViewBean);
                tempSelectViewBean.date = appointment.date;
            }

            /**
             * 空白的补位view
             */
            for (int i = 0, count = mColumnCount - appointment.time.size() % mColumnCount; i < count && count != mColumnCount; i++)
            {
                TextView emptyView = createEmptyView();
                addView(emptyView);
            }
        }
    }

    /**
     * 配置时间段点击交互
     *
     * @param timeView
     * @param time
     */
    private SelectViewBean initOnclick(TextView timeView, StoreReserveBean.Time time, SelectViewBean previousBean)
    {

        SelectViewBean selectViewBean = new SelectViewBean();
        selectViewBean.timeView = timeView;
        selectViewBean.mTime = time;
        selectViewBean.previousBean = previousBean;
        if (previousBean != null)
        {
            previousBean.nextBean = selectViewBean;
            selectViewBean.index = previousBean.index + 1;
        } else
        {
            selectViewBean.index = 1;
        }
        timeView.setOnClickListener(this);
        timeView.setTag(selectViewBean);
        return selectViewBean;
    }

    /**
     * 点击的关键逻辑
     *
     * @param
     * @see SelectViewBean
     */
    @Override
    public void onClick(View v)
    {
        SelectViewBean selectViewBean = (SelectViewBean) v.getTag();

        /**
         * 点击取消选中
         */
        if (selectViewBean.isSelected)
        {
            selectViewBean.timeView.setBackgroundResource(R.drawable.bg_stroke_656c91_r2);
            selectViewBean.isSelected = false;
            selectedViewBeanList.remove(selectViewBean);
            setNotSelectByAfter(selectViewBean);
        }
        /**
         * 点击选中
         */
        else
        {
            /**
             * 判断该时段是否为可用状态
             */
            if (selectViewBean.mTime.status == StoreBean.STATUS_Timeout)
            {
                return;
            }
            if (selectViewBean.mTime.status == StoreBean.STATUS_Full)
            {
                return;
            }

            if (null != mOnSelectChangedListener && !mOnSelectChangedListener.onSelectChangeing(getTimes(),selectViewBean.mTime))
                return;

            /**
             * 如果第一次选择
             */
            if (selectedViewBeanList.size() == 0)
            {
                selectViewBean.timeView.setBackgroundResource(R.drawable.bg_round_7457ff_r2);
                selectViewBean.isSelected = true;
                selectedViewBeanList.add(selectViewBean);
            }
            /**
             * 如果前一个view为已选择 则可选
             */
            else if (null != selectViewBean.previousBean
                    && selectViewBean.previousBean.isSelected)
            {
                selectViewBean.timeView.setBackgroundResource(R.drawable.bg_round_7457ff_r2);
                selectViewBean.isSelected = true;
                selectedViewBeanList.add(selectViewBean);
            }
            /**
             * 如果后一个view为已选择 则可选
             */
            else if (null != selectViewBean.nextBean
                    && selectViewBean.nextBean.isSelected)
            {
                selectViewBean.timeView.setBackgroundResource(R.drawable.bg_round_7457ff_r2);
                selectViewBean.isSelected = true;
                selectedViewBeanList.add(selectViewBean);
            }
            /**
             * 否则不可选
             */
            else
            {
                ToastUtil.toastShort("只能选择连续时间段");
            }
        }
        onChenged();
    }

    /**
     * 把从当前view开始后面的选中view都设为非选中状态
     * 如果是连续中的第一个，则无需操作
     */
    private void setNotSelectByAfter(SelectViewBean selectViewBean)
    {
        if (null == selectViewBean.previousBean || !selectViewBean.previousBean.isSelected)
            return;

        SelectViewBean nextBean = selectViewBean.nextBean;

        while (null != nextBean && nextBean.isSelected)
        {
            nextBean.timeView.setBackgroundResource(R.drawable.bg_stroke_656c91_r2);
            nextBean.isSelected = false;
            selectedViewBeanList.remove(nextBean);
            nextBean = nextBean.nextBean;
        }
    }

    /**
     * 当选项发生改变
     */
    private void onChenged()
    {
        if (null != mOnSelectChangedListener && selectedViewBeanList.size() > 0)
        {
            mOnSelectChangedListener.onSelectChanged(selectedViewBeanList.get(0).date,  getTimes());
        }
    }

    @NonNull
    private List<StoreReserveBean.Time> getTimes()
    {
        selectedDataReorder();
        List<StoreReserveBean.Time> times = new ArrayList<>();
        for (SelectViewBean selectViewBean : selectedViewBeanList)
        {
            times.add(selectViewBean.mTime);
        }
        return times;
    }

    /**
     * 把已选中的数据重新排序，定证从前到后排序
     */
    private void selectedDataReorder()
    {
        Collections.sort(selectedViewBeanList, new Comparator<SelectViewBean>()
        {
            public int compare(SelectViewBean arg0, SelectViewBean arg1)
            {
                return arg0.index > arg1.index ? 1 : 0;
            }
        });
    }


    /**
     * 创建一个日期View
     *
     * @return
     */
    private TextView createDateView()
    {
        TextView textView = createBaseTextView();
        LayoutParams layoutParams = createBaseLayoutParams();

        textView.setTextColor(0xFF656C91);
        layoutParams.columnSpec = spec(0, mColumnCount, 3);
        textView.setLayoutParams(layoutParams);
        return textView;
    }


    /**
     * 创建一个时间段View
     *
     * @return
     */
    private TextView createTimeView(@StoreBean.StoreStatus int storeStatus)
    {
        checkStartIndex();
        TextView textView = createBaseTextView();
        LayoutParams layoutParams = createBaseLayoutParams();

        textView.setTextColor(0xFFC7D0FF);
        textView.setBackgroundResource(R.drawable.bg_stroke_656c91_r2);
        /**
         * 不可用状态的样式
         */
        if (storeStatus == StoreBean.STATUS_Timeout || storeStatus == StoreBean.STATUS_Full)
        {
            textView.getPaint().setAntiAlias(true);//抗锯齿
            textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
            textView.setAlpha(0.2f);
        }
        layoutParams.columnSpec = spec(startIndex++, 1, 1);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    /**
     * 创建一个空白的占位view,用于补充最后一行多余的空位
     *
     * @return
     */
    private TextView createEmptyView()
    {
        checkStartIndex();
        TextView textView = createBaseTextView();
        LayoutParams layoutParams = createBaseLayoutParams();

        layoutParams.columnSpec = spec(startIndex++, 1, 1);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    /**
     * 单个时间段单元格的起始位,表示第几列
     */
    private int startIndex = 0;

    /**
     * 检查单个单元格的起始位是否已经达到最高列数
     * 如果是，把起始位归0
     */
    private void checkStartIndex()
    {
        if (startIndex == mColumnCount)
        {
            startIndex = 0;
        }
    }

    private TextView createBaseTextView()
    {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        return textView;
    }

    private LayoutParams createBaseLayoutParams()
    {
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.height = mColumnHeight;
        layoutParams.setMargins(mColumnMargin, mColumnMargin, mColumnMargin, mColumnMargin);
        return layoutParams;
    }

    /**
     * 与选择业务相配合使用的bean
     * 主要作用：
     * 连接上一个和下一个时段view,型一个链表，方便判断用户是否连续时段选择
     * 缓存时段顺序及这个时段view对应的数据实体
     */
    class SelectViewBean
    {
        TextView timeView;
        StoreReserveBean.Time mTime;

        String date;
        int index;
        boolean isSelected = false;

        SelectViewBean previousBean;
        SelectViewBean nextBean;

    }

    public interface OnSelectChangedListener
    {
        /**
         * 如果返回false 则不会被选中
         * @param times
         * @param nowTime
         * @return
         */
        boolean onSelectChangeing(List<StoreReserveBean.Time> times, StoreReserveBean.Time nowTime);

        void onSelectChanged(String date, List<StoreReserveBean.Time> times);
    }

}
