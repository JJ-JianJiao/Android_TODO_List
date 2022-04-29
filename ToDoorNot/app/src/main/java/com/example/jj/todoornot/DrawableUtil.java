package com.example.jj.todoornot;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class DrawableUtil {


    //textview drawable order number : 0 lfet, 1 top, 2 right, 3 bottom
    private final int RIGHT = 2;

    private OnDrawableListener mListener;
    private TextView mTextView;

    public DrawableUtil(TextView textView, OnDrawableListener listener) {
        mTextView = textView;
        mTextView.setOnTouchListener(mOnTouchListener);
        mListener = listener;
    }

    public interface OnDrawableListener{
        public void onRight(View v, Drawable right);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(mListener != null){
                        Drawable drawableRight = mTextView.getCompoundDrawables()[RIGHT];
                        if(drawableRight != null && event.getX() >= (mTextView.getRight() - drawableRight.getBounds().width())){
                            mListener.onRight(v, drawableRight);
                            return true;
                        }
                    }
                    break;
            }
            return false;
        }
    };
}
