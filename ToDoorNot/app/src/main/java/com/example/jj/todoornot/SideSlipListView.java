package com.example.jj.todoornot;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SideSlipListView extends ListView {

    private static final String TAG = "SideSlipListView";
    private int mScreenWidth;

    private int mDownX;
    private int mDownY;
    private boolean isDeleteShow;
    private ViewGroup mPointChild;
    private int mDeleteWidth;
    private LinearLayout.LayoutParams mItemLayoutParams;

    private int mPointPosition;//手指按下位置所在的item位置
    private boolean isAllowItemClick;//是否允许item点击


    public SideSlipListView(Context context) {
        super(context);
        getScreenWidth(context);
    }

    public SideSlipListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreenWidth(context);
    }

    public SideSlipListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenWidth(context);
    }

    public SideSlipListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getScreenWidth(context);
    }

    private void getScreenWidth(Context context){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        Log.i(TAG, "***********mScreenWidth: " + mScreenWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                performActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                performActionUp(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void performActionUp(MotionEvent ev) {
        if (-mItemLayoutParams.leftMargin >= mDeleteWidth / 2) {
            mItemLayoutParams.leftMargin = -mDeleteWidth;
            isDeleteShow = true;
            mPointChild.getChildAt(0).setLayoutParams(mItemLayoutParams);
        } else {
            turnNormal();
        }
    }

    private boolean  performActionMove(MotionEvent ev) {
        int nowX = (int)ev.getX();
        int nowY = (int)ev.getY();
        int diffX = nowX - mDownX;
        if(Math.abs(diffX) > Math.abs(nowY - mDownY) && Math.abs(nowY - mDownY) < 20){
            if (!isDeleteShow && nowX < mDownX) {//删除按钮未显示时向左滑
                if (-diffX >= mDeleteWidth) {//如果滑动距离大于删除组件的宽度时进行偏移的最大处理
                    diffX = -mDeleteWidth;
                }
                mItemLayoutParams.leftMargin = diffX;
                mPointChild.getChildAt(0).setLayoutParams(mItemLayoutParams);
                isAllowItemClick = false;
            } else if (isDeleteShow && nowX > mDownX) {//删除按钮显示时向右滑
                if (diffX >= mDeleteWidth) {
                    diffX = mDeleteWidth;
                }
                mItemLayoutParams.leftMargin = diffX - mDeleteWidth;
                mPointChild.getChildAt(0).setLayoutParams(mItemLayoutParams);
                isAllowItemClick = false;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void performActionDown(MotionEvent ev) {
        mDownX = (int)ev.getX();
        mDownY = (int)ev.getY();
        if(isDeleteShow){
            ViewGroup temViewGroup = (ViewGroup) getChildAt(pointToPosition(mDownX,mDownY) - getFirstVisiblePosition());
            Log.i(TAG,"*********mPointChild.equals(tmpViewGroup): " + mPointChild.equals(temViewGroup));
            if(!mPointChild.equals(temViewGroup)){
                turnNormal();
            }
        }

        mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX,mDownY) - getFirstVisiblePosition());

        mDeleteWidth = mPointChild.getChildAt(1).getLayoutParams().width;
        Log.i(TAG, "**********pointToPosition(x,y): " + pointToPosition(mDownX, mDownY)
                + ", getFirstVisiblePosition() = " + getFirstVisiblePosition()
                + ", mDeleteWidth = " + mDeleteWidth);
        mItemLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0).getLayoutParams();

        mItemLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mItemLayoutParams);
    }
    public boolean isAllowItemClick() {
        return isAllowItemClick;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isAllowItemClick = true;

                //侧滑删除
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mPointPosition = pointToPosition(mDownX, mDownY);
                Log.i(TAG, "*******pointToPosition(mDownX, mDownY): " + mPointPosition);
                if (mPointPosition != -1) {
                    if (isDeleteShow) {
                        ViewGroup tmpViewGroup = (ViewGroup) getChildAt(mPointPosition - getFirstVisiblePosition());
                        if (!mPointChild.equals(tmpViewGroup)) {
                            turnNormal();
                        }
                    }
                    //获取当前的item
                    mPointChild = (ViewGroup) getChildAt(mPointPosition - getFirstVisiblePosition());

                    mDeleteWidth = mPointChild.getChildAt(1).getLayoutParams().width;
                    mItemLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0).getLayoutParams();

                    Log.i(TAG, "*********mItemLayoutParams.height: " + mItemLayoutParams.height +
                            ", mDeleteWidth: " + mDeleteWidth);
                    mItemLayoutParams.width = mScreenWidth;
                    mPointChild.getChildAt(0).setLayoutParams(mItemLayoutParams);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int nowX = (int) ev.getX();
                int nowY = (int) ev.getY();
                int diffX = nowX - mDownX;
                Log.i(TAG, "******dp2px(4): " + dp2px(8) + ", dp2px(8): " + dp2px(8) +
                        ", density: " + getContext().getResources().getDisplayMetrics().density);
                if (Math.abs(diffX) > dp2px(4) || Math.abs(nowY - mDownY) > dp2px(4)) {
                    return true;//避免子布局中有点击的控件时滑动无效
                }
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void turnNormal() {
        mItemLayoutParams.leftMargin = 0;
        mPointChild.getChildAt(0).setLayoutParams(mItemLayoutParams);
        isDeleteShow = false;
    }

}
