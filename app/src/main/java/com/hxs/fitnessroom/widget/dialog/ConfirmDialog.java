package com.hxs.fitnessroom.widget.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.util.ValidateUtil;

/**
 * 普通提示弹窗
 * 包括： 提示内容
 * 确认/取消 回调
 * Created by je on 9/15/17.
 */

public class ConfirmDialog extends DialogFragment implements View.OnClickListener
{
    private View dialog_background;
    private TextView content;
    private TextView confirm_action;
    private TextView cancel_action;
    private View dialog_content_layout;
    private OnDialogCallback onDialogCallback;
    private String mContent;
    private String mCancelText;
    private String mConfirmText;
    private boolean mIsConfirm = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.widget_confirm_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        content = (TextView) view.findViewById(R.id.content);
        confirm_action = (TextView) view.findViewById(R.id.confirm_action);
        cancel_action = (TextView) view.findViewById(R.id.cancel_action);
        dialog_background = view.findViewById(R.id.dialog_background);
        dialog_content_layout = view.findViewById(R.id.dialog_content_layout);

        if(ValidateUtil.isNotEmpty(mContent))
            content.setText(mContent);
        if(ValidateUtil.isNotEmpty(mConfirmText))
            confirm_action.setText(mConfirmText);
        if(ValidateUtil.isNotEmpty(mCancelText))
            cancel_action.setText(mCancelText);

        windowInAnimate();
        confirm_action.setOnClickListener(this);
        dialog_content_layout.setOnClickListener(this);
        cancel_action.setOnClickListener(this);
        dialog_background.setOnClickListener(this);
    }

    private void windowInAnimate()
    {
        dialog_background.animate().alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300).start();
        dialog_content_layout.animate().alpha(1f).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
            }
        }).setInterpolator(new FastOutSlowInInterpolator()).setDuration(200).start();
    }

    private void windowOutAnimate()
    {
        dialog_background.animate().alpha(0.0f).setInterpolator(new FastOutSlowInInterpolator()).setDuration(300).start();
        dialog_content_layout.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                dismiss();
            }
        }).setDuration(200).start();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.confirm_action:
                this.mIsConfirm = true;
                windowOutAnimate();
                onDialogCallback.onConfirm();
                break;
            case R.id.dialog_content_layout:
                break;
            case R.id.cancel_action:
                windowOutAnimate();
                onDialogCallback.onCancel();
                break;
            case R.id.dialog_background:
                windowOutAnimate();
                onDialogCallback.onCancel();
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        if(!this.mIsConfirm)
            onDialogCallback.onCancel();
    }



    public void setOnDialogCallback(OnDialogCallback onDialogCallback)
    {
        this.onDialogCallback = onDialogCallback;
    }

    public void setContent(String content)
    {
        mContent = content;
    }

    public void setCancelText(String cancelText)
    {
        mCancelText = cancelText;
    }

    public void setConfirmText(String confirmText)
    {
        mConfirmText = confirmText;
    }


    public interface OnDialogCallback
    {
        void onConfirm();

        void onCancel();
    }

    public static class OnDialogCallbackAdapter implements OnDialogCallback
    {
        @Override
        public void onCancel()
        {
        }

        @Override
        public void onConfirm()
        {
        }
    }

}
