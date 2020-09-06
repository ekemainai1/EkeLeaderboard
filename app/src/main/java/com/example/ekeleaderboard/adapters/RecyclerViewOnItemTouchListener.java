package com.example.ekeleaderboard.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private OnItemTouchListener onItemTouchListener;

    public RecyclerViewOnItemTouchListener(Context context, final RecyclerView recyclerView,
                                           final OnItemTouchListener onItemTouchListener) {
        this.onItemTouchListener = onItemTouchListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && onItemTouchListener != null) {
                    onItemTouchListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && onItemTouchListener != null && gestureDetector.onTouchEvent(e)) {
           onItemTouchListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }


}
