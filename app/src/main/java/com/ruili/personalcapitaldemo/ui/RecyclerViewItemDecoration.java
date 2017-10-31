package com.ruili.personalcapitaldemo.ui;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Rui Li on 10/30/17.
 */

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        int span = gridLayoutManager.getSpanCount();

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) < span) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
