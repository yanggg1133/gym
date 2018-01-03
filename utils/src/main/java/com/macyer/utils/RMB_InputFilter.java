package com.macyer.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;


/**
 * 
 * textview.setFilters(new InputFilter[]{new RMB_InputFilter(mContext)});
 * Created by liuxiu on 2017/5/5.
 */

public class RMB_InputFilter implements InputFilter {
    private int RMB_LENGTH = 8;

    private String[] RMB_DES = {"十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};

    private Context mContext;

    public RMB_InputFilter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @param length   设置限制金额的整数的位数
     */
    public RMB_InputFilter(Context mContext, int length) {
        this.mContext = mContext;
        RMB_LENGTH = length;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if (source.equals(".") && dest.toString().length() == 0) {
            return "0.";
        }
        if (source.equals("0") && dest.toString().equals("0")) {
            return "";
        }
        if (dest.toString().trim().length() > 1
                && (dest.toString().contains(".") ? dest.subSequence(0, dest.toString().indexOf(".")).length() : dest.toString().length()) > (RMB_LENGTH - 1)
                && !(".".equals(source) || dest.toString().contains("."))) {
            ToastUtil.show("金额不得大于一" + RMB_DES[RMB_LENGTH - 1]);
            return "";
        }
        if (dest.toString().contains(".")) {
            int index = dest.toString().indexOf(".");
            int mlength = dest.toString().substring(index).length();
            if (mlength == 3) {
                return "";
            }
        }

        return null;
    }
}
