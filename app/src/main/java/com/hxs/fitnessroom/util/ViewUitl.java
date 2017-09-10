package com.hxs.fitnessroom.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TintContextWrapper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;

import static com.baidu.location.h.a.f;
import static com.baidu.location.h.k.R;


/**
 *Created by lsleidongran on 2016/7/15.
 */
public class ViewUitl {




    /**
     * 设置下拉刷新View的圆圈颜色
     * @see SwipeRefreshLayout#setColorSchemeResources(int...)
     * @author shaojunjie on 17-5-10
     * @Email fgnna@qq.com
     *
     */
    public static void initSwipeRefreshLayoutColor(SwipeRefreshLayout swipeRefreshLayout)
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
            android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
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
                    mSpace = ViewUitl.dpToPx(10f,view.getContext());

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
                    mSpace = ViewUitl.dpToPx(10f,view.getContext());

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
     * 获取RecyclerView的通用布局间距 x20
     * 只支持 两列 的布局
     * @author shaojunjie on 17-5-10
     * @Email fgnna@qq.com
     *
     */
    public static RecyclerView.ItemDecoration getRecyclerViewItemDecorationTwoRow()
    {


        return new RecyclerView.ItemDecoration() {
            int mSpace = -1;
            int mOutSideSpace = -1;
            int mInSideSpace = -1;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
            {
                if( -1 == mSpace )
                {
                    mSpace = ViewUitl.dpToPx(10f,view.getContext());
                    mOutSideSpace =ViewUitl.dpToPx(12f,view.getContext());
                    mInSideSpace = ViewUitl.dpToPx(6f,view.getContext());
                }

                int pos = parent.getChildAdapterPosition(view);
                if( RecyclerView.NO_POSITION == pos)
                    return;
                if (pos == 0 || pos == 1)
                {
                    outRect.top = mSpace;
                }
                else
                {
                    outRect.top = 0;
                    outRect.bottom = 0;
                }

                if(pos % 2 == 0 )
                    ((ViewGroup)view).setPadding(mOutSideSpace,mSpace,mInSideSpace,mSpace);
                else
                    ((ViewGroup)view).setPadding(mInSideSpace,mSpace,mOutSideSpace,mSpace);

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
     * 通过反射改变Tablayout下划线的长度
     * @param tabs
     * @param leftDip 左pindding值
     * @param rightDip 右pindding值
     */
    public static void setIndicator (final TabLayout tabs, final int leftDip, final int rightDip)
    {
        tabs.post(new Runnable()
        {
            @Override
            public void run()
            {

                Class<?> tabLayout = tabs.getClass();
                Field tabStrip = null;
                try {
                    tabStrip = tabLayout.getDeclaredField("mTabStrip");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                tabStrip.setAccessible(true);
                LinearLayout llTab = null;
                try {
                    llTab = (LinearLayout) tabStrip.get(tabs);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
                int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

                for (int i = 0; i < llTab.getChildCount(); i++)
                {
                    View child = llTab.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = left;
                    params.rightMargin = right;
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            }
        });
    }

    public static JSONObject getViewJson(View view)
    {
        try
        {
            JSONObject ret = new JSONObject();
            if (view instanceof ViewGroup)
            {
                ViewGroup vg = (ViewGroup) view;
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < vg.getChildCount(); i++)
                {
                    View sub = vg.getChildAt(i);
                    jsonArray.put(getViewJson(sub));
                }
                ret.put("subView", jsonArray);
            }
            ret.put("class", view.getClass().getName());
            ret.put("id", view.getId());
            ret.put("background", view.getBackground());
            ret.put("visibility", view.getVisibility());
            ret.put("width", view.getMeasuredWidth());
            ret.put("height", view.getMeasuredHeight());
            if (view instanceof TextView)
            {
                ret.put("text", ((TextView) view).getText());
            }
            else if (view instanceof ImageView)
            {
                ret.put("src", ((ImageView) view).getDrawable());
            }
            return ret;
        }
        catch (Exception e)
        {
            return null;
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
}
