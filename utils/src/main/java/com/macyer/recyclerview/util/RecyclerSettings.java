package com.macyer.recyclerview.util;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

/**
 * Created by Lenovo on 2017/12/6.
 */

public class RecyclerSettings {

    private RecyclerView recyclerView;
    private LayoutEnum layoutEnum;
    private int lines;
    private boolean isVertical;

    private enum LayoutEnum {
        LINEAR,
        GRID,
        STAGGERED;
    }

    public RecyclerSettings(RecyclerView recyclerView) {
        this(recyclerView, 1);
        layoutEnum = LayoutEnum.LINEAR;
    }

    /**
     * Grid
     *
     * @param recyclerView
     * @param lines
     */
    public RecyclerSettings(RecyclerView recyclerView, int lines) {
        this(recyclerView, lines, true);
        layoutEnum = LayoutEnum.GRID;
    }

    /**
     * Grid
     *
     * @param recyclerView
     * @param lines
     */
    public RecyclerSettings(RecyclerView recyclerView, int lines, boolean isVertical) {
        this.recyclerView = recyclerView;
        this.lines = lines;
        this.isVertical = isVertical;
        layoutEnum = LayoutEnum.STAGGERED;
        init();
    }

    private void init() {
        switch (layoutEnum) {
            case LINEAR:
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                break;
            case GRID:
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), lines));
                break;
            case STAGGERED:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(lines, isVertical ? StaggeredGridLayoutManager.VERTICAL : StaggeredGridLayoutManager.HORIZONTAL));
                break;
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //当屏幕停止滚动时为0；
            // 当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
            // 由于用户的操作，屏幕产生惯性滑动时为2
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case 0:
                        Glide.with(recyclerView.getContext()).resumeRequests();
                        break;
                    case 1:
                    case 2:
                        Glide.with(recyclerView.getContext()).pauseRequests();
                        break;
                }
            }
        });
    }
}
