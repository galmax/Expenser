package com.github.sinapple.expenser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Used to intercept the touch gesture above each RecyclerView item and provides an interface for handling it
 * Implements stock listener and attach convenient adapter.
 */
public class RecycleItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    /*
     * "Adapter" interface to organize interaction between this class and some other.
    */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public RecycleItemClickListener(Context context, OnItemClickListener listener) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        this.mListener = listener;
    }

    //Clarify item where event has been occurred
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
