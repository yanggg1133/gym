package com.hxs.fitnessroom.widget.body;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ViewUtil;

/**
 * 身高尺子
 * Created by je on 9/8/17.
 */
public class RulerViewLayout extends FrameLayout
{


    public RulerViewLayout(Context context)
    {
        this(context, null);
    }

    public RulerViewLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, R.style.AppTheme);
    }

    public RulerViewLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializa();
    }

    private VerticalScaleView mVerticalScaleView;
    private View mMoveIconView;
    private View mBodyView;
    private boolean mIsScrolling;

    private float heightScale;
    private float minHeight;
    private float maxHeight;

    private OnChengedListener mOnChengedListener;

    private void initializa()
    {
    }

    public void setOnChengedListener(OnChengedListener onChengedListener)
    {
        mOnChengedListener = onChengedListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if (mVerticalScaleView == null)
        {
            for (int i = 0; i < getChildCount(); i++)
            {
                if (getChildAt(i) instanceof VerticalScaleView)
                {
                    mVerticalScaleView = (VerticalScaleView) getChildAt(i);
                } else if (getChildAt(i).getId() == R.id.body_image)
                {
                    mBodyView = getChildAt(i);
                } else
                {
                    mMoveIconView = getChildAt(i);
                }
            }
            mMoveIconView.setY(mVerticalScaleView.getStartY() - mMoveIconView.getHeight() / 2);

            minHeight = mBodyView.getHeight();
            maxHeight = minHeight + mBodyView.getY() - ViewUtil.dpToPx(10,getContext());
            heightScale = (maxHeight - minHeight) / 80f;

        }
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {

        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP)
        {
            LogUtil.dClass("ACTION_CANCEL || action == MotionEvent.ACTION_UP ");
            mIsScrolling = false;
            return false;
        }

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                LogUtil.dClass("ACTION_DOWN");
                return true;
            }
            case MotionEvent.ACTION_MOVE:
            {
                LogUtil.dClass("ACTION_MOVE");
                break;
            }
        }

        return false;
    }


    public void setHeight(final int height)
    {
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setHeight(height,false);
            }
        },100);
    }

    private void setHeight(int height,boolean isTouchEvent)
    {

        if (height < 120 || height > 200)
        {
            Toast.makeText(getContext(), "超出可选高度范围", Toast.LENGTH_SHORT).show();
            height = 160;
        }

        int scale = (int) (((float) height - 120f) * heightScale);
        mBodyView.getLayoutParams().height = (int) (minHeight + scale);
        float sumY = mVerticalScaleView.getEndY() - mVerticalScaleView.getStartY();
        float cy = sumY / 80 * (200 - height);
        if(!isTouchEvent)
        {
            mMoveIconView.setY(mVerticalScaleView.getStartY()+cy-mMoveIconView.getHeight()/2);
            if (null != mOnChengedListener)
                mOnChengedListener.onChenged(height);
        }
        requestLayout();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (ev.getY() <= mVerticalScaleView.getStartY())
            mMoveIconView.setY(mVerticalScaleView.getStartY() - mMoveIconView.getHeight() / 2);
        else if (ev.getY() >= mVerticalScaleView.getEndY())
            mMoveIconView.setY(mVerticalScaleView.getEndY() - mMoveIconView.getHeight() / 2);
        else
            mMoveIconView.setY(ev.getY() - mMoveIconView.getHeight() / 2);

        float sum = mVerticalScaleView.getEndY() - mVerticalScaleView.getStartY();
        float now = mVerticalScaleView.getEndY() - (mMoveIconView.getY() + mMoveIconView.getHeight() / 2);
        float space = sum / 80;
        int res = (int) (now / space + 120);
        if (null != mOnChengedListener)
            mOnChengedListener.onChenged(res);
        setHeight(res,true);

        return true;
    }

    public interface OnChengedListener
    {
        void onChenged(int height);
    }

}
