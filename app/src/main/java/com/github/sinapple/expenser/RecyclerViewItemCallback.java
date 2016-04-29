package com.github.sinapple.expenser;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Performs tuning swipe gesture for each item of RecyclerView.
 * Also contains callback methods to call specific method of adapter.
 */
public class RecyclerViewItemCallback extends ItemTouchHelper.Callback {
    private ItemTouchHelperAdapter mAdapter;
    private Paint mUnderneathItemColor;

    public RecyclerViewItemCallback(ItemTouchHelperAdapter adapter, int colorUnderItem) {
        mAdapter = adapter;
        mUnderneathItemColor = new Paint();
        mUnderneathItemColor.setColor(colorUnderItem);
    }

    //Disable drag and drop
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    //Enable swipe
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //Method is invoked when the swipe gesture has ended
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.itemView, viewHolder.getAdapterPosition());
    }

    //Set directions that can be performed with swipe
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /*
     * "Adapter" interface to organize interaction between this class and some other.
     */
    public interface ItemTouchHelperAdapter {
        void onItemDismiss(View messageOutput, int position);
    }

    //Draw background for RecyclerView item while swiping
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX > 0) {
                // Draw Rect with varying right side, equal to displacement dX and with specified color
                c.drawRect((float) viewHolder.itemView.getLeft(), (float) viewHolder.itemView.getTop(), dX,
                        (float) viewHolder.itemView.getBottom(), mUnderneathItemColor);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
