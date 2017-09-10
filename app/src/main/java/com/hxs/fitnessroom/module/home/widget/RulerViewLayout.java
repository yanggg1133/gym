package com.hxs.fitnessroom.module.home.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.home.widget.ruler.VerticalScaleView;
import com.hxs.fitnessroom.util.LogUtil;

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
    private boolean mIsScrolling;
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
        if(mVerticalScaleView == null )
        {
            for (int i = 0; i < getChildCount(); i++)
            {
                if(getChildAt(i) instanceof VerticalScaleView)
                {
                    mVerticalScaleView = (VerticalScaleView) getChildAt(i);
                }
                else
                {
                    mMoveIconView = getChildAt(i);
                }
            }
            mMoveIconView.setY(mVerticalScaleView.getStartY()-mMoveIconView.getHeight()/2);

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */


        final int action = MotionEventCompat.getActionMasked(ev);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                LogUtil.dClass("ACTION_CANCEL || action == MotionEvent.ACTION_UP ");
            // Release the scroll.
            mIsScrolling = false;
            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            {
                LogUtil.dClass("ACTION_DOWN");

                return true;

                //break;
            }
            case MotionEvent.ACTION_MOVE: {
                LogUtil.dClass("ACTION_MOVE");
//                if (mIsScrolling) {
//                    // We're currently scrolling, so yes, intercept the
//                    // touch event!
//                    return true;
//                }
//
//                // If the user has dragged her finger horizontally more than
//                // the touch slop, start the scroll
//
//                // left as an exercise for the reader
//                final int xDiff = calculateDistanceX(ev);
//
//                // Touch slop should be calculated using ViewConfiguration
//                // constants.
//                if (xDiff > mTouchSlop) {
//                    // Start scrolling!
//                    mIsScrolling = true;
//                    return true;
//                }
                break;
            }
        }

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }

    private int calculateDistanceX(MotionEvent ev)
    {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE,
        // scroll this container).
        // This method will only be called if the touch event was intercepted in
        // onInterceptTouchEvent


        //
        if(ev.getY() <=  mVerticalScaleView.getStartY())
            mMoveIconView.setY(mVerticalScaleView.getStartY()-mMoveIconView.getHeight()/2);
        else if(ev.getY() >=  mVerticalScaleView.getEndY())
            mMoveIconView.setY(mVerticalScaleView.getEndY()-mMoveIconView.getHeight()/2);
        else
            mMoveIconView.setY(ev.getY()-mMoveIconView.getHeight()/2);

        float sum = mVerticalScaleView.getEndY()-mVerticalScaleView.getStartY();
        float now = mVerticalScaleView.getEndY() - (mMoveIconView.getY()+mMoveIconView.getHeight()/2 );
        float space = sum / 80;
        int res = (int) (now / space + 120);
        if( null != mOnChengedListener)
            mOnChengedListener.onChenged(res);


        return true;
    }

    public interface  OnChengedListener
    {
        void onChenged(int height);
    }

}
