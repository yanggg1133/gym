package com.hxs.fitnessroom.util;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;


/**
 * dialog 封装 类
 * Created by jie on 15-12-17.
 */
public class DialogUtil
{

    public static void  showConfirmDialog(String content, FragmentManager fragmentManager,ConfirmDialog.OnDialogCallback onDialogCallback)
    {
        showConfirmDialog(null,content,null,null,fragmentManager,onDialogCallback);
    }
    public static void  showConfirmDialog(String content, @Nullable String cancelText, @Nullable String confirmText,
                                          FragmentManager fragmentManager,ConfirmDialog.OnDialogCallback onDialogCallback)
    {
        showConfirmDialog(null,content,cancelText,confirmText,fragmentManager,onDialogCallback);
    }
    public static void  showConfirmDialog(String title,String content, @Nullable String cancelText, @Nullable String confirmText,
                                          FragmentManager fragmentManager,ConfirmDialog.OnDialogCallback onDialogCallback)
    {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setOnDialogCallback(onDialogCallback);
        confirmDialog.setContent(content);
        confirmDialog.setCancelText(cancelText);
        confirmDialog.setConfirmText(confirmText);
        confirmDialog.setTitle(title);
        confirmDialog.show(fragmentManager,ConfirmDialog.class.getSimpleName());

    }



}
