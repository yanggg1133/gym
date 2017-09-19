package com.hxs.fitnessroom.widget;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
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
    private TextView other_tip;
    private ImageView other_tip_icon;

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
        other_tip = (TextView) findViewById(R.id.other_tip);
        other_tip_icon = (ImageView) findViewById(R.id.other_tip_icon);
        network_error_tip.setOnClickListener(this);
        setOnClickListener(this);
        hide();
    }

    public void hide()
    {
        contentLoadingProgressBar.hide();
        other_tip_icon.setVisibility(GONE);
        other_tip.setVisibility(GONE);
        setVisibility(GONE);
    }

    public void show()
    {
        post(new Runnable()
        {
            @Override
            public void run()
            {
                setBackgroundResource(R.color.colorPrimary);
                setVisibility(VISIBLE);
                contentLoadingProgressBar.show();

            }
        });
    }


    public void showByNullBackground()
    {
        setBackground(null);
        setVisibility(VISIBLE);
        contentLoadingProgressBar.show();
    }


    public void showNetworkError()
    {
        contentLoadingProgressBar.hide();
        network_error_tip.setVisibility(VISIBLE);
    }

    public void showSuccess(String message)
    {
        setBackgroundResource(R.color.colorPrimary);
        setVisibility(VISIBLE);
        other_tip_icon.setVisibility(VISIBLE);
        other_tip.setVisibility(VISIBLE);
        other_tip_icon.setImageResource(R.mipmap.ic_pay_success);
        other_tip.setText(message);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.network_error_tip:
            {
                contentLoadingProgressBar.show();
                network_error_tip.setVisibility(GONE);
                if(null != mOnReloadListener)
                {
                    mOnReloadListener.onReload();
                }
                break;
            }
            default:
                break;
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
