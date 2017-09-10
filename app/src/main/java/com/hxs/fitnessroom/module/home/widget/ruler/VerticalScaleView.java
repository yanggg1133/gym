package com.hxs.fitnessroom.module.home.widget.ruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.ViewUitl;


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

    private float strokeWidth = 3f;
    private float fullLineWidth = 0.4f;
    private float middleLineWidth = 0.3f;
    private float smallLineWidth = 0.2f;
    private float textSize = 0f;


    //间隔
    private float space;

    private void initializa()
    {
        mBigTypePaint = new Paint();
        mBigTypePaint.setColor(0xffffffff);
        mBigTypePaint.setStrokeWidth(strokeWidth);
        mBigTypePaint.setTextSize(textSize);

        mSmallTypePaint = new Paint();
        mSmallTypePaint.setColor(0xffdddddd);
        mSmallTypePaint.setStrokeWidth(strokeWidth);
        textSize = ViewUitl.dpToPx(10, getContext());

        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(textSize);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float height = getHeight() - textSize;

        space = ((float) height - textSize) / 80f;
        sumSpace = textSize * 0.7f;
        textSumSpace = textSize;
        fullLineWidth = getWidth() - (getWidth() * fullLineWidth);
        middleLineWidth = getWidth() - (getWidth() * middleLineWidth);
        smallLineWidth = getWidth() - (getWidth() * smallLineWidth);

        int maxCm = 200;
        for (int i = 0; i <= 80; i++)
        {
            float start = getWidth();
            start = ((i % 10) == 0) ? fullLineWidth : ((i % 5) == 0) ? middleLineWidth : smallLineWidth;
            canvas.drawLine(start, sumSpace, getWidth(), sumSpace, mSmallTypePaint);
            sumSpace += space;

            if ((i % 10) == 0)
            {
                canvas.drawText(maxCm+"cm", 0, textSumSpace, mTextPaint);
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
        return getY() + sumSpace - space;
    }
}
