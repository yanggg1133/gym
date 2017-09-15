package com.hxs.fitnessroom.widget;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxs.fitnessroom.R;

/**
 * 页面内嵌加载中
 * Created by je on 9/14/17.
 */

public class LoadingView extends RelativeLayout implements View.OnClickListener
{
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private TextView network_error_tip;
    private OnReloadListener mOnReloadListener;

    public LoadingView(Context context)
    {
        super(context);
        initializ();
    }

    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializ();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializ();
    }

    private void initializ()
    {
        inflate(getContext(), R.layout.widget_loading,this);
        setBackgroundResource(R.color.colorPrimary);
        contentLoadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingProgressBar);
        network_error_tip = (TextView) findViewById(R.id.network_error_tip);
        network_error_tip.setOnClickListener(this);
    }

    public void hide()
    {
        contentLoadingProgressBar.hide();
        setVisibility(GONE);
    }

    public void show()
    {
        setBackgroundResource(R.color.colorPrimary);
        contentLoadingProgressBar.show();
        setVisibility(VISIBLE);
    }


    public void showByNullBackground()
    {
        setBackground(null);
        setVisibility(VISIBLE);
        contentLoadingProgressBar.show();
    }


    public void showError()
    {
        contentLoadingProgressBar.hide();
        network_error_tip.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        contentLoadingProgressBar.show();
        network_error_tip.setVisibility(GONE);
        if(null != mOnReloadListener)
        {
            mOnReloadListener.onReload();
        }
    }

    public void setReloadListener(OnReloadListener reloadListener)
    {
        this.mOnReloadListener = reloadListener;
    }

    public interface OnReloadListener
    {
        void onReload();
    }




}
