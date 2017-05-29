package com.ustc.gjqapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by MaLei on 2017/3/19 0019.
 * Email:ml1995@mail.ustc.edu.cn
 * custom view
 */
public class CarbonWebView extends WebView {
    public CarbonWebView(Context context) {
        super(context);
    }

    public CarbonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarbonWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CarbonWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
    }

    //解决WebView盖住CardView导致CardView不响应点击事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }

    //解决ViewPager里非首屏WebView点击事件不响应
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //TODO: 允许长按选择文本

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
            return false;
        }else{
            return true;
        }
        //return super.onTouchEvent(ev);


    }
}
