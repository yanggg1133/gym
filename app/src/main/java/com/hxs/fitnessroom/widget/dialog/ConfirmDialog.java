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
    private TextView title;
    private OnDialogCallback onDialogCallback;
    private String mContent;
    private String mCancelText;
    private String mConfirmText;
    private boolean mIsConfirm = false;
    private String mTitle;
    private TextView confirm_action2;

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
        confirm_action2 = (TextView) view.findViewById(R.id.confirm_action2);
        cancel_action = (TextView) view.findViewById(R.id.cancel_action);
        dialog_background = view.findViewById(R.id.dialog_background);
        dialog_content_layout = view.findViewById(R.id.dialog_content_layout);
        title = (TextView) view.findViewById(R.id.title);

        if(ValidateUtil.isNotEmpty(mContent))
            content.setText(mContent);
        if(null != mConfirmText)
            confirm_action.setText(mConfirmText);
        if(null != mConfirmText && ValidateUtil.isEmpty(mCancelText))
        {
            confirm_action2.setText(mConfirmText);
            confirm_action.setText("");
            confirm_action2.setVisibility(View.VISIBLE);
        }
        if(null != mCancelText)
            cancel_action.setText(mCancelText);
        if(null != mTitle)
        {
            title.setText(mTitle);
            title.setVisibility(View.VISIBLE);
        }

        windowInAnimate();
        confirm_action.setOnClickListener(this);
        confirm_action2.setOnClickListener(this);
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
            case R.id.confirm_action2:
                this.mIsConfirm = true;
                windowOutAnimate();
                if(null != onDialogCallback)
                    onDialogCallback.onConfirm();
                break;
            case R.id.dialog_content_layout:
                break;
            case R.id.cancel_action:
                windowOutAnimate();
                if(null != onDialogCallback)
                    onDialogCallback.onCancelClick();
                break;
            case R.id.dialog_background:
                windowOutAnimate();
                if(null != onDialogCallback)
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
    public void setTitle(String title)
    {
        mTitle = title;
    }


    public interface OnDialogCallback
    {
        void onConfirm();

        /**
         * 其他取消行为
         */
        void onCancel();

        /**
         * 点击取消按钮
         */
        void onCancelClick();
    }

    public static class OnDialogCallbackAdapter implements OnDialogCallback
    {
        @Override
        public void onCancel()
        {
        }

        @Override
        public void onCancelClick()
        {
            onCancel();
        }

        @Override
        public void onConfirm()
        {
        }
    }

}
