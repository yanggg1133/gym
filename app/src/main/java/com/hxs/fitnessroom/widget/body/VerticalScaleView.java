package com.hxs.fitnessroom.widget.body;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ViewUtil;


/**
 * 纵向刻度视图
 * Created by je on 9/8/17.
 */

public class VerticalScaleView extends View
{
    private Paint mTextPaint;
    private float sumSpace;
    private float textSumSpace;

    public VerticalScaleView(Context context)
    {
        this(context, null);
    }

    public VerticalScaleView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, R.style.AppTheme);
    }

    public VerticalScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializa();
    }

    private Paint mBigTypePaint;
    private Paint mSmallTypePaint;

    private float strokeWidth = 0f;
    private float fullLineWidth = 0.36f;
    private float middleLineWidth = 0.3f;
    private float smallLineWidth = 0.2f;
    private float textSize = 0f;


    //间隔
    private float space;

    private void initializa()
    {

        strokeWidth = ViewUtil.dpToPx(1, getContext());

        mBigTypePaint = new Paint();
        mBigTypePaint.setColor(0xFFC7D0FF);
        mBigTypePaint.setStrokeWidth(strokeWidth);

        mSmallTypePaint = new Paint();
        mSmallTypePaint.setColor(0xB2C7D0FF);
        mSmallTypePaint.setStrokeWidth(strokeWidth/2);

        textSize = ViewUtil.dpToPx(12, getContext());
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFF8b92b8);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);
    }

    private float[] spaceList = new float[81];

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float height = getHeight() - textSize;

        space = ( height - textSize) / 80f;
        sumSpace = textSize * 0.7f;
        textSumSpace = textSize;
        fullLineWidth = getWidth() - ViewUtil.dpToPx(16, getContext());
        middleLineWidth = getWidth() - ViewUtil.dpToPx(13, getContext());
        smallLineWidth = getWidth() - ViewUtil.dpToPx(10, getContext());
        int textStart = getWidth() - ViewUtil.dpToPx(44, getContext());

        int maxCm = 200;
        for (int i = 0; i <= 80; i++)
        {
            float start = getWidth();
            start = ((i % 10) == 0) ? fullLineWidth : ((i % 5) == 0) ? middleLineWidth : smallLineWidth;
            canvas.drawLine(start, sumSpace, getWidth(), sumSpace, (i % 10) == 0 ? mBigTypePaint : mSmallTypePaint);
            sumSpace += space;

            if ((i % 10) == 0)
            {
                canvas.drawText(String.valueOf(maxCm), textStart, textSumSpace, mTextPaint);
                maxCm -= 10;
            }
            textSumSpace += space;

        }
        canvas.drawLine(getWidth() - strokeWidth / 2, textSize * 0.7f, getWidth() - strokeWidth / 2, sumSpace - space, mBigTypePaint);
    }

    public float getStartY()
    {
        return getY() + textSize * 0.7f;
    }

    public float getEndY()
    {
        LogUtil.dClass("EndY=" + (getY() + sumSpace - space));
        return getY() + sumSpace - space;
    }
}
