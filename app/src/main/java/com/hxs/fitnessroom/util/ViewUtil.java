package com.hxs.fitnessroom.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TintContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxs.fitnessroom.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;


/**
 *Created by lsleidongran on 2016/7/15.
 */
public class ViewUtil
{

    /**
     * 设置下拉刷新View的圆圈颜色
     * @see SwipeRefreshLayout#setColorSchemeResources(int...)
     * @author shaojunjie on 17-5-10
     * @Email fgnna@qq.com
     *
     */
    public static void initSwipeRefreshLayoutColor(SwipeRefreshLayout swipeRefreshLayout)
    {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
    }

    /**
     * 获取RecyclerView的通用布局间距 x20
     * 只支持 一列 的布局
     * @author shaojunjie on 17-5-10
     * @Email fgnna@qq.com
     *
     */
    public static RecyclerView.ItemDecoration getRecyclerViewItemDecorationOneRow()
    {
        return new RecyclerView.ItemDecoration() {
            int mSpace = -1;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
            {
                if( -1 == mSpace )
                    mSpace = ViewUtil.dpToPx(10f,view.getContext());

                int pos = parent.getChildAdapterPosition(view);
                if( RecyclerView.NO_POSITION == pos)
                    return;
                if (pos == 0)
                {
                    outRect.top = mSpace;
                    outRect.bottom = mSpace;
                }
                else
                {
                    outRect.top = 0;
                    outRect.bottom = mSpace;
                }
            }
        };
    }
    public static RecyclerView.ItemDecoration getRecyclerViewItemDecorationCardView(final boolean fristNeedSpace)
    {
        return new RecyclerView.ItemDecoration() {
            int mSpace = -1;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
            {
                if( -1 == mSpace )
                    mSpace = ViewUtil.dpToPx(10f,view.getContext());

                int pos = parent.getChildAdapterPosition(view);
                if( RecyclerView.NO_POSITION == pos)
                    return;
                if (pos == 0)
                {
                    if( fristNeedSpace)
                    {
                        outRect.top = mSpace;
                        outRect.bottom = mSpace;
                        outRect.left = mSpace;
                        outRect.right = mSpace;
                    }
                    else
                    {
                        outRect.top = 0;
                        outRect.bottom = mSpace;
                        outRect.left = 0;
                        outRect.right = 0;
                    }

                }
                else
                {
                    outRect.top = 0;
                    outRect.bottom = mSpace;
                    outRect.left = mSpace;
                    outRect.right = mSpace;
                }


            }
        };
    }


    /**
     * Drawable 着色的后向兼容方案
     *
     * @param drawable Drawable
     * @param colors   颜色状态列表
     * @return Drawable
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static Drawable tintDrawable(Drawable drawable, int colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 根据背景的着色
     * @param viewGroup 控件
     * @param color     颜色
     */
    public static void setBackgroundTint(ViewGroup viewGroup, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewGroup.setBackground(tintDrawable(viewGroup.getBackground(), color));
        } else {
            viewGroup
                    .setBackgroundDrawable(tintDrawable(viewGroup.getBackground(), color));
        }
    }

    /**
     * 在高版本兼容包中，普通TextView的Context有可能被包装为TintContextWrapper
     * 所以当需要从TextView中获取 context时，最好使用此方法，以防使用了错误的Context
     * @param view
     * @return
     */
    public static Context getSupportContext(View view){
        if (view.getContext() instanceof TintContextWrapper){
            return ((TintContextWrapper)view.getContext()).getBaseContext();
        }else {
            return view.getContext();
        }
    }


    /**
     * DP转PX
     */
    public static int dpToPx(float dp,Context context)
    {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }




    /**
     * 用于编辑EditTextView时，内容产生变动时清除错误提示内容
     */
    public static class ClearErrorTextWatcher implements TextWatcher
    {
        private TextInputLayout textInputLayout;
        public ClearErrorTextWatcher(TextInputLayout textInputLayout){this.textInputLayout = textInputLayout;}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {this.textInputLayout.setError(null);}
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
