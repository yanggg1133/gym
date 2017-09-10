package com.hxs.fitnessroom.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.hxs.fitnessroom.R;


/**
 * dialog 封装 类
 * Created by jie on 15-12-17.
 */
public class DialogUtil
{
    /**
     * 一个最简单的文本提示框
     *
     * @param context
     * @param title   标题如果为空，默认为  提示
     * @param content 内容
     */
    public static void showSimpleDialog(Context context, @Nullable CharSequence title, String content, @Nullable final OnCancelCallback cancelCallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
        if (TextUtils.isEmpty(title)) {
            dialogBuilder.setTitle("提示");
        } else {
            dialogBuilder.setTitle(title);
        }

        dialogBuilder.setMessage(content);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != cancelCallback) {
                    cancelCallback.onCancel(dialog);
                }
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
    }


    /**
     * 确认提示框
     *
     * @param context
     * @param title   标题如果为空，默认为  提示
     * @param content 内容
     */
    public static void showConfirmationDialog(Context context, @Nullable CharSequence title,
                                              CharSequence content, @NonNull final OnConfirmCallback onConfirmCallback,
                                              CharSequence positiveButtonText, CharSequence negativeButtonText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
        if (TextUtils.isEmpty(title))
            dialogBuilder.setTitle("提示");
        else
            dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(content);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton(positiveButtonText == null ? "确认" : positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != onConfirmCallback) {
                    onConfirmCallback.onConfirm();
                }
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(negativeButtonText == null ? "取消" : negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != onConfirmCallback) {
                    onConfirmCallback.onCancel(dialog);
                }
                dialog.cancel();
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#92959f"));
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00d2ff"));

            }
        });
        alertDialog.show();
    }




    /**
     * 进度提示框
     */
//    public static AlertDialog showLoadingDialog(Context context, @Nullable final OnCancelCallback onCancelCallback) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.LoadingDialogStyle);
//        dialogBuilder.setView(R.layout.m_loading_dialog);
//        dialogBuilder.setCancelable(false);
//        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    if (null != onCancelCallback) {
//                        onCancelCallback.onCancel(dialog);
//                    } else {
//                        dialog.dismiss();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
//        AlertDialog dialog = dialogBuilder.show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        return dialog;
//    }





    public interface OnCancelCallback {
        void onCancel(DialogInterface dialog);
    }

    public interface OnConfirmCallback extends OnCancelCallback {
        void onConfirm();
    }

    public interface OnSimpleConfirmCallback {
        void onConfirm();
    }



}
